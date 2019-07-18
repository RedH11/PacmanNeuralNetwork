package game;

import java.util.Random;

public class Pacman {
    private int speed = 1;
    private boolean isPowered = false;
    private int currentPosX = 16;
    private int currentPosY = 20;
    private int dir = -1;
    private int score = 0;
    private boolean isAlive = true;

    public Pacman() {
        setCurrentPosX(16);
        setCurrentPosY(20);
        this.score = 0;
        this.speed = 1;
        this.isPowered = false;
        this.isAlive = true;
        this.dir = -1;
    }

    public void move(Grid[][] gameMap) {

        // Up
        if (dir == 0) {
            if (!gameMap[currentPosY - 1][currentPosX].isWall()) currentPosY -= speed;
            // Left
        } else if (dir == 1) {
            try {
                if (!gameMap[currentPosY][currentPosX-1].isWall()) currentPosX -= speed;
            } catch (ArrayIndexOutOfBoundsException ex) {
                currentPosX = 31;
            }
            // Right
        } else if (dir == 2) {
            try {
                if (!gameMap[currentPosY][currentPosX+1].isWall()) currentPosX += speed;
            } catch (IndexOutOfBoundsException ex) {
                currentPosX = 0;
            }
            // Down
        } else if (dir == 3) {
            if (!gameMap[currentPosY + 1][currentPosX].isWall()) currentPosY += speed;
        }

        /*
        // If he collides with a ghost
        if (gameMap[currentPosX][currentPosY].isGhost() && !isPowered) {
            isAlive = false;
            gameMap[currentPosX][currentPosY].setEmpty(true);
            System.out.println("DEAD");
        }*/

        if (gameMap[currentPosX][currentPosY].isPellet()) {
            score += 10;
            gameMap[currentPosX][currentPosY].setPellet(false);
        }

        if (gameMap[currentPosX][currentPosY].isPowerPellet()) {
            score += 50;
            gameMap[currentPosX][currentPosY].setPowerPellet(false);
            isPowered = true;
        }
    }

    // Generates a random direction other than the current one
    public int randDir(int prevNum) {
        Random num = new Random();

        int dir = num.nextInt(4);
        while (dir == prevNum) {
            dir = num.nextInt(4);
        }

        return dir;
    }

    // Bot that moves pacman in random directions
    public void moveBot(Grid[][] gameMap) {
        int prevNum;

        if (dir == -1) dir = 2;

        // Up
        if (dir == 0) {
            if (!gameMap[currentPosY - 1][currentPosX].isWall() && !gameMap[currentPosY - 1][currentPosX].isGhost()) currentPosY -= speed;
            else {
                prevNum = dir;
                dir = randDir(prevNum);
            }
            // Left
        } else if (dir == 1) {
            try {
                if (!gameMap[currentPosY][currentPosX-1].isWall() && !gameMap[currentPosY][currentPosX-1].isGhost()) currentPosX -= speed;
                else {
                    prevNum = dir;
                    dir = randDir(prevNum);
                }

            } catch (ArrayIndexOutOfBoundsException ex) {
                currentPosX = 31;
            }
            // Right
        } else if (dir == 2) {
            try {
                if (!gameMap[currentPosY][currentPosX+1].isWall() && !gameMap[currentPosY][currentPosX+1].isGhost()) currentPosX += speed;
                else {
                    prevNum = dir;
                    dir = randDir(prevNum);
                }
            } catch (IndexOutOfBoundsException ex) {
                currentPosX = 0;
            }
            // Down
        } else if (dir == 3) {
            if (!gameMap[currentPosY + 1][currentPosX].isWall() && !gameMap[currentPosY+1][currentPosX].isGhost()) currentPosY += speed;
            else {
                prevNum = dir;
                dir = randDir(prevNum);
            }
        }

        if (gameMap[currentPosX][currentPosY].isGhost()) {
            isAlive = false;
            gameMap[currentPosX][currentPosY].setEmpty(true);
        }

        if (gameMap[currentPosX][currentPosY].isPellet()) {
            //score += 100;
            gameMap[currentPosX][currentPosY].setPellet(false);
        }

        if (gameMap[currentPosX][currentPosY].isPowerPellet()) {
            //score += 100;
            gameMap[currentPosX][currentPosY].setPowerPellet(false);
            isPowered = true;
        }

    }

    public void setPowered(boolean powered) {
        isPowered = powered;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public void setCurrentPosX(int x) {
        currentPosX = x;
    }

    public void setCurrentPosY(int y) {
        currentPosX = y;
    }

    public void setDir(int dir) {
        this.dir = dir;
    }

    public int getDir() {
        return dir;
    }

    public boolean isPowered() {
        return isPowered;
    }

    public int getCurrentPosX() {
        return currentPosX;
    }

    public int getCurrentPosY() {
        return currentPosY;
    }

    public boolean isAlive() {
        return isAlive;
    }
}