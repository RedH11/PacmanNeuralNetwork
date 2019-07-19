package game;

import javafx.scene.canvas.GraphicsContext;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class GeneticAlgorithm {
    //Initialize arraylists of total population and children
    ArrayList<PacmanGame> population = new ArrayList<>();
    ArrayList<PacmanGame> children = new ArrayList<>();

    int populationSize;
    int totalGens;
    int generation = 0;
    int lowerGhosts;
    int topGhosts;

    double mutationChance;

    Random random = new Random();
    String PacmanDataPath;

    private FileWriter fw;
    final int fileNum;

    final int MAXMOVES = 500;

    /**
     * Genetic algorithm function
     * @param PacmanDataPath dynamic data path for pacman game folder, which saves to desktop
     * @param popSize total population size of ghosts per generation
     * @param totalGens total generations to iterate
     * @param mutationChance chance to mutate per generation
     * @param lowerGhosts how many random ghosts other than the top we choose
     * @param topGhosts how many of the top ghosts to breed
     * @throws IOException
     */
    public GeneticAlgorithm(String PacmanDataPath, int popSize, int totalGens, double mutationChance, int lowerGhosts, int topGhosts) throws IOException {
        this.PacmanDataPath = PacmanDataPath;
        this.populationSize = popSize;
        this.totalGens = totalGens;
        this.mutationChance = mutationChance;
        this.lowerGhosts = lowerGhosts;
        this.topGhosts = topGhosts;

        // Gets amount of files in the folder already
        File folder = new File(PacmanDataPath);
        File[] listOfFiles = folder.listFiles();

        // Make file to hold the maps of the generations
        fileNum = listOfFiles.length / 2;

        // Make new file to store pacman games
        new File(PacmanDataPath+ "/pacGens" + fileNum).mkdir();

        // Create test data file writer
        try {
            fw = new FileWriter(PacmanDataPath + "pacData" + fileNum + ".txt");
        } catch (FileNotFoundException ex) {
            System.out.println("File Not Found");
        }

        // Write in test data to file
        try {
            fw.write("Popsize: " + popSize + "\nMutationChance: " + mutationChance + "\nTopGhosts: " + topGhosts + "\nLowerGhosts: " + lowerGhosts + "\nNNSizes ");
        } catch (NullPointerException ex) {
            System.out.println("Error Writing File");
        }

        // Write in neural network proportions
        fw.write(new Inky().INPUTS + " ");
        fw.write(new Inky().HIDDEN_ONE + " ");
        fw.write(new Inky().HIDDEN_TWO + " ");
        fw.write(new Inky().OUTPUTS + "\n");

        makePopulation();
    }

    /**
     * Creates each generation and records them to a file
     * @throws IOException if file fails to write
     */
    public void makeGenerations() throws IOException {

        while (generation < totalGens) {
            if (generation > 0) {
                recreatePopulation();
            }
            testInkys();
            sortPopulation();
            mutatePopulation();
            population.clear();
            breedPopulation();
            System.out.println("Gen: " + generation);
            generation++;
        }
        // Close file writer for fitness
        fw.close();
    }

    /**
     * Simulates Inkys
     * @throws IOException
     */
    public void testInkys() throws IOException {
        for (PacmanGame game : population) {
            game.simulateGame();
        }
    }

    // Make a fresh population
    public void makePopulation() {
        for (int i = 0; i < populationSize; i++) {
            // First member of each population has their game recorded
            if (i == 0) population.add(new PacmanGame(PacmanDataPath, MAXMOVES, true, generation, fileNum));
            else population.add(new PacmanGame(MAXMOVES));
        }
    }

    public void recreatePopulation() {
        // Add in the bred children to the population
        for (int i = 0; i < populationSize; i++) {
            population.add(children.get(i));
        }
    }

    public void mutatePopulation() {
        for (int i = 0; i < children.size(); i++) {
            children.get(i).inky.brain.mutate(mutationChance);
        }
    }

    public void breedPopulation() {
        NeuralNetwork parent1;
        NeuralNetwork parent2;

        ArrayList<Integer> parents = NetworkTools.randomValues(0, topGhosts + lowerGhosts, topGhosts + lowerGhosts);

        int parentIndx = 0;

        boolean oneRecording = false;

        // Breeds the ghosts based on the parents indexes
        while (children.size() < populationSize) {

            parent1 = children.get(parents.get(parentIndx)).inky.brain;
            parent2 = children.get(parents.get(parentIndx + 1)).inky.brain;

            if (parentIndx == (topGhosts + lowerGhosts - 2)) {
                parentIndx = 0;
            }

            // One child records their game
            if (!oneRecording) {
                children.add(new PacmanGame(PacmanDataPath, MAXMOVES, true, generation, fileNum, parent1.makeChild(parent2)));
                oneRecording = true;
            }

            // Add the newly bred child to the children array (won't be selected because of the parents array boundary)
            children.add(new PacmanGame(MAXMOVES, parent1.makeChild(parent2)));

            parentIndx++;
        }
    }

    /**
     * Sorts population by fitness, worst to best
     * @throws IOException if file fails to write
     */
    public void sortPopulation() throws IOException {

        children.clear();

        // Add top __ ghosts to the children array to be bred
        for (int i = 0; i < topGhosts; i++) {
            children.add(population.get(populationSize - i - 1));
        }

        // Get _ lower scoring ghosts to be bred
        ArrayList<Integer> rand = NetworkTools.randomValues(0, populationSize - 1 - topGhosts, lowerGhosts);

        for (int i = 0; i < rand.size(); i++) {
            children.add(population.get(rand.get(i)));
        }

        // Sort inkys
        children.sort(new FitnessComparator());
        System.out.println("Gen " + generation + " fitness " + children.get(children.size() - 1).getInkyFitness());
        String fitness = Double.toString(Math.round(children.get(children.size() - 1).getInkyFitness()));
        fw.write(fitness + "\n");
    }
}
