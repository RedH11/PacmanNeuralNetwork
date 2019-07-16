package game;

import java.util.Random;

public class ClydeGhost implements Ghost {
    private int currentPosX = STARTING_X;
    private int currentPosY = STARTING_Y;
    private int speed = NORM_SPEED;
    private boolean scared = false;

    private Grid[][]map;

    public ClydeGhost(Grid[][]maplayout){
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
    public void move(int px, int py, boolean scared) {
        int x = 0;
        int y = 0;
        Random rand = new Random();//can evolve on this
        if(!scared) {
            if (px > currentPosX) {
                x = 1;
                y = 0;
                if ((px - 5 < currentPosX) || (px + 5 > currentPosX)) {
                    x = rand.nextInt(11) - 10;
                    if (x == 0) {
                        y = rand.nextInt(10) - 10;
                    }
                }
            }
            if (px < currentPosX) {
                x = -1;
                y = 0;
                if ((px - 5 < currentPosX) || (px + 5 > currentPosX)) {
                    x = rand.nextInt(11) - 10;
                    if (x == 0) {
                        y = rand.nextInt(10) - 10;
                    }
                }
            }
            if (px == currentPosX && py > currentPosY) {
                x = 0;
                y = 1;
                if ((py - 5 < currentPosY) || (py + 5 > currentPosY)) {
                    x = rand.nextInt(11) - 10;
                    if (x == 0) {
                        y = rand.nextInt(10) - 10;
                    }
                }
            }
            if (px == currentPosX && py < currentPosY) {
                x = 0;
                y = -1;
                if ((py - 5 < currentPosY) || (py + 5 > currentPosY)) {
                    x = rand.nextInt(11) - 10;
                    if (x == 0) {
                        y = rand.nextInt(10) - 10;
                    }
                }
            }
        }
        else{
            if (px > currentPosX) {
                x = -1;
                y = 0;
            }
            if (px < currentPosX) {
                x = 1;
                y = 0;
            }
            if (px == currentPosX && py > currentPosY) {
                x = 0;
                y = -1;
            }
            if (px == currentPosX && py < currentPosY) {
                x = 0;
                y = 1;
            }
        }
        if(map[currentPosY][currentPosX-1].isWall()){
            x=0;
            y=1;
        }
        else if(map[currentPosY-1][currentPosX].isWall()){
            x=1;
            y=0;
        }
        else if(map[currentPosY][currentPosX+1].isWall()){
            x=0;
            y=1;
        }
        else if(map[currentPosY+1][currentPosX].isWall()){
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