package game;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class GeneticAlgorithm {

    GraphicsContext gc;

    ArrayList<GameArray> pop = new ArrayList<>();
    ArrayList<GameArray> children = new ArrayList<>();

    int popSize;
    int totalGens;
    int generation = 0;
    int lowerGhosts;
    int topGhosts;

    double mutationChance;

    Random random = new Random();

    FileWriter fw;

    private final int[] NETWORK_LAYER_SIZES;

    public GeneticAlgorithm(int popSize, int totalGens, double mutationChance, int lowerGhosts, int topGhosts, GraphicsContext gc, int... NETWORK_LAYER_SIZES) throws IOException {
        this.popSize = popSize;
        this.gc = gc;
        this.totalGens = totalGens;
        this.mutationChance = mutationChance;
        this.lowerGhosts = lowerGhosts;
        this.topGhosts = topGhosts;
        this.NETWORK_LAYER_SIZES = NETWORK_LAYER_SIZES;

        // Gets amount of files int he folder already
        File folder = new File("/Users/hunterwebb/Desktop/PacmanData");
        File[] listOfFiles = folder.listFiles();

        try {
            fw = new FileWriter("/Users/hunterwebb/Desktop/PacmanData/pacData" + listOfFiles.length + ".txt");
        } catch (FileNotFoundException ex) {
            System.out.println("File Not Found");
        }

        try {
            fw.write("Popsize: "  + popSize + "\nMutationChance: " + mutationChance + "\nTopGhosts: " + topGhosts + "\nLowerGhosts: " + lowerGhosts + "\nNNSizes ");
        } catch (NullPointerException ex) {
            System.out.println("Error Writing File");
        }

        for (int i = 0; i < NETWORK_LAYER_SIZES.length; i++) {
            fw.write(NETWORK_LAYER_SIZES[i] + " ");
        }

        fw.write("\n\n");

        makePopulation();
    }

    public void makeGenerations() throws IOException {

        while (generation < totalGens) {
            //System.out.println("New Gen");
            if (generation > 0) {
                recreatePopulation();
                //System.out.println("Recreated");
            }
            testInkys();
            //System.out.println("Tested");
            sortPopulation();
            //System.out.println("Sorted");

            mutatePopulation();
            //System.out.println("Mutated");

            breedPopulation();
            //System.out.println("Bred");
            generation++;
        }

        fw.close();
    }

    public void testInkys() {

        for (GameArray game : pop) {
            game.runGame();
        }
    }

    // Make a fresh population
    public void makePopulation() {
        for (int i = 0; i < popSize; i++) {
            pop.add(new GameArray(NETWORK_LAYER_SIZES));
        }
    }

    public void recreatePopulation() {
        // Add in the bred children to the population
        for (int i = 0; i < popSize; i++) {
            pop.add(children.get(i));
        }
    }

    public void mutatePopulation() {
        for (int i = 0; i < children.size(); i++) {
            children.get(i).inkyGhost.getBrain().mutate(mutationChance);
        }
    }

    public void breedPopulation() {

        pop.clear();

        NeuralNetwork parent1;
        NeuralNetwork parent2;

        ArrayList<Integer> parents = NetworkTools.randomValues(0, topGhosts + lowerGhosts, topGhosts + lowerGhosts);

        int parentIndx = 0;

        // Breeds the ghosts based on the parents indexes
        while (children.size() < popSize) {

            parent1 = children.get(parents.get(parentIndx)).inkyGhost.getBrain();
            parent2 = children.get(parents.get(parentIndx + 1)).inkyGhost.getBrain();

            if (parentIndx == (topGhosts + lowerGhosts - 2)) {
                parentIndx = 0;
            }
            // Add the newly bred child to the children array (won't be selected because of the parents array boundary)
            children.add(new GameArray(parent1.makeChild(parent2)));
            parentIndx++;
        }
    }

    public void sortPopulation() throws IOException {

        children.clear();

        // Add top __ ghosts to the children array to be bred
        for (int i = 0; i < topGhosts; i++) {
            children.add(pop.get(popSize - i - 1));
        }

        // Get _ lower scoring ghosts to be bred
        ArrayList<Integer> rand = NetworkTools.randomValues(0, popSize - 1 - topGhosts, lowerGhosts);

        for (int i = 0; i < rand.size(); i++) {
            children.add(pop.get(rand.get(i)));
        }

        // Sort inkys
        children.sort(new IncomComparator());

        System.out.println("Gen " + generation + " fitness " + children.get(children.size() - 1).inkyGhost.getScore());

        String fitness = Double.toString(Math.round(children.get(children.size() - 1).inkyGhost.getScore()));
        fw.write(fitness + "\n");
    }
}
