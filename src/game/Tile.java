package game;

class Tile {
    // States of the tile
    boolean wall = false;
    boolean dot = false;
    boolean bigDot = false;
    boolean eaten = false;

    int wallX = 0;
    int wallY = 0;

    // [y, x] array for coordinates
    int[][] coordinates = new int[1][2];

    //-------------------------------------------------------------------------------------------------------------------------------------------
    // Constructor
    Tile(int x, int y) {
        coordinates[0][0] = x;
        coordinates[0][1] = y;
    }
}
