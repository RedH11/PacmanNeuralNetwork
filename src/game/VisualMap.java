package game;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class VisualMap {

    private int generation;
    private int fitness;
    private GraphicsContext gc;

    private Grid[][] gameMap;

    public VisualMap(Grid[][] gameMap, GraphicsContext gc, int generation, int fitness) {
        this.gc = gc;
        this.generation = generation;
        this.fitness = fitness;

        drawGrid();
        drawWalls();
    }

    public void drawGrid() {

        int rectW = 20;
        int gridW = 30;

        int startX = 20;
        int startY = 20;

        gc.fillRect(0, 0, 640, 640);

        // Drawing the grid
        for (int r = 0; r < gridW; r++) {
            for (int c = 0; c < gridW; c++) {
                gc.setFill(Color.BLACK);
                gc.setStroke(Color.WHITE);
                gc.strokeRect(c * rectW + startX, r * rectW + startY, rectW, rectW);
                gc.fillRect(c * rectW + startX, r * rectW + startY, rectW, rectW);
            }
        }
    }

    public void drawWalls() {

        gc.setFill(Color.DARKBLUE);

        // Top Border
        gc.fillRect(0, 4, 640, 2);
        gc.fillRect(0, 14, 640, 2);
    }
}
