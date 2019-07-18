package game;

import java.util.Random;

public class InkyGhost implements Ghost {
    private int currentPosX = STARTING_X;
    private int currentPosY = STARTING_Y;
    private int speed = NORM_SPEED;
    private boolean scared = false;
    private double score = 0;
    NeuralNetwork brain;
    private Grid[][] map;

    private int moveCounter = 0;

    public InkyGhost(Grid[][] maplayout){
        map = maplayout;
        brain = new NeuralNetwork(7, 14, 4);
    }
    public InkyGhost(Grid[][] maplayout, NeuralNetwork bigBrain){
        map = maplayout;
        brain = bigBrain;
    }

    public void setBrain(NeuralNetwork brain) {
        this.brain = brain;
    }

    @Override
    public void setSpeed(boolean slow) {
        if(slow){
            speed = SCARED_SPEED;
        }
        else{
            speed = NORM_SPEED;
        }
    }

    @Override
    public void setCurrentPos(int x, int y) {
        currentPosX = x;
        currentPosY = y;
    }

    @Override
    public void respawn(Grid[][] map) {
        map[currentPosY][currentPosX].setInky(false);
        setNormal();
        currentPosX = STARTING_X;
        currentPosY = STARTING_Y;
    }

    @Override
    public void setScared() {
        setSpeed(true);
        scared = true;
    }

    @Override
    public void setNormal() {
        setSpeed(false);
        scared = false;
    }

    public int think(double[] input){
        double []outputs = brain.calculate(input);
        //double up, down, left, right;
        int highestDir = 0;

        for(int i = 1; i < outputs.length; i++){
            if(outputs[highestDir]<outputs[i]){
                highestDir = i;
            }

        }
        return highestDir;
    }
    
    public double[] see(int px, int py) {
        
        double[] inputs = new double[7];

        // Whether or not there are walls in the four blocks around it
        inputs[0] = wallUp();
        inputs[1] = wallRight();
        inputs[2] = wallDown();
        inputs[3] = wallLeft();

        // Distance from the ghost to pacman
        inputs[4] = distanceFromPac(px, py);
        // Angle from ghost to pacman
        inputs[5] = closestDirToPac(px, py);
        if (scared) inputs[6] = 1;
        else inputs[6] = 0;

        return inputs;
    }

    public NeuralNetwork getBrain() {
        return brain;
    }


    public void translate(int dir) {
        // Up
        if (dir == 0) {
            if (!map[currentPosY - 1][currentPosX].isWall()) currentPosY -= speed;
            // Left
        } else if (dir == 1) {
            try {
                if (!map[currentPosY][currentPosX-1].isWall()) currentPosX -= speed;
            } catch (ArrayIndexOutOfBoundsException ex) {
                currentPosX = 31;
            }
            // Right
        } else if (dir == 2) {
            try {
                if (!map[currentPosY][currentPosX+1].isWall()) currentPosX += speed;
            } catch (IndexOutOfBoundsException ex) {
                currentPosX = 0;
            }
            // Down
        } else if (dir == 3) {
            if (!map[currentPosY + 1][currentPosX].isWall()) currentPosY += speed;
        }
    }

    @Override
    public void move(int dir, Grid[][] map) {

        // Implements half speed if the ghost is scared
        if (scared) {
            if (moveCounter >= 2) {
                translate(dir);
                moveCounter = 0;
            } else {
                moveCounter++;
            }
        } else {
            translate(dir);
        }
    }

    @Override
    public boolean getScared() {
        return scared;
    }

    @Override
    public double distanceFromPac(int px, int py) {
        return Math.sqrt(Math.pow(px-currentPosX, 2)+ Math.pow(py-currentPosY, 2));
    }

    @Override
    public double closestDirToPac(int px, int py) {
        return (Math.sin(getCurrentY()/distanceFromPac(px, py))*distanceFromPac(px, py));
    }

    @Override
    public int getCurrentX() {
        return currentPosX;
    }

    @Override
    public int getCurrentY() {
        return currentPosY;
    }

    @Override
    public double wallUp() {
        if (map[currentPosY-1][currentPosX].isWall()) return 1;
        else return 0;
    }

    @Override
    public double wallDown() {
        if (map[currentPosY+1][currentPosX].isWall()) return 1;
        else return 0;
    }

    @Override
    public double wallRight() {
        if (map[currentPosY][currentPosX+1].isWall()) return 1;
        else return 0;
    }

    @Override
    public double wallLeft() {
        if (map[currentPosY][currentPosX-1].isWall()) return 1;
        else return 0;  
    }

    @Override
    public void addScore(double add) {
        score  = score + add;
    }

    @Override
    public double getScore() {
        return score;
    }
}