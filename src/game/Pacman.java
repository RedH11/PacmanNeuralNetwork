package game;

public class Pacman {
    private int speed = 2;
    private boolean isPowered = false;
    private int currentPosX = 0;
    private int currentPosY = 0;

    public void move(int x, int y){
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
}
