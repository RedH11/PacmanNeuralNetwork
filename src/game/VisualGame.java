package game;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class VisualGame {

    private int generation;
    private int fitness;

    private int visionDistance = 1;

    int MAXMOVES;
    int[][] pCoords;
    int[][] gCoords;
    int[] gDirs;
    int[] gFits;
    int[] pDirs;
    int[] pFits;
    boolean pPowered[];
    GraphicsContext gc;
    int moves = 0;
    int[][] g2Coords;
    int[] g2Fits;
    int [] g2Dirs;

    private boolean complete = false;

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
            {1, 1, 1, 1, 1, 1, 0, 1, 1, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 1, 1, 0, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 0, 1, 1, 6, 1, 1, 1, 1, 1, 1, 1, 1, 6, 1, 1, 0, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 0, 1, 1, 6, 1, 1, 1, 1, 1, 1, 1, 1, 6, 1, 1, 0, 1, 1, 1, 1, 1, 1},
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

    public VisualGame(GraphicsContext gameGC, InfoStorage is, int generation, int FPS) {
        this.MAXMOVES = is.getMAXMOVES();
        this.pCoords = is.getPacCoords();
        this.pDirs = is.getpDirs();
        this.pFits = is.getpFits();
        this.pPowered = is.getpPowered();

        this.gCoords = is.getgCoords();
        this.gDirs = is.getgDirs();
        this.gFits = is.getgFits();

        this.g2Coords = is.getG2Coords();
        this.g2Dirs = is.getG2Dirs();
        this.g2Fits = is.getG2Fits();
        this.generation = generation;

        this.gc = gameGC;

        try {
            while (moves < MAXMOVES && g2Coords[moves][1] != 0) {
                drawMap();
                drawGame(moves);
                moves++;
                try {
                    Thread.sleep(1000 / FPS);
                } catch (InterruptedException ex) {
                    System.out.println("Interruption oof");
                }
            }
        } catch (ArrayIndexOutOfBoundsException ex) {}
    }

    public void stop() {
        moves = MAXMOVES;
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
        gc.fillText("Inky 1 Fitness: " + gFits[moves], 400, 15);
        gc.fillText("Inky 2 Fitness: " + g2Fits[moves], 400, 30);
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

        int iC = gCoords[moves][0];
        int iR = gCoords[moves][1];

        int iC2 = g2Coords[moves][0];
        int iR2 = g2Coords[moves][1];

        int arrowLength = 15;

        // Draw pacman and inky and arrows showing their direction

        int pacX = pC * rectW + startX  + 9;
        int pacY = pR * rectW + startY + 9;

        drawArrows(pacX, pacY, moves, pDirs, arrowLength);

        if (pPowered[moves]) {
            gc.setFill(Color.LIGHTGOLDENRODYELLOW);
            gc.fillOval(pC * rectW + startX, pR * rectW + startY, 18, 18);
        } else {
            gc.setFill(Color.YELLOW);
            gc.fillOval(pC * rectW + startX, pR * rectW + startY, 18, 18);
        }

        // Make pacmans vision area rectangles with a white stroke
        for (int j = -1; j < visionDistance + 1; j++) {
            for (int i = -1; i < visionDistance + 1; i++) {
                if (pC * rectW + startX + i >= 0 && pR * rectW + startY + j >= 0) {
                    gc.setStroke(Color.rgb(0, 0, 0, 0.2));
                    gc.strokeRect(pC * rectW + startX + i, pR * rectW + startY + j, rectW, rectW);
                }
            }
        }

        // Show eaten pellets
        if (moves > 0) {
            // Draw empty spaces for pellets behind pacman
            if (!(pCoords[moves - 1][0] == pCoords[moves][0] && pCoords[moves - 1][1] == pCoords[moves][1])) {
                tiles[pCoords[moves][1]][pCoords[moves][0]] = 6;
            }
        }

        int inkyX = iC * rectW + startX  + 9;
        int inkyY = iR * rectW + startY + 9;

        int inkyX2 = iC2 * rectW + startX + 9;
        int inkyY2 = iR2 * rectW + startY + 9;

        drawArrows(inkyX, inkyY, moves, gDirs, arrowLength);
        drawArrows(inkyX2, inkyY2, moves, g2Dirs, arrowLength);

        // Draw Inky
        if (pPowered[moves]) {
            gc.setFill(Color.BLUEVIOLET);
            gc.fillOval(iC * rectW + startX, iR * rectW + startY, 18, 18);
            gc.fillOval(iC2 * rectW + startX, iR2 *rectW + startY, 18, 18);
        } else {
            gc.setFill(Color.LIGHTBLUE);
            gc.fillOval(iC * rectW + startX, iR * rectW + startY, 18, 18);
            gc.fillOval(iC2 * rectW + startX, iR2 *rectW + startY, 18, 18);
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
