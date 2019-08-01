package game;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;

public class VisualGame {

    private int generation;
    private int fitness;

    private int visionDistance = 1;

    int MAXMOVES;
    int[][] pCoords;
    int[] choices;
    int[] pDirs;
    int[] pFits;
    GraphicsContext gc;
    int moves = 0;

    int prevPR;
    int prevPC;

    Button showGame;

    /**
     * The game map (1: Walls, 0: Pellets, 8: Power Pellets, 6: Empty)
     */
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

    /**
     * Displays the game
     * @param gameGC javafx graphics context framework
     * @param is the infostorage file to use
     * @param generation which generation
     * @param showGame showgame button
     * @param FPS frames per second to render
     */
    public VisualGame(GraphicsContext gameGC, InfoStorage is, int generation, Button showGame, int FPS) {
        this.MAXMOVES = is.getMAXMOVES();
        this.pCoords = is.getPacCoords();
        this.choices = is.getChoices();
        this.pDirs = is.getpDirs();
        this.pFits = is.getpFits();
        this.generation = generation;
        this.showGame = showGame;
        this.gc = gameGC;

        try {
            while (moves < MAXMOVES && pCoords[moves][1] != 0) {
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

        gc.setFill(Color.BLACK);
        gc.clearRect(0, 0, 1000, 1000);
        showGame.setDisable(false);
    }

    public void stop() {
        moves = MAXMOVES;
    }

    /**
     * Draws the game map
     */
    private void drawMap() {

        // 0: Pellet / 1: Wall / 6: Empty / 8: Power Pellet

        int rectW = 20;

        int startX = -10;
        int startY = -10;

        gc.setStroke(Color.WHITE);

        // Draw the base map
        for (int r = 0; r < 31; r++) {
            for (int c = 0; c < 28; c++) {
                switch (tiles[r][c]) {
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
                }
            }
        }

        gc.setFill(Color.WHITE);
        gc.fillText("Pacman Fitness: " + pFits[moves], 400, 45);
        gc.fillText("Generation: " + generation, 25, 15);
    }

    /**
     * Every move, draws the game objects
     * @param moves keeps track of number of moves so far
     */
    private void drawGame(int moves) {

        int rectW = 20;

        int startX = -10;
        int startY = -10;

        // Get pacmans row and column coordinate (r, c)
        int pC = pCoords[moves][0];
        int pR = pCoords[moves][1];

        // Delete the trail behind the ghosts / pacman
        if (prevPR != 0) {
            gc.setFill(Color.BLACK);
            gc.clearRect(prevPC * rectW + startX, prevPR * rectW + startY, 22, 22);
        }

        gc.setFill(Color.YELLOW);
        gc.fillOval(pC * rectW + startX, pR * rectW + startY, 18, 18);


        // Show eaten pellets
        if (moves > 0) {
            // Draw empty spaces for pellets behind pacman
            if (!(pCoords[moves - 1][0] == pCoords[moves][0] && pCoords[moves - 1][1] == pCoords[moves][1])) {
                tiles[pCoords[moves][1]][pCoords[moves][0]] = 6;
            }
        }

        prevPC = pC;
        prevPR = pR;
    }

    /**
     * (Deprecated) draws arrows displaying the direction Pacman is attempting to move in
     * @param x arrow's x position
     * @param y arrow's y position
     * @param moves
     * @param length arrow length
     */
    public void drawArrows (int x, int y, int moves, int length) {
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
        // Set the direction arrow to be red while the others are grey (oriented for direction travelling)
        if (pDirs[moves] == 0 && choices[moves] == 0) gc.strokeLine(x, y, x + length, y);
        else if (pDirs[moves] == 0 && choices[moves] == 1) gc.strokeLine(x, y, x - length, y);
        else if (pDirs[moves] == 1 && choices[moves] == 0) gc.strokeLine(x, y, x, y - length);
        else if (pDirs[moves] == 1 && choices[moves] == 1) gc.strokeLine(x, y, x, y + length);
        else if (pDirs[moves] == 2 && choices[moves] == 0) gc.strokeLine(x, y, x, y - length);
        else if (pDirs[moves] == 2 && choices[moves] == 1) gc.strokeLine(x, y, x, y + length);
        else if (pDirs[moves] == 3 && choices[moves] == 0) gc.strokeLine(x, y, x - length, y);
        else if (pDirs[moves] == 3 && choices[moves] == 1) gc.strokeLine(x, y, x + length, y);
    }
}
