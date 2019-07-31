package game;

import java.io.Serializable;

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
    int[] pDirs;
    private int[] choices;
    private int[] pFits;
    private double[] weights;
    private double[] biases;
    private double[][] outputs;

    // Index tracking
    private int totalCoords = 0;
    private int totalInfo = 0;
    private int totalNNInfo = 0;

    public InfoStorage(int MAXMOVES) {
        this.MAXMOVES = MAXMOVES;
        pacCoords = new int[MAXMOVES][2];
        pDirs = new int[MAXMOVES];
        choices = new int[MAXMOVES];
        pFits = new int[MAXMOVES];
        outputs = new double[MAXMOVES][4];
    }

    public void initializeNNStorage(int weightsLength, int biasLength) {
        weights = new double[weightsLength];
        biases = new double[biasLength];
    }

    public void addAllCoords(int px, int py) {
        pacCoords[totalCoords][0] = px;
        pacCoords[totalCoords][1] = py;
        totalCoords++;
    }

    public void addAllInfo(int choice, int dir, int fit) {
        choices[totalInfo] = choice;
        pDirs[totalInfo] = dir;
        pFits[totalInfo] = fit;
        totalInfo++;
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


    public double[] getBiases() {
        return biases;
    }

    public double[][] getOutputs() {
        return outputs;
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
