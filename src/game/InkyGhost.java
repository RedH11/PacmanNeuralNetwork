package game;

import java.util.Random;

public class InkyGhost implements Ghost {
    private int currentPosX = STARTING_X;
    private int currentPosY = STARTING_Y;
    private int speed = NORM_SPEED;
    private boolean scared = false;
    private double score = 0;

    private Grid[][] map;

    public InkyGhost(Grid[][]maplayout){
        map = maplayout;
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

    @Override
    public void move(int dir, Grid[][] map) {
        if(dir == 0){
            setCurrentPos(currentPosX, currentPosY-speed);
        }
        if(dir == 1){
            setCurrentPos(currentPosX - speed, currentPosY);
        }
        if(dir == 2){
            setCurrentPos(currentPosX + speed, currentPosY);
        }
        if(dir ==3){
            setCurrentPos(currentPosX, currentPosY + speed);
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