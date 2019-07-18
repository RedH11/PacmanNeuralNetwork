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

    public InkyGhost(Grid[][] maplayout){
        map = maplayout;
        brain = new NeuralNetwork(7, 14, 4);
    }
    public InkyGhost(Grid[][] maplayout, NeuralNetwork bigBrain){
        map = maplayout;
        brain = bigBrain;
    }


    public NeuralNetwork getBrain() {
        return brain;
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

    public int think(double[]input){
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
    @Override
    public void move(int dir, Grid[][] map) {
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
    public double []getInputs(int px, int py){
        double[]temp = new double [7];
        if(!wallUp())temp[0]=1.0;
        else temp[0]=0.0;
        if(!wallRight())temp[1]=1.0;
        else temp[1]=0.0;
        if(!wallDown())temp[2] = 1.0;
        else temp[2] = 0.0;
        if(!wallLeft())temp[3]=1.0;
        else temp[3]=1.0;
        temp[4] = distanceFromPac(px, py);
        temp[5] = closestDirToPac(px, py);
        if(!getScared())temp[6] = 1.0;
        else temp[6] = 0.0;

        return temp;
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
    public boolean wallUp() {
        return map[currentPosY+1][currentPosX].isWall();
    }

    @Override
    public boolean wallDown() {
        return map[currentPosY-1][currentPosX].isWall();    }

    @Override
    public boolean wallRight() {
        return map[currentPosY][currentPosX+1].isWall();
    }

    @Override
    public boolean wallLeft() {
        return map[currentPosY][currentPosX-1].isWall();    }

    @Override
    public void addScore(double add) {
        score  = score + add;
    }

    @Override
    public double getScore() {
        return score;
    }
}
