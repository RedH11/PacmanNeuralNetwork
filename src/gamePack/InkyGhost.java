package gamePack;

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
    public void move(int px, int py, boolean scared) {
        int dir = 0;
        Random rand = new Random();//can evolve on this
        /*
        int x = 0;
        int y = 0;
        Random rand = new Random();//can evolve on this
        x = rand.nextInt(11)-10;
        if(x ==0){
            y = rand.nextInt(10)-10;
        }
        else if(map[currentPosY][currentPosX-1].isWall()){
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
        */
        dir = rand.nextInt(3);
        if(!scared) {
            // Up
            if (dir == 0) {
                // If there is a wall above the ghost, go right or left towards pacman
                if (!map[currentPosY - 1][currentPosX].isWall()) currentPosY -= speed;
                    // Right
                else if (!map[currentPosY][currentPosX + 1].isWall() && px >= currentPosX) currentPosX += speed;
                    //Down (and pacman is lower)
                else if (!map[currentPosY][currentPosX - 1].isWall() && py >= currentPosY) currentPosX -= speed;
                    // Left
                else if (!map[currentPosY - 1][currentPosX].isWall()) currentPosY += speed;
                // Left
            } else if (dir == 1) {
                // The try method is in case it goes through the tunnel
                try {
                    // Tries to go left
                    if (!map[currentPosY][currentPosX - 1].isWall()) currentPosX -= speed;
                        // If there is a wall and pacman is below go down
                    else if (!map[currentPosY][currentPosX + 1].isWall() && py >= currentPosY) currentPosY += speed;
                        //If there is a wall below / pacman is above go up
                    else if (!map[currentPosY - 1][currentPosX].isWall() && py <= currentPosY) currentPosY -= speed;
                        // Otherwise go right if there is no wall there
                    else if (!map[currentPosY][currentPosX + 1].isWall()) currentPosX += speed;
                } catch (ArrayIndexOutOfBoundsException ex) {
                    currentPosX = 31;
                }
                // Right
            } else if (dir == 2) {
                // The try method is in case it goes through the tunnel
                try {
                    // Tries to go right
                    if (!map[currentPosY][currentPosX + 1].isWall()) currentPosX += speed;
                        // If there is a wall and pacman is below go down
                    else if (!map[currentPosY][currentPosX + 1].isWall() && py >= currentPosY) currentPosY += speed;
                        //If there is a wall below / pacman is above go up
                    else if (!map[currentPosY - 1][currentPosX].isWall() && py <= currentPosY) currentPosY -= speed;
                        // Otherwise go left if there is no wall there
                    else if (!map[currentPosY][currentPosX + 1].isWall()) currentPosX -= speed;
                } catch (IndexOutOfBoundsException ex) {
                    currentPosX = 0;
                }
                // Down
            } else if (dir == 3) {
                // Tries to go down
                if (!map[currentPosY + 1][currentPosX].isWall()) currentPosY += speed;
                    // If there is a wall and pacman is to the right try to go right
                else if (!map[currentPosY][currentPosX + 1].isWall() && px >= currentPosX) currentPosX += speed;
                    // If there is a wall to the right / pacman is to the left try to go left
                else if (!map[currentPosY][currentPosX - 1].isWall() && py >= currentPosY) currentPosX -= speed;
                    // Otherwise try to go up
                else if (!map[currentPosY - 1][currentPosX].isWall()) currentPosY -= speed;
            }
        }
        else{
            // Up
            if (dir == 0) {
                // If there is a wall above the ghost, go right or left towards pacman
                if (!map[currentPosY - 1][currentPosX].isWall()) currentPosY -= speed;
                    // Right
                else if (!map[currentPosY][currentPosX + 1].isWall() && px >= currentPosX) currentPosX += speed;
                    //Down (and pacman is lower)
                else if (!map[currentPosY][currentPosX - 1].isWall() && py >= currentPosY) currentPosX -= speed;
                    // Left
                else if (!map[currentPosY - 1][currentPosX].isWall()) currentPosY += speed;
                // Left
            } else if (dir == 1) {
                // The try method is in case it goes through the tunnel
                try {
                    // Tries to go left
                    if (!map[currentPosY][currentPosX - 1].isWall()) currentPosX -= speed;
                        // If there is a wall and pacman is below go down
                    else if (!map[currentPosY][currentPosX + 1].isWall() && py >= currentPosY) currentPosY += speed;
                        //If there is a wall below / pacman is above go up
                    else if (!map[currentPosY - 1][currentPosX].isWall() && py <= currentPosY) currentPosY -= speed;
                        // Otherwise go right if there is no wall there
                    else if (!map[currentPosY][currentPosX + 1].isWall()) currentPosX += speed;
                } catch (ArrayIndexOutOfBoundsException ex) {
                    currentPosX = 31;
                }
                // Right
            } else if (dir == 2) {
                // The try method is in case it goes through the tunnel
                try {
                    // Tries to go right
                    if (!map[currentPosY][currentPosX + 1].isWall()) currentPosX -= speed;
                        // If there is a wall and pacman is below go down
                    else if (!map[currentPosY][currentPosX + 1].isWall() && py >= currentPosY) currentPosY -= speed;
                        //If there is a wall below / pacman is above go up
                    else if (!map[currentPosY - 1][currentPosX].isWall() && py <= currentPosY) currentPosY += speed;
                        // Otherwise go left if there is no wall there
                    else if (!map[currentPosY][currentPosX + 1].isWall()) currentPosX += speed;
                } catch (IndexOutOfBoundsException ex) {
                    currentPosX = 0;
                }
                // Down
            } else if (dir == 3) {
                // Tries to go down
                if (!map[currentPosY + 1][currentPosX].isWall()) currentPosY -= speed;
                    // If there is a wall and pacman is to the right try to go right
                else if (!map[currentPosY][currentPosX + 1].isWall() && px >= currentPosX) currentPosX -= speed;
                    // If there is a wall to the right / pacman is to the left try to go left
                else if (!map[currentPosY][currentPosX - 1].isWall() && py >= currentPosY) currentPosX += speed;
                    // Otherwise try to go up
                else if (!map[currentPosY - 1][currentPosX].isWall()) currentPosY += speed;
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
