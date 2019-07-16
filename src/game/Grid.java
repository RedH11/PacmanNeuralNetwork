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
        clearGrid(pacman);
    }

    public void setPinky(boolean pinky) {
        this.pinky = pinky;
        if (pinky) empty = false;
        if (isWall()) pinky = false;
    }

    public void settLWall(boolean tLWall) {
        clearWalls();
        this.tLWall = tLWall;
        clearGrid(tLWall);
    }

    public void settRWall(boolean tRWall) {
        clearWalls();
        this.tRWall = tRWall;
        clearGrid(tRWall);
    }

    public void setvWall(boolean vWall) {
        clearWalls();
        this.vWall = vWall;
        clearGrid(vWall);
    }

    public void sethWall(boolean hWall) {
        clearWalls();
        this.hWall = hWall;
        clearGrid(hWall);
    }

    public void setClyde(boolean clyde) {
        this.clyde = clyde;
        if (clyde) empty = false;
        if (isWall()) clyde = false;
    }

    public void setbRWall(boolean bRWall) {
        clearWalls();
        this.bRWall = bRWall;
        clearGrid(bRWall);
    }

    public void setbLWall(boolean bLWall) {
        clearWalls();
        this.bLWall = bLWall;
        clearGrid(bLWall);
    }

    public void setBlinky(boolean blinky) {
        this.blinky = blinky;
        if (blinky) empty = false;
        if (isWall()) blinky = false;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
        if (empty) {
            pellet = false;
            pacman = false;
        }
        clearWalls();
    }

    public void setInky(boolean inky) {
        this.inky = inky;
        if (inky) empty = false;
        if (isWall()) inky = false;
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
