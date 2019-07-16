package game;

public class Pacman {
    private final int speed = 2;
    private boolean isPowered = false;
    private int currentPosX = 0;
    private int currentPosY = 0;
    private int score = 0;

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

    }

    public void setPowered(boolean powered) {
        isPowered = powered;
    }

    public boolean isPowered() {
        return isPowered;
    }

    private int getCurrentPosX() {
        return currentPosX;
    }

    private int getCurrentPosY() {
        return currentPosY;
    }
}
