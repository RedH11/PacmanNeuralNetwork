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
        clearWalls();
        this.pellet = pellet;
        if (pellet) empty = false;
    }

    public void setPowerPellet(boolean powerPellet) {
        clearGrid(powerPellet);
        this.powerPellet = powerPellet;
    }

    public void setPacman(boolean pacman) {
        this.pacman = pacman;
        if (pacman) empty = false;
    }

    public void setPinky(boolean pinky) {
        this.pinky = pinky;
        if (isWall()) pinky = false;
        if (pinky) empty = false;
    }

    public void setvWall(boolean vWall) {
        this.vWall = vWall;
        clearGrid(true);
    }

    public void sethWall(boolean hWall) {
        this.hWall = hWall;
        clearGrid(true);
    }

    public void setClyde(boolean clyde) {
        this.clyde = clyde;
        if (isWall()) clyde = false;
        if (clyde) empty = false;
    }

    public void setBlinky(boolean blinky) {
        this.blinky = blinky;
        if (isWall()) blinky = false;
        if (blinky) empty = false;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
        if (empty) {
            pellet = false;
            pacman = false;
            inky = false;
        }
        clearWalls();
    }

    public void setInky(boolean inky) {
        this.inky = inky;
        if (isWall()) inky = false;
        if (inky) empty = false;
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

    public boolean isGhost() {
        if (blinky || pinky || clyde || inky) return true;
        return false;
    }

    public boolean isBlinky() {
        return blinky;
    }

    public void clearGrid(boolean clear) {
        if (clear) {
            empty = false;
            pellet = false;
            powerPellet = false;
        }
    }

    public void clearWalls() {
        hWall = false;
        vWall = false;
        bRWall = false;
        bLWall = false;
        tRWall = false;
        tLWall = false;
    }
}
