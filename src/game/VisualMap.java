package game;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class VisualMap {

    private int generation;
    private int fitness;
    private GraphicsContext gc;

    private Grid[][] gameMap;

    public VisualMap(GameArray game, GraphicsContext gc, int generation, int fitness) {

        gameMap = game.getGameMapLayout();
        this.gc = gc;
        this.generation = generation;
        this.fitness = fitness;

        drawGrid();
        //updateGrid();
    }

    public void drawGrid() {

        int rectW = 20;
        int gridW = 33;

        int startX = -10;
        int startY = -10;

        //int wallColor = 192;
        int wallColor = 100;

        gc.setStroke(Color.WHITE);

        gc.setFill(Color.BLACK);
        // Background color black
        gc.fillRect(0, 0, 640, 640);


        // Drawing the grid
        for (int r = 0; r < gridW; r++) {
            for (int c = 0; c < gridW; c++) {
                if (gameMap[r][c].isPinky()) {
                    gc.setFill(Color.PINK);
                    gc.fillOval(c * rectW + startX, r * rectW + startY, 18, 18);
                } else if (gameMap[r][c].isBlinky()) {
                    gc.setFill(Color.RED);
                    gc.fillOval(c * rectW + startX, r * rectW + startY, 18, 18);
                } else if (gameMap[r][c].isInky()) {
                    gc.setFill(Color.BLUE);
                    gc.fillOval(c * rectW + startX, r * rectW + startY, 18, 18);
                } else if (gameMap[r][c].isClyde()) {
                    gc.setFill(Color.ORANGE);
                    gc.fillOval(c * rectW + startX, r * rectW + startY, 18, 18);
                } else if (gameMap[r][c].isPellet()) {
                    gc.setFill(Color.YELLOW);
                    gc.fillOval(c * rectW + startX + 7, r * rectW + startY + 7, 5, 5);
                } else if (gameMap[r][c].isWall()) {
                    gc.setFill(Color.rgb(0, 0, 156));
                    gc.fillRect(c * rectW + startX, r * rectW + startY, rectW, rectW);
                } else if (gameMap[r][c].isPacman()) {
                    gc.setFill(Color.YELLOW);
                    gc.fillOval(c * rectW + startX, r * rectW + startY, 18, 18);
                } else if (gameMap[r][c].isPowerPellet()) {
                    gc.setFill(Color.YELLOW);
                    // Correct for difference in layout of upper and lower power pellets
                    if (r > 16) {
                        gc.fillOval(c * rectW + startX + 3, r * rectW + startY + 1, 10, 10);
                        gc.strokeOval(c * rectW + startX + 3, r * rectW + startY + 1, 10, 10);
                    } else {
                        gc.fillOval(c * rectW + startX + 4, r * rectW + startY + 4, 10, 10);
                        gc.strokeOval(c * rectW + startX + 4, r * rectW + startY + 4, 10, 10);
                    }
                }
            }
        }
    }

    public void updateGrid() {
        gc.setFill(Color.WHITE);
        gc.fillText("Fitness: " + fitness, 550, 15);
        gc.fillText("Generation: " + fitness, 25, 15);
    }
}
