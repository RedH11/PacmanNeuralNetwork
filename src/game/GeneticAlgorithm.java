package game;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class GeneticAlgorithm {

    ArrayList<PacmanGame> gamePopulation = new ArrayList<>();
    ArrayList<PacmanGame> pacmanBabys = new ArrayList<>();
    ArrayList<NeuralNetwork> pacmanBrains = new ArrayList<>();

    int populationSize;
    int totalGens;
    int generation = 0;
    int lowerPacman;
    int topPacman;
    double mutationChance;

    Random random = new Random();
    String PacmanDataPath;


    ArrayList<Double> probArr = new ArrayList<>();

    private FileWriter pacWriter;
    private String pacFitnessStr;

    final int fileNum;
    final int MAXMOVES = 100;

    boolean startBreeding = false;


    public GeneticAlgorithm(String PacmanDataPath, int popSize, int totalGens, double mutationChance, int lowerPacman, int topPacman) throws IOException {
        this.PacmanDataPath = PacmanDataPath;
        this.populationSize = popSize;
        this.totalGens = totalGens;
        this.mutationChance = mutationChance;
        this.lowerPacman = lowerPacman;
        this.topPacman = topPacman;

        // u1 is the chance that the number one pacman is the parent
        double r = calcRate(topPacman + lowerPacman, 20);
        fillProbArr(r, topPacman + lowerPacman, 20);

        // Gets amount of files int he folder already
        File folder = new File(PacmanDataPath);
        File[] listOfFiles = folder.listFiles();

        // Make file to hold the Game Data
        fileNum = listOfFiles.length;
        new File(PacmanDataPath + "/Game" + fileNum).mkdir();
        this.PacmanDataPath += "/Game" + fileNum;

        // Make new file to store the generation game tracker
        new File(this.PacmanDataPath + "/Gens").mkdir();

        // Create test data file writer
        try {
            pacWriter = new FileWriter(this.PacmanDataPath + "/pacFits" + fileNum + ".txt");
          //  inkyWriter = new FileWriter(this.PacmanDataPath + "/inkyFits" + fileNum + ".txt");
        } catch (FileNotFoundException ex) {
            System.out.println("File Not Found");
        }

        // Write in test data to file
        try {
            pacWriter.write("Pacman - Popsize: " + popSize + "\nMutationChance: " + mutationChance + "\nTopGhosts: " + topPacman + "\nLowerGhosts: " + lowerPacman + "\nNNSizes ");

            // Write in neural network proportions
            pacWriter.write(new Pacman().INPUTS + " ");
            pacWriter.write(new Pacman().HIDDEN_ONE + " ");
            //pacWriter.write(new Pacman().HIDDEN_TWO + " ");
            pacWriter.write(new Pacman().OUTPUTS + "\n");

            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

            // File write the time
            pacWriter.write(new SimpleDateFormat("dd-MMM-YYYY").format(new Date()) + "\n");
            pacWriter.write( sdf.format(cal.getTime()) + "\n");

        } catch (NullPointerException ex) {
            System.out.println("Error Writing File");
        }

        makePopulation();
    }


    public void makeGenerations() throws IOException {

        while (generation < totalGens) {
            test();
            sort();
            if (startBreeding) {
                mutate();
                clear();
                breedPopulation();
                generation++;
            } else {
                //System.out.println("Population Failed");
                makePopulation();
            }
        }

        // Write all fitness records to file
        writeAllFitnesses();
        // Close file writer for fitness
        pacWriter.close();
    }


    private void writeAllFitnesses() throws IOException {
        pacWriter.write(pacFitnessStr);
    }

    // Make a fresh population of Inkys and Pacmans
    public void makePopulation() {
        gamePopulation.clear();
        for (int i = 0; i < populationSize; i++) {
            // Make a fresh new population
            gamePopulation.add(new PacmanGame(PacmanDataPath, MAXMOVES));
        }
    }

    // Test inkys and pacmans in games
    public void test() {
        for (PacmanGame game : gamePopulation) {
            game.simulateGame();
        }
    }

    public void sort() {

        pacmanBabys.clear();

        // Add all inkys and pacmen to the baby arrays
        for (int i = 0; i < populationSize; i++) {
            // Don't allow things with 0 fitness to be added
            pacmanBabys.add(gamePopulation.get(i));
        }

        // Sort the pacman babies by their fitnesses
        pacmanBabys.sort(new PacmanFitnessComparator());

        startBreeding =  pacmanBabys.get(pacmanBabys.size() - 1).pacman.fitness != 0;

        if (generation == 300 && pacmanBabys.get(pacmanBabys.size() - 1).pacman.fitness < 500) {
            generation = 0;
            startBreeding = false;
        }

        if (startBreeding) {

            // Make file records of the best Pacman
            pacmanBabys.get(pacmanBabys.size() - 1).writeFileContent(generation);

            if (generation % 10 == 0) {
                System.out.println("\nLeaderboard: " + generation);
                for (int i = 1; i < pacmanBabys.size(); i++) {
                    System.out.println("#" + (i - 1) + " : " + pacmanBabys.get(pacmanBabys.size() - i).getPacmanFitness());
                    if (i == 10) break;
                }
            }

            String pacmanFitness = Double.toString(Math.round(pacmanBabys.get(pacmanBabys.size() - 1).getPacmanFitness()));
            if (pacmanFitness != null) pacFitnessStr += (pacmanFitness + "\n");

            while (pacmanBabys.size() > topPacman) {
                pacmanBabys.remove(0);
            }

            // Get _ lower scoring pacmen to be bred
            ArrayList<Integer> randPacmen = NetworkTools.randomValues(0, populationSize - 1 - topPacman, lowerPacman);

            for (int i = 0; i < randPacmen.size(); i++) {
                // If the fitness isn't 0 allow it to be part of the breeding population
                if (gamePopulation.get(randPacmen.get(i)).pacman.fitness != 0) {
                    pacmanBabys.add(gamePopulation.get(randPacmen.get(i)));
                }
            }

            // Sort pacman array
            pacmanBabys.sort(new PacmanFitnessComparator());
        }
    }

    public void mutate() {
        // Mutate some pacman babies
        for (int i = 0; i < pacmanBabys.size(); i++) {
            pacmanBabys.get(i).pacman.brain.mutate(mutationChance);
        }
    }

    public void clear() {
        gamePopulation.clear();
    }

    public NeuralNetwork getRandParent() {
        double randParent = 0;
        NeuralNetwork parent = null;
        for (int i = 0; i < probArr.size(); i++) {
            randParent = random.nextDouble() * 100;
            if (randParent < probArr.get(i)) {
                parent = pacmanBabys.get(pacmanBabys.size() - 1 - i).pacman.brain;
                break;
            }
        }
        return parent;
    }

    public void breedPopulation() {

        NeuralNetwork parent1 = null;
        NeuralNetwork parent2 = null;

        // Pacman Breeding
        pacmanBrains.clear();

        // Add in top pacman brains
        for (int i = 0; i < pacmanBabys.size(); i++) {
            pacmanBrains.add(pacmanBabys.get(i).pacman.brain);
        }

        // Choose random parents to breed to make new pacmen
        while (pacmanBrains.size() < populationSize) {
            parent1 = getRandParent();
            parent2 = getRandParent();

            if (parent1 != null && parent2 != null) pacmanBrains.add(parent1.makeChild(parent2));
        }

        // Repopulated game population with new inky and pacman brains starting with 0 fitness
        for (int i = 0; i < populationSize; i++) {
            gamePopulation.add(new PacmanGame(PacmanDataPath, MAXMOVES, pacmanBrains.get(i)));
        }

    }

    public double calcRate(int n, double u1) {
        double SumN = 0;
        double r = .01; // default to 2
        double change = .5;
        boolean switched = false;

        // Using the sum of a geometric sequence equation to find what the rate should be for n numbers

        // While the sum (rounded up) of the geometric sequence is not 100
        while (Math.round(SumN) != 100) {

            if (r == 1) r -= 0.1; // safeguard against dividing by 0

            SumN = u1 * (1 - Math.pow(r, n)) / (1 - r);
            //console.log("SumN = " + SumN + " r = " + r + " change = " + change);
            // If SumN is larger than 100 decrease the rate else increase it to find the exact rate
            if (Math.round(SumN) < 100) {
                r += change;
                switched = true;
                //change /= 2; // Divide the change in half to get more specific in finding the exact value
            } else { // Only decreases when above because thats a change
                r -= change;
                if (switched) change /= 2;
                switched = false;
            }
        }

        return r;
    }
    public void fillProbArr(double r, int n, double u1) {
        double tsum = 0;
        // calculate the values for the geomectric sequence
        // Un = U1 * r^n-1
        for (int i = 1; i < n; i++) {
            tsum += u1 * Math.pow(r, i - 1);
            probArr.add(tsum);
        }
    }
}
