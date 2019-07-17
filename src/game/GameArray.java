package game;

import java.util.Random;

public class GameArray {

    // Make a 32x32 2D grid as the game
    Grid[][] gameMap;

    Pacman pacman;
    PinkyGhost pinkyGhost;
    InkyGhost inkyGhost;
    BlinkyGhost blinkyGhost;
    ClydeGhost clydeGhost;


    public GameArray(MapLayout map) {
        gameMap = map.getLayout();
        // Make new pacman for the game
        pacman = new Pacman();
        pinkyGhost = new PinkyGhost(gameMap);
        inkyGhost = new InkyGhost(gameMap);
        blinkyGhost = new BlinkyGhost(gameMap);
        clydeGhost = new ClydeGhost(gameMap);
    }

    public void updatePacman() {

        // Deletes space behind pacman
        if (pacman.getDir() != -1) gameMap[pacman.getCurrentPosY()][pacman.getCurrentPosX()].setEmpty(true);
        pacman.move(gameMap);
        gameMap[pacman.getCurrentPosY()][pacman.getCurrentPosX()].setPacman(true);

    }

    public void updateGhosts() {
        // Spawn ghosts in the corner
        gameMap[pinkyGhost.getCurrentY()][pinkyGhost.getCurrentX()].setPinky(false);
        pinkyGhost.move(pacman.getCurrentPosX(), pacman.getCurrentPosY(), false);
        gameMap[pinkyGhost.getCurrentY()][pinkyGhost.getCurrentX()].setPinky(true);

        gameMap[inkyGhost.getCurrentY()][inkyGhost.getCurrentX()].setInky(false);
       // inkyGhost.move(pacman.getCurrentPosX(), pacman.getCurrentPosY(), false);
        gameMap[inkyGhost.getCurrentY()][inkyGhost.getCurrentX()].setInky(true);

        gameMap[blinkyGhost.getCurrentY()][blinkyGhost.getCurrentX()].setBlinky(false);
        blinkyGhost.move(pacman.getCurrentPosX(), pacman.getCurrentPosY(), false);
        gameMap[blinkyGhost.getCurrentY()][blinkyGhost.getCurrentX()].setBlinky(true);

        gameMap[clydeGhost.getCurrentY()][clydeGhost.getCurrentX()].setClyde(false);
        clydeGhost.move(pacman.getCurrentPosX(), pacman.getCurrentPosY(), false);
        gameMap[clydeGhost.getCurrentY()][clydeGhost.getCurrentX()].setClyde(true);
    }

    public void setPacmanDir(int dir) {
        pacman.setDir(dir);
    }

    public boolean pacmanIsAlive() {
        return pacman.isAlive();
    }

    public Grid[][] getLayout() {
        return gameMap;
    }

    public void outputConsoleMap() {
        for (int r = 0; r < gameMap.length; r++) {
            for (int c = 0; c < gameMap.length; c++) {
                if (gameMap[r][c].isvWall()) System.out.print("| ");
                    // Doubled to make a square because the vertical is long
                else if (gameMap[r][c].ishWall()) System.out.print("- ");
                else if (gameMap[r][c].isPacman()) System.out.print("C ");
                else if (gameMap[r][c].isbLWall()) System.out.print("\\ ");
                else if (gameMap[r][c].isbRWall()) System.out.print("/ ");
                else if (gameMap[r][c].istLWall()) System.out.print("/ ");
                else if (gameMap[r][c].istRWall()) System.out.print("\\ ");
                    // Is ghost
                else if (gameMap[r][c].isPinky()) System.out.println("P ");
                else if (gameMap[r][c].isBlinky()) System.out.println("B ");
                else if (gameMap[r][c].isInky()) System.out.println("I ");
                else if (gameMap[r][c].isClyde()) System.out.println("8 ");
                else if (gameMap[r][c].isEmpty()) System.out.print("  ");
                else if (gameMap[r][c].isPellet()) System.out.print(". ");
                else if (gameMap[r][c].isPowerPellet()) System.out.print("* ");

            }
            System.out.println();
        }
    }
}
