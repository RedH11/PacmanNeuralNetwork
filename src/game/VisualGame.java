package game;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class VisualGame {

    private int generation;
    private int fitness;

    int MAXMOVES;
    int[][] pCoords;
    int[] pDirs;
    int[] pFits;
    boolean pPowered[];
    GraphicsContext gc;
    int moves = 0;

    private int[][] tiles = {
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1},
            {1, 8, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 8, 1},
            {1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 0, 1},
            {1, 0, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 1},
            {1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 6, 1, 1, 6, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 6, 1, 1, 6, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 0, 1, 1, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 1, 1, 0, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 0, 1, 1, 6, 1, 1, 1, 1, 1, 1, 1, 1, 6, 1, 1, 0, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 0, 1, 1, 6, 1, 1, 1, 1, 1, 1, 1, 1, 6, 1, 1, 0, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 0, 6, 6, 6, 1, 1, 1, 1, 1, 1, 1, 1, 6, 6, 6, 0, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 0, 1, 1, 6, 1, 1, 1, 1, 1, 1, 1, 1, 6, 1, 1, 0, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 0, 1, 1, 6, 1, 1, 1, 1, 1, 1, 1, 1, 6, 1, 1, 0, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 0, 1, 1, 0, 0, 0, 6, 6, 6, 6, 0, 0, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1},
            {1, 8, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 8, 1},
            {1, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 1},
            {1, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 1},
            {1, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 1},
            {1, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1},
            {1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}};

    public VisualGame(int MAXMOVES, int[][] pCoords,  int[] pDirs, int[] pFits, boolean pPowered[], GraphicsContext gc, int generation, int FPS) {
        this.MAXMOVES = MAXMOVES;
        this.pCoords = pCoords;
        this.pDirs = pDirs;
        this.pFits = pFits;
        this.pPowered = pPowered;
        this.gc = gc;
        this.generation = generation;

        drawMap();

        try {
            while (moves < MAXMOVES ) {
                drawMap();
                drawGame(moves);
                moves++;
                try {
                    Thread.sleep(1000 / FPS);
                } catch (InterruptedException ex) {
                }
            }
        } catch (ArrayIndexOutOfBoundsException ex) {}
    }

    private void drawMap() {

        // 0: Pellet / 1: Wall / 6: Empty / 8: Power Pellet

        int rectW = 20;

        int startX = -10;
        int startY = -10;

        gc.setStroke(Color.WHITE);

        gc.setFill(Color.BLACK);
        // Background color black
        gc.fillRect(0, 0, 640, 640);

        // Draw the base map
        for (int r = 0; r < 31; r++) {
            for (int c = 0; c < 28; c++) {
                switch (tiles[r][c]) {
                    case 1: // Wall
                        gc.setFill(Color.rgb(0, 0, 156));
                        gc.fillRect(c * rectW + startX, r * rectW + startY, rectW, rectW);
                        break;
                    case 0: // Power Pellet
                        gc.setFill(Color.YELLOW);
                        gc.fillOval(c * rectW + startX + 7, r * rectW + startY + 7, 5, 5);
                        break;
                    case 8: // Pellet
                        gc.setFill(Color.YELLOW);
                        // Correct for difference in layout of upper and lower power pellets
                        if (r > 16) {
                            gc.fillOval(c * rectW + startX + 3, r * rectW + startY + 1, 10, 10);
                            gc.strokeOval(c * rectW + startX + 3, r * rectW + startY + 1, 10, 10);
                        } else {
                            gc.fillOval(c * rectW + startX + 4, r * rectW + startY + 4, 10, 10);
                            gc.strokeOval(c * rectW + startX + 4, r * rectW + startY + 4, 10, 10);
                        }
                        break;
                    case 6: // Empty
                        gc.setFill(Color.BLACK);
                        gc.fillRect(c * rectW + startX, r * rectW + startY, rectW, rectW);
                        break;
                    default: // Other
                        gc.setFill(Color.WHITE);
                        gc.fillRect(c * rectW + startX, r * rectW + startY, rectW, rectW);
                }
            }
        }

        gc.setFill(Color.WHITE);
        gc.fillText("Pacman Fitness: " + pFits[moves], 400, 45);
        gc.fillText("Generation: " + generation, 25, 15);
    }

    private void drawGame(int moves) {

        int rectW = 20;

        int startX = -10;
        int startY = -10;

        // Get pacmans row and column coordinate (r, c)
        int pC = pCoords[moves][0];
        int pR = pCoords[moves][1];

        int arrowLength = 15;

        // Draw pacman and inky and arrows showing their direction
        int pacX = pC * rectW + startX  + 9;
        int pacY = pR * rectW + startY + 9;

        drawArrows(pacX, pacY, moves, pDirs, arrowLength);

        drawArrows(pacX, pacY, moves, pDirs, arrowLength);

        Image pacLeft = new Image("assets/sprites/pacman_left.png");
        Image pacRight = new Image("assets/sprites/pacman_right.png");
        Image pacUp = new Image("assets/sprites/pacman_up.png");
        Image pacDown = new Image("assets/sprites/pacman_down.png");
        Image poweredPacLeft = new Image("assets/sprites/powered_pacman_left.png");
        Image poweredPacRight = new Image("assets/sprites/powered_pacman_right.png");
        Image pacmanImage = pacLeft;

        Image inkyLeft = new Image("assets/sprites/inky_left.png");
        Image inkyRight = new Image("assets/sprites/inky_Right.png");
        Image scaredGhost = new Image("assets/sprites/scared_ghost.png");
        Image inkyImage = inkyLeft;

        //Set Pacman's sprite depending on movement direction / powered state
        if (pDirs[moves] == 0) {
            if (pPowered[moves]) {

            } else {
                pacmanImage = pacUp;
            }
        }
        if (pDirs[moves] == 1) {
            if (pPowered[moves]) {
                pacmanImage = poweredPacLeft;
            } else {
                pacmanImage = pacLeft;
            }
        }
        if (pDirs[moves] == 2) {
            if (pPowered[moves]) {
                pacmanImage = poweredPacRight;
            } else {
                pacmanImage = pacRight;
            }
        }
        if (pDirs[moves] == 3) {
            if (pPowered[moves]) {

            } else {
                pacmanImage = pacDown;
            }
        }

        //Set Inky's sprite depending on movement direction / scared state
        /*if (gDirs[moves] == 1) {
            if (pPowered[moves]) {
                inkyImage = scaredGhost;
            } else {
                inkyImage = inkyLeft;
            }
        }
        if (gDirs[moves] == 2) {
            if (pPowered[moves]) {
                inkyImage = scaredGhost;
            } else {
                inkyImage = inkyRight;
            }
        }*/
        //Draw Pacman
        gc.drawImage(pacmanImage, pC * rectW + startX, pR * rectW + startY);
        //Draw Inkies
        /*gc.drawImage(inkyImage, iC * rectW + startX, iR * rectW + startY, 20, 20);
        gc.drawImage(inkyImage, iC2 * rectW + startX, iR2 * rectW + startY, 20, 20);*/

        // Show eaten pellets
        if (moves > 0) {
            // Draw empty spaces for pellets behind pacman
            if (!(pCoords[moves - 1][0] == pCoords[moves][0] && pCoords[moves - 1][1] == pCoords[moves][1])) {
                tiles[pCoords[moves][1]][pCoords[moves][0]] = 6;
            }
        }
    }

    public void drawArrows (int x, int y, int moves, int[] dirs, int length) {
        gc.setStroke(Color.GRAY);
        // Up arrow
        gc.strokeLine(x, y, x, y - length);
        // Left arrow
        gc.strokeLine(x, y, x - length, y);
        // Right arrow
        gc.strokeLine(x, y, x + length, y);
        // Down arrow
        gc.strokeLine(x, y, x, y + length);

        gc.setStroke(Color.RED);
        // Set the direction arrow to be red while the others are grey
        if (dirs[moves] == 0) gc.strokeLine(x, y, x, y - length);
        else if (dirs[moves] == 1) gc.strokeLine(x, y, x - length, y);
        else if (dirs[moves] == 2) gc.strokeLine(x, y, x + length, y);
        else if (dirs[moves] == 3) gc.strokeLine(x, y, x, y + length);
    }
}
