package game;

import java.io.*;
import java.util.ArrayList;

public class InfoStorage implements Serializable {

    /** Visual Information
     * MAXMOVES - Maximum Amount of Moves
     * pacCoords - Pacman's Coordinate History
     * gCoords - Ghost's (Inky for now) Coordinate History
     * gDirs - Ghost's Direction History
     * gFits - Ghost's Fitness History
     */
    private int MAXMOVES;
    private int[][] pacCoords;
    private int[] pDirs;
    private int[] pFits;
    private boolean[] pPowered;
    private int[][] gCoords;
    private int[][] g2Coords;
    private int[] gDirs;
    private int[] g2Dirs;
    private int[] gFits;
    private int[] g2Fits;
    private double[] weights;
    private double[] biases;
    private double[][] outputs;

    // Stores the top ten average fitnesses of Pacman, Inky, and InkyTwo
    private  int[] pacTopTenFitnesses = new int[10];
    private int[] ghostTopTenFitnesses = new int[10];

    // Index tracking
    private int totalCoords = 0;
    private int totalInfo = 0;
    private int totalNNInfo = 0;

    public InfoStorage(int MAXMOVES) {
        this.MAXMOVES = MAXMOVES;
        pacCoords = new int[MAXMOVES][2];
        gCoords = new int[MAXMOVES][2];
        g2Coords = new int[MAXMOVES][2];
        gDirs = new int[MAXMOVES];
        gFits = new int[MAXMOVES];
        g2Dirs = new int[MAXMOVES];
        g2Fits = new int[MAXMOVES];
        pDirs = new int[MAXMOVES];
        pFits = new int[MAXMOVES];
        pPowered = new boolean[MAXMOVES];
        outputs = new double[MAXMOVES][4];
    }

    public void initializeNNStorage(int weightsLength, int biasLength) {
        weights = new double[weightsLength];
        biases = new double[biasLength];
    }

    
    public void addAllCoords(int px, int py, int ix, int iy, int ix2, int iy2) {
        pacCoords[totalCoords][0] = px;
        pacCoords[totalCoords][1] = py;
        gCoords[totalCoords][0] = ix;
        gCoords[totalCoords][1] = iy;
        g2Coords[totalCoords][0] = ix2;
        g2Coords[totalCoords][1] = iy2;
        totalCoords++;
    }

    public void addAllInfo(int dir, int fit, boolean powered, int gDir, int gFit, int g2Dir, int g2Fit) {
        pDirs[totalInfo] = dir;
        pFits[totalInfo] = fit;
        pPowered[totalInfo] = powered;
        gDirs[totalInfo] = gDir;
        gFits[totalInfo] = gFit;
        g2Dirs[totalInfo] = g2Dir;
        g2Fits[totalInfo] = g2Fit;
        totalInfo++;
    }

    // Saves the top ten fitnesses of the game
    public void setTopTenFitness(int[] pacTopTen, int[] ghostTopTen) {
        pacTopTenFitnesses = pacTopTen;
        ghostTopTenFitnesses = ghostTopTen;
    }

    public void setNNInfo(double[] weights, double[] biases) {
        this.weights = weights;
        this.biases = biases;
    }

    public void setNNOutputs(double[] outputs) {
        this.outputs[totalNNInfo] = outputs;
    }

    public int getTotalCoords() {
        return totalCoords;
    }

    public boolean[] getpPowered() {
        return pPowered;
    }

    public double[] getBiases() {
        return biases;
    }

    public double[][] getOutputs() {
        return outputs;
    }

    public int[] getG2Dirs() {
        return g2Dirs;
    }

    public int[] getG2Fits() {
        return g2Fits;
    }

    public int[] getgDirs() {
        return gDirs;
    }

    public int[] getgFits() {
        return gFits;
    }

    public int[] getGhostTopTenFitnesses() {
        return ghostTopTenFitnesses;
    }

    public int[] getPacTopTenFitnesses() {
        return pacTopTenFitnesses;
    }

    public int[] getpDirs() {
        return pDirs;
    }

    public int[] getpFits() {
        return pFits;
    }

    public int[][] getG2Coords() {
        return g2Coords;
    }

    public int[][] getgCoords() {
        return gCoords;
    }

    public int[][] getPacCoords() {
        return pacCoords;
    }

    public double[] getWeights() {
        return weights;
    }

    public int getMAXMOVES() {
        return MAXMOVES;
    }
}
