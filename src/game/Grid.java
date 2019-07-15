package game;

public class Grid {
    // Wall Types
    private boolean vWall;
    private boolean hWall;
    private boolean tLWall;
    private boolean tRWall;
    private boolean bLWall;
    private boolean bRWall;

    private boolean empty;

    // Food Types
    private boolean pellet;
    private boolean powerPellet;


    // Sprite Types
    private boolean pacman;
    // Red ghost
    private boolean blinky;
    // Pink ghost
    private boolean pinky;
    // Blue ghost
    private boolean inky;
    // Orange ghost
    private boolean clyde;
    
    public Grid() {
        // Wall Types
         vWall = false;
         hWall = false;
         tLWall = false;
         tRWall = false;
         bLWall = false;
         bRWall = false;

         empty = false;

        // Food Types
         pellet = false;
         powerPellet = false;


        // Sprite Types
         pacman = false;
        // Red ghost
         blinky = false;
        // Pink ghost
         pinky = false;
        // Blue ghost
         inky = false;
        // Orange ghost
         clyde = false;
    }

    public void setPellet(boolean pellet) {
        this.pellet = pellet;
    }

    public void setPowerPellet(boolean powerPellet) {
        this.powerPellet = powerPellet;
    }

    public void setPacman(boolean pacman) {
        this.pacman = pacman;
    }

    public void setPinky(boolean pinky) {
        this.pinky = pinky;
    }

    public void settLWall(boolean tLWall) {
        this.tLWall = tLWall;
    }

    public void settRWall(boolean tRWall) {
        this.tRWall = tRWall;
    }

    public void setvWall(boolean vWall) {
        this.vWall = vWall;
    }

    public boolean isPellet() {
        return pellet;
    }

    public boolean isPowerPellet() {
        return powerPellet;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setInky(boolean inky) {
        this.inky = inky;
    }

    public void sethWall(boolean hWall) {
        this.hWall = hWall;
    }

    public void setClyde(boolean clyde) {
        this.clyde = clyde;
    }

    public void setbRWall(boolean bRWall) {
        this.bRWall = bRWall;
    }

    public void setbLWall(boolean bLWall) {
        this.bLWall = bLWall;
    }

    public void setBlinky(boolean blinky) {
        this.blinky = blinky;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }

    public boolean isvWall() {
        return vWall;
    }

    public boolean istRWall() {
        return tRWall;
    }

    public boolean istLWall() {
        return tLWall;
    }

    public boolean isPinky() {
        return pinky;
    }

    public boolean isPacman() {
        return pacman;
    }

    public boolean isInky() {
        return inky;
    }

    public boolean ishWall() {
        return hWall;
    }

    public boolean isbRWall() {
        return bRWall;
    }

    public boolean isbLWall() {
        return bLWall;
    }

    public boolean isClyde() {
        return clyde;
    }

    public boolean isWall() {
        if (vWall || hWall || tLWall || bLWall || bRWall || tRWall) return true;
        return false;
    }

    public boolean isBlinky() {
        return blinky;
    }
}
