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
        pDirs = new int[MAXMOVES];
        pFits = new int[MAXMOVES];
        pPowered = new boolean[MAXMOVES];
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

    public void addAllInfo(int dir, int fit, boolean powered) {
        pDirs[totalInfo] = dir;
        pFits[totalInfo] = fit;
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

    public int[] getpDirs() {
        return pDirs;
    }

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
}
