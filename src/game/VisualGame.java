package game;

import game.NEAT.ConnectionGene;
import game.NEAT.NodeGene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class VisualGame {

    private int generation ;
    private int fitness;

    private Map<Integer, ConnectionGene> connections ;
    private Map<Integer, NodeGene> nodes;

    int MAXMOVES;
    int[][] pCoords;
    int[][] g1Coords;
    int[][] g2Coords;
    int[] pFits;
    boolean pPowered[];
    GraphicsContext gc;
    int[] layerSizes = new int[3];
    int moves = 0;

    int prevPC = 0;
    int prevPR = 0;
    int prevG1C = 0;
    int prevG1R = 0;
    int prevG2C = 0;
    int prevG2R = 0;

    HashMap<Integer, Point> nodeGenePositions = new HashMap<Integer, Point>();
    int circleD = 20;
    
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

    public VisualGame(GraphicsContext gameGC, GhostInfoStorage is, int generation, int FPS) {
        this.MAXMOVES = is.getMAXMOVES();
        this.pCoords = is.getPacCoords();
        this.pPowered = is.getpPowered();
        this.generation = generation;
        this.g1Coords = is.getG1Coords();
        this.g2Coords = is.getG2Coords();
        this.gc = gameGC;
        this.layerSizes = is.getLayerSizes();

        nodes = is.getNodes();
        connections = is.getConnections();

        gc.setFill(Color.BLACK);
        gc.fillText("Best Ghost Neural Network", 550 + 120, 20);
        drawNodes();
        drawConnections();

        try {
            if (pCoords[moves][1] == 0) System.out.println("Coordinate Error");

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
        } catch (ArrayIndexOutOfBoundsException ex) {
            System.out.println("Error Drawing Map" + ex);
        }

        gc.setFill(Color.BLACK);
        gc.clearRect(0, 0, 1000, 1000);
        gc.setFill(Color.WHITE);
        gc.fillText("GAME OVER", 235, 290);
    }

    private void drawMap() {

        // 0: Pellet / 1: Wall / 6: Empty / 8: Power Pellet

        int rectW = 20;

        int startX = 0;
        int startY = 0;

        gc.setStroke(Color.WHITE);

        // Draw the pellets on the map
        for (int r = 0; r < 31; r++) {
            for (int c = 0; c < 28; c++) {
                switch (tiles[r][c]) {
                    case 0: // Power Pellet
                        gc.setFill(Color.YELLOW);
                        gc.fillOval(c * rectW + startX + 2.5, r * rectW + startY, 5, 5);
                        break;
                    case 8: // Pellet
                        gc.setFill(Color.YELLOW);
                        // Correct for difference in layout of upper and lower power pellets
                        if (r > 16) {
                            gc.fillOval(c * rectW + startX, r * rectW + startY, 10, 10);
                            gc.strokeOval(c * rectW + startX, r * rectW + startY, 10, 10);
                        } else {
                            gc.fillOval(c * rectW + startX , r * rectW + startY, 10, 10);
                            gc.strokeOval(c * rectW + startX, r * rectW + startY, 10, 10);
                        }
                        break;
                }
            }
        }

        gc.setFill(Color.WHITE);
        //gc.fillText("Pacman Fitness: " + pFits[moves], 400, 45);
        //gc.fillText("Generation: " + generation, 25, 15);
    }

    private void drawGame(int moves) {

        int rectW = 20;

        double startX = -4;
        double startY = -5;
        
        int circleW = 17;

        // Get pacmans row and column coordinate (r, c)
        int pC = pCoords[moves][0];
        int pR = pCoords[moves][1];

        int g1C = g1Coords[moves][0];
        int g1R = g1Coords[moves][1];

        int g2C = g2Coords[moves][0];
        int g2R = g2Coords[moves][1];

        // Delete the trail behind the ghosts / pacman
        if (prevPR != 0) {
            gc.setFill(Color.BLACK);
            gc.fillRect(prevPC * rectW + startX, prevPR * rectW + startY, 20, 20);
            gc.fillRect(prevG1C * rectW + startX, prevG1R * rectW + startY, 20, 20);
            gc.fillRect(prevG2C * rectW + startX, prevG2R * rectW + startY, 20, 20);
        }

        // Draw Pacman
        if (pPowered[moves]) {
            gc.setFill(Color.LIGHTGOLDENRODYELLOW);
            gc.fillOval(pC * rectW + startX, pR * rectW + startY, circleW, circleW);
        } else {
            gc.setFill(Color.YELLOW);
            gc.fillOval(pC * rectW + startX, pR * rectW + startY, circleW, circleW);
        }

        // Draw Ghosts
        gc.setFill(Color.LIGHTBLUE);
        gc.fillOval(g1C * rectW + startX, g1R * rectW + startY, circleW, circleW);

        gc.setFill(Color.RED);
        gc.fillOval(g2C * rectW + startX, g2R * rectW + startY, circleW, circleW);

        // Show eaten pellets
        if (moves > 0) {
            // Draw empty spaces for pellets behind pacman
            //if (!(pCoords[moves - 1][0] == pCoords[moves][0] && pCoords[moves - 1][1] == pCoords[moves][1])) {
                tiles[pCoords[moves][1]][pCoords[moves][0]] = 6;
            //}
        }

        prevPC = pC;
        prevPR = pR;
        prevG1C = g1C;
        prevG1R = g1R;
        prevG2C = g2C;
        prevG2R = g2R;
    }

    private void drawNodes() {
        int INPUTS = layerSizes[0];
        int HIDDEN = layerSizes[1];
        int OUTPUT = layerSizes[2];
        
        int inputXCoord = 550 + 20;
        // Canvas height - 20 / Inputs = Even spacing with 10 room above and below
        int inputYSpacing = 600 / INPUTS;
        int inputYCoord = inputYSpacing / 2;

        int hiddenXCoord = 0;
        int hiddenYSpacing = 0;
        int hiddenYCoord = 0;

        if (HIDDEN > 0) {
            hiddenXCoord = 550 + 180;
            hiddenYSpacing = 600 / HIDDEN;
            hiddenYCoord = hiddenYSpacing / 2;
        }

        int outputXCoord = 550 + 360;
        int outputYSpacing = 640 / OUTPUT;
        int outputYCoord = outputYSpacing / 2;

        for (NodeGene node : nodes.values()) {

            if (node.getType() == NodeGene.TYPE.INPUT) {
                gc.setFill(Color.rgb(153, 204, 255));
                gc.fillOval(inputXCoord, inputYCoord, circleD, circleD);
                nodeGenePositions.put(node.getId(), new Point(inputXCoord, inputYCoord));
                inputYCoord += inputYSpacing;
            } else if (node.getType() == NodeGene.TYPE.HIDDEN) {
                gc.setFill(Color.rgb(170, 255, 158));
                gc.fillOval(hiddenXCoord, hiddenYCoord, circleD, circleD);
                nodeGenePositions.put(node.getId(), new Point(hiddenXCoord, hiddenYCoord));
                hiddenYCoord += hiddenYSpacing;
            } else {
                gc.setFill(Color.rgb(255, 153, 153));
                gc.fillOval(outputXCoord, outputYCoord, circleD, circleD);
                nodeGenePositions.put(node.getId(), new Point(outputXCoord, outputYCoord));
                outputYCoord += outputYSpacing;
            }
        }
    }

    private void drawConnections() {

        gc.setStroke(Color.BLACK);

        for (ConnectionGene gene : connections.values()) {
            if (!gene.isExpressed()) {
                continue;
            }

            // Width of line is based off of weight
            gc.setLineWidth(gene.getWeight());

            Point inNode = nodeGenePositions.get(gene.getInNode());
            Point outNode = nodeGenePositions.get(gene.getOutNode());

            gc.strokeLine(inNode.x + circleD, inNode.y + circleD/2, outNode.x, outNode.y + circleD/2);
        }
    }
}
