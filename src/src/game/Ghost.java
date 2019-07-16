package game;

public interface Ghost {

    int STARTING_X = 16;
    int STARTING_Y = 16;


    int NORM_SPEED = 1;
    int SCARED_SPEED = 1;
    void setSpeed(boolean slow);

    void setCurrentPos(int x, int y);
    void respawn();
    void setScared();
    void setNormal();
    void move(int px, int py, boolean scared);
    boolean getScared();

    int getCurrentX();

    int getCurrentY();
}