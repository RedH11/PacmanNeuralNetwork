package game;

public interface Ghost {

    int STARTING_X = 0;
    int STARTING_Y = 0;


    int NORM_SPEED = 2;
    int SCARED_SPEED = 1;
    void setSpeed(boolean slow);


    void respawn();
    void setScared();
    void setNormal();
    void move(int x, int y, int px, int py, boolean scared);
    boolean getScared();

    int getCurrentX();

    int getCurrentY();
}
