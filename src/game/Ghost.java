package game;

public class Ghost {
    private boolean scared = false;
    final int STARTING_X = 0;
    final int STARTING_Y = 0;
    private int currentPosX = 0;
    private int currentPosY = 0;

    //Sprite and Strat defined by the child
    int speed;
    final int NORM_SPEED = 2;
    final int SCARED_SPEED = 1;
    private void setSpeed(boolean slow){
        if(slow){
            speed = SCARED_SPEED;
        }
        else{
            speed = NORM_SPEED;
        }
    }
    public void respawn(){

        setNormal();
        currentPosX = STARTING_X;
        currentPosY = STARTING_Y;
    }
    public void setScared(){
        scared = true;
        setSpeed(true);//change color
    }
    public void setNormal(){
        scared = false;
        //change color back to regular
        setSpeed(false);
    }
    public void move(int x, int y) {
        //check collision here
        //change x and y depending on collision

        if ((x != 0) && (y == 0)){
            if(x > 0){
                currentPosX = currentPosX + speed;
            }
            else{
                currentPosX = currentPosX - speed;
            }
        }
        else if((y != 0) && (x == 0)){
            if(y > 0){
                currentPosY = currentPosY + speed;
            }
            else{
                currentPosY = currentPosY - speed;
            }
        }
        else{
            throw new java.lang.IllegalArgumentException("x and y cannot both have values");
        }


    }
    public boolean getScared(){
        return scared;
    }
    public int getCurrentX(){
        return currentPosX;
    }
    public int getCurrentY(){
        return currentPosY;
    }
}
