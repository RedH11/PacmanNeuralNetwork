package game;

import game.NEAT.ConnectionGene;
import game.NEAT.NodeGene;

import java.io.*;
import java.util.Map;

public class GhostInfoStorage implements Serializable {

    /** Visual Information
     * MAXMOVES - Maximum Amount of Moves
     * pacCoords - Pacman's Coordinate History
     * gCoords - Ghost's (Inky for now) Coordinate History
     * gDirs - Ghost's Direction History
     * gFits - Ghost's Fitness History
     */
    private int MAXMOVES;
    private int[][] pacCoords;
    private int[][] g1Coords;
    private int[][] g2Coords;
    private int[] g1Fits;
    private int[] g2Fits;
    private boolean[] pPowered;
    private Map<Integer, ConnectionGene> connections ;
    private Map<Integer, NodeGene> nodes;

    // Stores the top ten average fitnesses of Pacman, Inky, and InkyTwo
    private int[] layerSizes = new int[3];

    // Index tracking
    private int totalCoords = 0;
    private int totalInfo = 0;

    public GhostInfoStorage(int MAXMOVES) {
        this.MAXMOVES = MAXMOVES;
        pacCoords = new int[MAXMOVES][2];
        g1Coords = new int [MAXMOVES][2];
        g2Coords = new int [MAXMOVES][2];
        g1Fits = new int[MAXMOVES];
        g2Fits = new int[MAXMOVES];
        pPowered = new boolean[MAXMOVES];
    }
    
    public void addAllCoords(int px, int py, int g1x, int g1y, int g2x, int g2y) {
        pacCoords[totalCoords][0] = px;
        pacCoords[totalCoords][1] = py;

        g1Coords[totalCoords][0] = g1x;
        g1Coords[totalCoords][1] = g1y;

        g2Coords[totalCoords][0] = g2x;
        g2Coords[totalCoords][1] = g2y;

        totalCoords++;
    }

    public void addAllInfo(int g1fit, int g2fit, boolean powered) {
        g1Fits[totalInfo] = g1fit;
        g2Fits[totalInfo] = g2fit;
        pPowered[totalInfo] = powered;
        totalInfo++;
    }

    public void setLayerSizes(int[] layerSizes) {
        this.layerSizes = layerSizes;
    }

    public void setGenomeInfo(Map<Integer, NodeGene> nodes, Map<Integer, ConnectionGene> connections) {
        this.nodes = nodes;
        this.connections = connections;
    }

    public int[] getLayerSizes() {
        return layerSizes;
    }

    public Map<Integer, NodeGene> getNodes() {
        return nodes;
    }

    public Map<Integer, ConnectionGene> getConnections() {
        return connections;
    }

    public boolean[] getpPowered() {
        return pPowered;
    }

    public int[][] getPacCoords() {
        return pacCoords;
    }

    public int getMAXMOVES() {
        return MAXMOVES;
    }

    public int[][] getG1Coords() {
        return g1Coords;
    }

    public int[][] getG2Coords() {
        return g2Coords;
    }
}
