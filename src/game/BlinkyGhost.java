package game;

public class BlinkyGhost implements Ghost{
    private int currentPosX = STARTING_X;
    private int currentPosY = STARTING_Y;
    private int speed = NORM_SPEED;
    private boolean scared = false;

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
        if(!scared) {
            if (px > currentPosX) {
                x = 1;
                y = 0;
            }
            if (px < currentPosX) {
                x = -1;
                y = 0;
            }
            if (px == currentPosX && py > currentPosY) {
                x = 0;
                y = 1;
            }
            if (px == currentPosX && py < currentPosY) {
                x = 0;
                y = -1;
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
        //TODO add wall check
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
