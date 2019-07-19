package game;

import java.util.Random;

public class Pacman {
    Random num = new Random();
    int x;
    int y;
    private int dir = num.nextInt(4); // Start pacman in a random direction
    private int fitness = 0;
    // Accessible traits
    boolean powered = false;
    boolean alive = true;

    int poweredMoves = 0;
    // How many moves pacman gets when powered
    int maxPoweredMoves = 50;

    public Pacman() {
        respawn();
    }

    //Respawns pacman at start position
    private void respawn() {
        x = 13; // 9
        y = 17; // 20
        powered = false;
        alive = true;
    }

    /**
     * Pacman's move function
     * @param map the game map
     * @param Ix Inky's x coordinate
     * @param Iy Inky's y coordinate
     */
    private void move(Tile[][] map, int Ix, int Iy) {
        //Respawn
        if (!alive) respawn();

        int prevX = x;
        int prevY = y;

        // map[x][y]

        // 0: Up / 1: Left / 2: Right / 3: Down
        switch (dir) {
            case 0:
                if (!wallInWay(dir, map) && !(Ix == x && Iy == y - 1)) y--;
                else dir = randDir(dir);
                break;
            case 1:
                if (!wallInWay(dir, map) && !(Ix == x - 1 && Iy == y)) x--;
                else dir = randDir(dir);
                break;
            case 2:
                if (!wallInWay(dir, map) && !(Ix == x + 1 && Iy == y)) x++;
                else dir = randDir(dir);
                break;
            case 3:
                if (!wallInWay(dir, map) && !(Ix == x && Iy == y + 1)) y++;
                else dir = randDir(dir);
                break;
        }

        if (prevX - x == 2) x++;
        else if (prevX - x == -2) x--;
        else if (prevY - y == 2) y++;
        else if (prevY - y == -2) y--;
    }

    /**
     * Check if t here's a wall in pacman's way
     * @param dir the direction pacman is moving in
     * @param map the game map
     * @return true/false if there is a wall in the way
     */
    public boolean wallInWay(int dir, Tile[][] map) {
        if (dir == 0) {
            final int testX = x;
            final int testY = y - 1;
            if (map[testY][testX].wall) return true;
        } else if (dir == 1) {
            final int testX = x - 1;
            final int testY = y;
            if (map[testY][testX].wall) return true;
        } else if (dir == 2) {
            final int testX = x + 1;
            final int testY = y;
            if (map[testY][testX].wall) return true;
        } else if (dir == 3) {
            final int testX = x;
            final int testY = y + 1;
            if (map[testY][testX].wall) return true;
        }
        return false;
    }

    public void addFitness(int add) {
        fitness += add;
    }

    // Generates a random direction that isn't the last one
    private int randDir(int lastDir) {
        int dir = num.nextInt(4);
        while (dir == lastDir) {
            dir = num.nextInt(4);
        }
        return dir;
    }

    // Move pacman in random directions without hitting ghost
    public void moveBot(Tile[][] map, int Ix, int Iy) {

        // Check states
        if (powered) poweredMoves++;
        if (poweredMoves == maxPoweredMoves) powered = false;

        // Keep moving straight until a wall is hit
        move(map, Ix, Iy);
    }
}