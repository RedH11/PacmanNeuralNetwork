package game;

public interface Ghost {
    /*
    -inputs
        -distance from pacman
        -wall in all directions?
        -closest direction to pac man
        -scared?
      -outputs
        -weighted directions
     */
    int STARTING_X = 16;
    int STARTING_Y = 12;
    int score = 0;

    int NORM_SPEED = 1;
    int SCARED_SPEED = 1;

    void setSpeed(boolean slow);

    void setCurrentPos(int x, int y);

    void respawn();

    void setScared();
    void setNormal();
    void move(int dir);
    boolean getScared();

    double distanceFromPac(int px, int py);
    double closestDirToPac(int px, int py);

    int getCurrentX();
    int getCurrentY();

    boolean wallUp();
    boolean wallDown();
    boolean wallRight();
    boolean wallLeft();
    void addScore(int add);
    int getScore();
}