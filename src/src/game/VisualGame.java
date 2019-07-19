package game;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Transform;
import sun.font.GraphicComponent;

public class VisualGame {

    private int generation;
    private int fitness;

    int MAXMOVES;
    int[][] pCoords;
    int[][] gCoords;
    int[] gDirs;
    int[] gFits;
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

    public VisualGame(int MAXMOVES, int[][] pCoords, int[][] gCoords, int[] gDirs, int[] gFits, GraphicsContext gc, int generation, int FPS) {
        this.MAXMOVES = MAXMOVES;
        this.pCoords = pCoords;
        this.gCoords = gCoords;
        this.gDirs = gDirs;
        this.gFits = gFits;
        this.gc = gc;

        drawMap();

        while (moves < MAXMOVES) {
            drawMap();
            drawGame(moves);
            moves++;
            try {
                Thread.sleep(1000/FPS);
            } catch (InterruptedException ex) {}
        }
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
        gc.fillText("Fitness: " + gFits[moves], 450, 15);
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

        // Draw pacman and inky
        gc.setFill(Color.YELLOW);
        gc.fillOval(pC * rectW + startX, pR * rectW + startY, 18, 18);

        // Show eaten pellets
        if (moves > 0) {
            // Draw empty spaces for pellets behind pacman
            if (!(pCoords[moves - 1][0] == pCoords[moves][0] && pCoords[moves - 1][1] == pCoords[moves][1])) {
                tiles[pCoords[moves][1]][pCoords[moves][0]] = 6;
            }
        }

        int inkyX = iC * rectW + startX  + 9;
        int inkyY = iR * rectW + startY + 9;

        int lineLength = 15;

        gc.setStroke(Color.GRAY);
        // Up arrow
        gc.strokeLine(inkyX, inkyY, inkyX, inkyY - lineLength);
        // Left arrow
        gc.strokeLine(inkyX, inkyY, inkyX - lineLength, inkyY);
        // Right arrow
        gc.strokeLine(inkyX, inkyY, inkyX + lineLength, inkyY);
        // Down arrow
        gc.strokeLine(inkyX, inkyY, inkyX, inkyY + lineLength);

        gc.setStroke(Color.RED);
        // Set the direction arrow to be red while the others are grey
        if (gDirs[moves] == 0) gc.strokeLine(inkyX, inkyY, inkyX, inkyY - lineLength);
        else if (gDirs[moves] == 1) gc.strokeLine(inkyX, inkyY, inkyX - lineLength, inkyY);
        else if (gDirs[moves] == 2) gc.strokeLine(inkyX, inkyY, inkyX + lineLength, inkyY);
        else if (gDirs[moves] == 3) gc.strokeLine(inkyX, inkyY, inkyX, inkyY + lineLength);

        // Draw Inky
        gc.setFill(Color.LIGHTBLUE);
        gc.fillOval(iC * rectW + startX, iR * rectW + startY, 18, 18);
    }
}
