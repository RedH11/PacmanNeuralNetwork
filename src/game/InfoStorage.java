package game;

import java.io.*;

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
    private int[][] g1Coords;
    private int[][] g2Coords;

    int[] pDirs;


    private int[] choices;
    private int[] g1Choices;
    private int[] g2Choices;

    private int[] pFits;
    private int[] g1Fits;
    private int[] g2Fits;

    private boolean[] pPowered;

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
        g1Coords = new int [MAXMOVES][2];
        g2Coords = new int [MAXMOVES][2];

        pDirs = new int[MAXMOVES];

        choices = new int[MAXMOVES];
        g1Choices = new int[MAXMOVES];
        g2Choices = new int[MAXMOVES];

        pFits = new int[MAXMOVES];
        g1Fits = new int[MAXMOVES];
        g2Fits = new int[MAXMOVES];


        pPowered = new boolean[MAXMOVES];
        outputs = new double[MAXMOVES][4];
    }

    public void initializeNNStorage(int weightsLength, int biasLength) {
        weights = new double[weightsLength];
        biases = new double[biasLength];
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

    public void addAllInfo(int choice, int g1choice, int g2choice,   int dir, int fit, int g1fit, int g2fit, boolean powered) {
        choices[totalInfo] = choice;
        g1Choices[totalInfo] = g1choice;
        g2Choices[totalInfo] = g2choice;


        pDirs[totalInfo] = dir;

        pFits[totalInfo] = fit;
        g1Fits[totalInfo] = g1fit;
        g2Fits[totalInfo] = g2fit;


        pPowered[totalInfo] = powered;

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
        totalNNInfo++;
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

    public int[] getGhostTopTenFitnesses() {
        return ghostTopTenFitnesses;
    }

    public int[] getPacTopTenFitnesses() {
        return pacTopTenFitnesses;
    }

    public int[] getChoices() { return choices; }

    public int[] getpFits() {
        return pFits;
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

    public int[] getpDirs() {
        return pDirs;
    }
}
