package game;

public class GameArray {

    // Make a 32x32 2D grid as the game
    Grid[][] testMap;

    public GameArray(Grid[][] mapLayout) {
        testMap = mapLayout;
    }

    public void updateGrid(int dir) {

        if (dir == 0) {
            // MOVE UP
        } else if (dir == 1) {
            // MOVE LEFT
        } else if (dir == 2) {
            // MOVE RIGHT
        } else if (dir == 3) {
            // MOVE DOWN
        }
    }
}
