package game;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class GeneticAlgorithm {

    ArrayList<Pacman> pacmen = new ArrayList<>();
    ArrayList<PacmanGame> pacmanBabys = new ArrayList<>();
    ArrayList<PacmanGame> gamePopulation = new ArrayList<>();
    ArrayList<NeuralNetwork> pacmanBrains = new ArrayList<>();

    ArrayList<Double> probArr = new ArrayList<>();

    private int populationSize;
    private int totalGens;
    private int generation = 0;
    private int lowerPacman;
    private int topPacman;
    private double mutationChance;

    Random random = new Random();
    String PacmanDataPath;
    int round = 0;

    private FileWriter pacWriter;
    private String pacFitnessStr;
    private OutputStream opsPac;
    private ObjectOutputStream oosPac = null;
    private NeuralNetwork parent1;
    private NeuralNetwork parent2;

    private final int MAXMOVES = 600;
    private boolean lastRoundRobin = false;

    private int fitnessX = 10;
    private GraphicsContext gc;
    private int coordinateW = 5;

    public GeneticAlgorithm(String PacmanDataPath, int popSize, int totalGens, double mutationChance, int lowerGhosts, int topGhosts, int lowerPacman, int topPacman, GraphicsContext gc) throws IOException {
        this.PacmanDataPath = PacmanDataPath;
        this.populationSize = popSize;
        this.totalGens = totalGens;
        this.mutationChance = mutationChance;
        this.lowerPacman = lowerPacman;
        this.topPacman = topPacman;
        this.gc = gc;

        // u1 is the chance that the number one pacman is the parent
        double r = calcRate(topPacman + lowerPacman, 20);
        fillProbArr(r, topPacman + lowerPacman, 20);

        createFitnessWriter(popSize);

        makePopulation();
    }

    private void createFitnessWriter(int popSize) throws IOException {
        // Gets amount of files int he folder already
        File folder = new File(PacmanDataPath);
        File[] listOfFiles = folder.listFiles();

        // Make file to hold the Game Data
        int fileNum = listOfFiles.length;
        new File(PacmanDataPath + "/Game" + fileNum).mkdir();
        this.PacmanDataPath += "/Game" + fileNum;

        // Make new file to store the generation game tracker
        new File(this.PacmanDataPath + "/Gens").mkdir();

        // Create test data file writer
        try {
            pacWriter = new FileWriter(this.PacmanDataPath + "/pacFits" + fileNum + ".txt");
            opsPac = new FileOutputStream(PacmanDataPath + "/Gens/PacGens");
            oosPac = new ObjectOutputStream(opsPac);

        } catch (FileNotFoundException ex) {
            System.out.println("File Not Found");
        }

        // Write in test data to file
        try {
            pacWriter.write("Pacman - Popsize: " + popSize + "\nMutationChance: " + mutationChance + "\nTopGhosts: " + topPacman + "\nLowerGhosts: " + lowerPacman + "\nNNSizes ");

            // Write in neural network proportions
            pacWriter.write(new Pacman().INPUTS + " ");
            pacWriter.write(new Pacman().HIDDEN_ONE + " ");
            pacWriter.write(new Pacman().OUTPUTS + "\n");

            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

            // File write the time
            pacWriter.write(new SimpleDateFormat("dd-MMM-YYYY").format(new Date()) + "\n");
            pacWriter.write( sdf.format(cal.getTime()) + "\n");

        } catch (NullPointerException ex) {
            System.out.println("Error Writing File");
        }
    }

    public void makeGenerations() throws IOException {

        while (generation < totalGens) {
            roundRobin();
            roundRobin();
            lastRoundRobin = true;
            roundRobin();
            sort();
            mutate();
            clear();
            breedPopulation();
            generation++;
            lastRoundRobin = false;
            round = 0;
        }

        // Write all fitness records to file
        writeAllFitnesses();
        // Close file writer for fitness
        pacWriter.close();
        oosPac.close();
    }

    private void writeAllFitnesses() throws IOException {
        pacWriter.write(pacFitnessStr);
    }

    // Make a fresh population of Inkys and Pacmans
    public void makePopulation() {
        for (int i = 0; i < populationSize; i++) {
            // Make a fresh new population
            gamePopulation.add(new PacmanGame(PacmanDataPath, MAXMOVES));
        }
    }

    // Test inkys and pacmans in games
    public void test() {
        for (PacmanGame game : gamePopulation) {
            game.simulateGame(round);
        }
    }

    public void roundRobin() {
        round++;

        test();
        pacmen.clear();

        for (PacmanGame game : gamePopulation) {
            pacmen.add(game.pacman);
        }

        // Shuffle around pacmen to hit new inky ghosts
        Collections.shuffle(pacmen);

        if (!lastRoundRobin) gamePopulation.clear();

        int ghostIndx = 0;

        for (int i = 0; i < populationSize; i++) {
            // Put all of the inkies and pacmen back into new games with different opponents
            gamePopulation.add(new PacmanGame(PacmanDataPath, MAXMOVES, pacmen.get(i).brain));
            ghostIndx += 2;
        }
    }

    public void sort() {

        pacmanBabys.clear();

        // Add all inkies and pacmen to the baby arrays
        for (int i = 0; i < populationSize; i++) {
            pacmanBabys.add(gamePopulation.get(i));
        }

        // Sort the pacman babies by their fitnesses
        pacmanBabys.sort(new PacmanFitnessComparator());

        PacmanGame topPac = pacmanBabys.get(pacmanBabys.size() - 1);

        topPac.getIs().initializeNNStorage(topPac.pacman.brain.getArrayWeights().length, topPac.pacman.brain.getArrayBias().length);

        try {
            // Make file records of the best Pacman/Inky
            pacmanBabys.get(pacmanBabys.size() - 1).saveInformation(topPac.getIs(), oosPac);

        } catch (Exception ex) {}
        recordFitness();

        // Remove non top pacman
        while (pacmanBabys.size() > topPacman) {
            pacmanBabys.remove(0);
        }

        // Get a set number of lower scoring ghosts/pacmen to be kept alive
        ArrayList<Integer> randPacmen = NetworkTools.randomValues(0, populationSize - 1 - topPacman, lowerPacman);

        for (int i = 0; i < randPacmen.size(); i++) {
            pacmanBabys.add(gamePopulation.get(randPacmen.get(i)));
        }

        // Sort inkys and pacmen
        pacmanBabys.sort(new PacmanFitnessComparator());
    }

    public void mutate() {
        // Mutate some pacman babies
        for (int i = 0; i < pacmanBabys.size(); i++) {
            pacmanBabys.get(i).pacman.brain.mutate(mutationChance);
        }
    }

    private void recordFitness() {

        if (pacmanBabys.get(pacmanBabys.size() - 1).getPacmanAverageFitness() == 0) {
            gc.setFill(Color.YELLOW);
            gc.fillOval(fitnessX, 620, coordinateW, coordinateW);
            gc.fillText("0", fitnessX - coordinateW, 620-20);

        } else {
            gc.setFill(Color.YELLOW);
            gc.fillOval(fitnessX, 620 - (pacmanBabys.get(pacmanBabys.size() - 1).getPacmanAverageFitness() / 2), coordinateW, coordinateW);
            gc.fillText(Double.toString(pacmanBabys.get(pacmanBabys.size() - 1).getPacmanAverageFitness()), fitnessX - coordinateW, 620 - (pacmanBabys.get(pacmanBabys.size() - 1).getPacmanAverageFitness() / 2 - 20));
        }

        fitnessX += 40;

        String pacmanFitness = Double.toString(Math.round(pacmanBabys.get(pacmanBabys.size() - 1).getPacmanAverageFitness()));
        if (pacmanFitness != null) pacFitnessStr += (pacmanFitness + "\n");
    }

    public void clear() {
        gamePopulation.clear();
    }

    public void breedPopulation() {

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
}
