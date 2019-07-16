package gamePack;

public class Pacman {
    private int speed = 1;
    private boolean isPowered = false;
    private int currentPosX = 16;
    private int currentPosY = 20;
    private int dir = -1;
    private int score = 0;
    private boolean isAlive = true;



    public void move(Grid[][] gameMap){

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

        if (gameMap[currentPosX][currentPosY].isGhost()) {
            isAlive = false;
            gameMap[currentPosX][currentPosY].setEmpty(true);
        }

        if (gameMap[currentPosX][currentPosY].isPellet()) {
            score += 100;
            gameMap[currentPosX][currentPosY].setPellet(false);
        }

        if (gameMap[currentPosX][currentPosY].isPowerPellet()) {
            score += 100;
            gameMap[currentPosX][currentPosY].setPowerPellet(false);
            isPowered = true;
        }
    }

    public void pacman() {
        setCurrentPosX(16);
        setCurrentPosY(20);
        this.score = 0;
        this.speed = 1;
        this.isPowered = false;
        this.isAlive = true;
        this.dir = -1;
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

    public void setPowered(boolean powered) {
        isPowered = powered;
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
