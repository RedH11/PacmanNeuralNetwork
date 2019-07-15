package game;

public class GameArray {

    // Make a 32x32 2D grid as the game
    Grid[][] gameMap = new Grid[32][32];

    public GameArray() {
        constructArray();
    }

    private void constructArray() {
        for (int r = 0; r < gameMap.length; r++) {
            for (int c = 0; c < gameMap.length; c++) {
                gameMap[r][c] = new Grid();
                gameMap[r][c].setEmpty(true);
            }
        }

        gameMap[16][16].setPacman(true);

        for (int c = 1; c < gameMap.length - 1; c++) {
            gameMap[0][c].sethWall(true);
            gameMap[gameMap.length - 1][c].sethWall(true);
        }

        gameMap[0][gameMap.length - 1].settRWall(true);
        gameMap[0][0].settLWall(true);

        for (int r = 1; r < gameMap.length - 1; r++) {
            gameMap[r][0].sethWall(true);
        }

        gameMap[gameMap.length - 1][0].setbLWall(true);
        gameMap[gameMap.length - 1][gameMap.length - 1].setbRWall(true);


    }

    public void outputMap() {
        for (int r = 0; r < gameMap.length; r++) {
            for (int c = 0; c < gameMap.length; c++) {
                if (gameMap[r][c].isvWall()) System.out.print("|");
                else if (gameMap[r][c].ishWall()) System.out.print("-");
                else if (gameMap[r][c].isPacman()) System.out.print("C");
                else if (gameMap[r][c].isbLWall()) System.out.print("\\");
                else if (gameMap[r][c].isbRWall()) System.out.print("/");
                else if (gameMap[r][c].istLWall()) System.out.print("/");
                else if (gameMap[r][c].istRWall()) System.out.print("\\");
                else if (gameMap[r][c].isEmpty()) System.out.print(" ");
                else if (gameMap[r][c].isPellet()) System.out.print(".");
                else if (gameMap[r][c].isPowerPellet()) System.out.print("*");
                // Is ghost
                else System.out.println("8");
            }
            System.out.println();
        }
    }
}
