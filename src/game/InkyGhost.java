package game;

import java.util.Random;

public class InkyGhost implements Ghost {
    private int currentPosX = STARTING_X;
    private int currentPosY = STARTING_Y;
    private int speed = NORM_SPEED;
    private boolean scared = false;

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
    public void respawn() {
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
    public void move(int x, int y, int px, int py, boolean scared) {
        Random rand = new Random();//can evolve on this
        x = rand.nextInt(11)-10;
        if(x ==0){
            y = rand.nextInt(10)-10;
        }
        if(map[currentPosY][currentPosX+1].isWall()){
            x=0;
            y=1;
        }
        if(map[currentPosY+1][currentPosX].isWall()){
            x=1;
            y=0;
        }
        if(map[currentPosY][currentPosX-1].isWall()){
            x=0;
            y=1;
        }
        if(map[currentPosY-1][currentPosX].isWall()){
            x=1;
            y=0;
        }
        else if(map[currentPosY-1][currentPosX-1].isWall()){
            x=1;
            y=0;
        }
        else if(map[currentPosY-1][currentPosX+1].isWall()){
            x=0;
            y=1;
        }
        else if(map[currentPosY+1][currentPosX-1].isWall()){
            x=0;
            y=-1;
        }
        else if(map[currentPosY +1 ][currentPosX +1].isWall()){
            x=-1;
            y=0;
        }

        //check grid object
        if(x != 0 && y == 0){
            if(x > 0){
                currentPosX = currentPosX + speed;
            }
            else{
                currentPosX = currentPosY - speed;
            }
        }
        else if(y != 0 && x == 0){
            if(y > 0){
                currentPosY = currentPosY + speed;
            }
            else{
                currentPosY = currentPosY - speed;
            }
        }
        else{
            throw new java.lang.IllegalArgumentException("Cannot move diagonal");
        }
    }

    @Override
    public boolean getScared() {
        return scared;
    }

    @Override
    public int getCurrentX() {
        return currentPosX;
    }

    @Override
    public int getCurrentY() {
        return currentPosY;
    }
}