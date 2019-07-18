package game;

public class MapLayout {

    Grid[][] mapLayout = new Grid[33][33];

    Grid emptyGrid = new Grid();
    Grid hWallGrid = new Grid();
    Grid vWallGrid = new Grid();
    Grid pelletGrid = new Grid();
    Grid powerPelletGrid = new Grid();
    Grid pacmanGrid = new Grid();
    Grid inkyGrid = new Grid();

    public MapLayout() {
        createMap();
        
        hWallGrid.sethWall(true);
        vWallGrid.setvWall(true);
        pelletGrid.setPellet(true);
        powerPelletGrid.setPowerPellet(true);
        pacmanGrid.setPacman(true);
        inkyGrid.setInky(true);
        //outputMap();
    }

    // MAKE NEW THINGS EVERY TIME

    public Grid[][] getLayout() {
        return mapLayout;
    }

    private void createMap() {

        // Make all the spots pellets besides the area around the ghost box
        for (int r = 0; r < mapLayout.length; r++) {
            for (int c = 0; c < mapLayout.length; c++) {
                mapLayout[r][c] = new Grid();
                mapLayout[r][c] = pelletGrid;
            }
        }

        // Center of the grid
        int center = (int) Math.floor(mapLayout.length/2);

        // Pacman Spawn
        mapLayout[20][center] = pacmanGrid;

        // Spawn ghost in the center
        mapLayout[12][center] = inkyGrid;

        // Outside Walls
        makeBox(center, center, 32, 32, false);

        // Ghost Box
        makeBox(center, center, 11, 7, true);

        // Clear pellets around the ghost box (excluding the ghost and pacman)
        for (int c = 10; c < 23; c++) {
            if (c != 16) {
                mapLayout[12][c] = emptyGrid;
                mapLayout[20][c] = emptyGrid;
            }
        }

        for (int r = 12; r < 21; r++) {
            mapLayout[r][10] = emptyGrid;
            mapLayout[r][22] = emptyGrid;
        }

        // Top Middle Barrier
        mapLayout[0][15] = vWallGrid;
        mapLayout[0][17] = vWallGrid;
        mapLayout[1][16] = vWallGrid;
        mapLayout[2][16] = vWallGrid;
        mapLayout[3][16] = vWallGrid;
        mapLayout[4][16] = vWallGrid;

        // Plus signs (3 total) without top
        for (int r = 8; r < 12; r++) {
            mapLayout[r][center] = vWallGrid;
            mapLayout[r + 13][center] = vWallGrid;
            mapLayout[r + 19][center] = vWallGrid;
        }
        for (int c = 11; c < 22; c++) {
            mapLayout[6][c] = hWallGrid;
            mapLayout[7][c] = hWallGrid;

            mapLayout[center + 5][c] = hWallGrid;
            mapLayout[center + 6][c] = hWallGrid;
            mapLayout[center + 7][c] = hWallGrid;

            mapLayout[center + 11][c] = hWallGrid;
            mapLayout[center + 12][c] = hWallGrid;

            //mapLayout[center + 2][c]. = hWallGrid();
        }
        mapLayout[7 + 19][center] = vWallGrid;

        mapLayout[center + 6][11] = vWallGrid;
        mapLayout[center + 6][21] = vWallGrid;


        // Top middle right/left box
        makeBox(21, 3, 7, 3, true);
        makeBox(11, 3, 7, 3, true);

        // Plus sign without right/left arm under top middle right box
        for (int r = 6; r < 16; r++) {
            mapLayout[r][24] = vWallGrid;
            mapLayout[r][23] = vWallGrid;
            mapLayout[r][8] = vWallGrid;
            mapLayout[r][9] = vWallGrid;
        }
        for (int c = 18; c < 24; c++) {
            mapLayout[9][c] = hWallGrid;
            mapLayout[10][c] = hWallGrid;
            mapLayout[11][c] = hWallGrid;
            mapLayout[9][c - 9] = hWallGrid;
            mapLayout[10][c - 9] = hWallGrid;
            mapLayout[11][c - 9] = hWallGrid;
        }

        mapLayout[10][18] = hWallGrid;
        mapLayout[10][14] = hWallGrid;

        // Top far right/left box
        makeBox(28, 3, 5, 3, true);
        makeBox(4, 3, 5, 3, true);

        // 5x2 box under top far right/left box
        mapLayout[6][26] = hWallGrid;
        mapLayout[6][27] = hWallGrid;
        mapLayout[6][28] = hWallGrid;
        mapLayout[6][29] = hWallGrid;
        mapLayout[6][30] = hWallGrid;
        mapLayout[7][26] = hWallGrid;
        mapLayout[7][27] = hWallGrid;
        mapLayout[7][28] = hWallGrid;
        mapLayout[7][29] = hWallGrid;
        mapLayout[7][30] = hWallGrid;

        mapLayout[6][2] = hWallGrid;
        mapLayout[6][3] = hWallGrid;
        mapLayout[6][4] = hWallGrid;
        mapLayout[6][5] = hWallGrid;
        mapLayout[6][6] = hWallGrid;
        mapLayout[7][2] = hWallGrid;
        mapLayout[7][3] = hWallGrid;
        mapLayout[7][4] = hWallGrid;
        mapLayout[7][5] = hWallGrid;
        mapLayout[7][6] = hWallGrid;

        // Right/Left wall barriers
        makeBox(center + 13, center - 4, 7, 7, true);
        makeBox(center + 13, center + 4, 7, 7, true);

        makeBox(center - 13, center - 4, 7, 7, true);
        makeBox(center - 13, center + 4, 7, 7, true);


        // Make right/left tunnel AND Line at bottom right/left middle (transformed 7 to to the left)
        for (int c = 25; c < 32; c++) {
            mapLayout[16][c] = emptyGrid;
            mapLayout[16][c - 25] = emptyGrid;
            mapLayout[25][c - 7] = hWallGrid;
            mapLayout[25][c - 17] = hWallGrid;
        }

        // Right tunnel tweaks
        mapLayout[9][32] = hWallGrid;
        mapLayout[15][32] = hWallGrid;
        mapLayout[17][32] = hWallGrid;
        mapLayout[16][32] = emptyGrid;
        mapLayout[23][32] = hWallGrid;

        // Left tunnel tweaks
        mapLayout[9][0] = hWallGrid;
        mapLayout[15][0] = hWallGrid;
        mapLayout[17][0] = hWallGrid;
        mapLayout[16][0] = emptyGrid;
        mapLayout[23][0] = hWallGrid;

        // Clearing outside of the right/left wall barrier
        for (int r = 10; r < center - 1; r++) {
            mapLayout[r][32] = emptyGrid;
            mapLayout[r + 8][32] = emptyGrid;
            mapLayout[r][0] = emptyGrid;
            mapLayout[r + 8][0] = emptyGrid;
        }

        // Bottom right/left spike barrier
        for (int c = 29; c <= 31; c++) {
            mapLayout[center + 11][c] = hWallGrid;
            mapLayout[center + 12][c] = hWallGrid;
            mapLayout[center + 11][c - 28] = hWallGrid;
            mapLayout[center + 12][c - 28] = hWallGrid;
        }
        mapLayout[center + 11][29] = hWallGrid;
        mapLayout[center + 12][29] = hWallGrid;
        mapLayout[center + 11][3] = hWallGrid;
        mapLayout[center + 12][3] = hWallGrid;

        // Vertical middle right/left barrier
        for (int r = 17; r < 24; r++) {
            mapLayout[r][23] = vWallGrid;
            mapLayout[r][24] = vWallGrid;
            mapLayout[r][8] = vWallGrid;
            mapLayout[r][9] = vWallGrid;
        }

        // Sideways L in bottom right/left
        for (int r = 25; r < 29; r++) {
            mapLayout[r][26] = vWallGrid;
            mapLayout[r][27] = vWallGrid;
            mapLayout[r][5] = vWallGrid;
            mapLayout[r][6] = vWallGrid;
        }
        for (int c = 26; c < 31; c++) {
            mapLayout[25][c] = hWallGrid;
            mapLayout[25][c - 24] = hWallGrid;
        }

        // Bottom right/left upside town T
        for (int c = 18; c < 31; c++) {
            mapLayout[30][c] = hWallGrid;
            mapLayout[30][c - 16] = hWallGrid;
        }
        for (int r = 27; r < 30; r++) {
            mapLayout[r][23] = vWallGrid;
            mapLayout[r][24] = vWallGrid;
            mapLayout[r][8] = vWallGrid;
            mapLayout[r][9] = vWallGrid;
        }

        // Fill in top boxes
        for (int c = 3; c < 30; c++) {
            if (mapLayout[3][c].isEmpty()) mapLayout[3][c] = hWallGrid;
        }

        // Fix missing pellet at entrance of right tunnel
        mapLayout[16][25] = pelletGrid;

        // Power pellets
        mapLayout[1][1] = powerPelletGrid;
        mapLayout[1][31] = powerPelletGrid;
        mapLayout[31][1] = powerPelletGrid;
        mapLayout[31][31] = powerPelletGrid;

        // MAP FIXES
        // All the rows below the ghost are set to pacman?

        outputConsoleMap();
        System.out.println("Map Made");
    }

    public void makeBox(int centerX, int centerY, int w, int h, boolean emptyInside) {

        int halfW = (int) Math.floor(w / 2); // 2
        int halfH = (int) Math.floor(h / 2); // 1

        int rowCounter = 1;

        // Makes box empty
        if (emptyInside) {
            for (int c = centerX - halfW + 1; c < centerX + halfW; c++) {
                mapLayout[centerY - halfH + 1][c] = emptyGrid;
                if (h > 3) {
                    mapLayout[centerY - halfH + 2][c] = emptyGrid;
                    mapLayout[centerY - halfH + 3][c] = emptyGrid;
                }

                if (h > 5) {
                    mapLayout[centerY - halfH + 4][c] = emptyGrid;
                    mapLayout[centerY - halfH + 5][c] = emptyGrid;
                }

            }
        }

        // Top/Bottom
        for (int c = centerX - halfW; c < centerX + halfW; c++) {
            mapLayout[centerY - halfH][c] = hWallGrid;
            mapLayout[centerY + halfH][c] = hWallGrid;
        }

        // Left/Right
        for (int r = centerY - halfH; r < centerY + halfH; r++) {
            mapLayout[r][centerX - halfW] = vWallGrid;
            mapLayout[r][centerX + halfW] = vWallGrid;
        }
    }


    public void outputConsoleMap() {
        for (int r = 0; r < mapLayout.length; r++) {
            for (int c = 0; c < mapLayout.length; c++) {
                if (mapLayout[r][c] == vWallGrid) System.out.print("| ");
                    // Doubled to make a square because the vertical is long
                else if (mapLayout[r][c] == hWallGrid) System.out.print("- ");
                else if (mapLayout[r][c] == pacmanGrid) System.out.print("C ");
                else if (mapLayout[r][c] == emptyGrid) System.out.print("  ");
                else if (mapLayout[r][c] == pelletGrid) System.out.print(". ");
                else if (mapLayout[r][c] == powerPelletGrid) System.out.print("* ");
                    // Is ghost
                else System.out.println("8 ");
            }
            System.out.println();
        }
    }
}
