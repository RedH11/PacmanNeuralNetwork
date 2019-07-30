package game;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class GeneticAlgorithm {

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

    private FileWriter pacWriter;
    private String pacFitnessStr = "";
    private OutputStream opsPac;
    private ObjectOutputStream oosPac = null;
    private NeuralNetwork parent1;
    private NeuralNetwork parent2;

    // Settings for graphing the fitness
    private int fitnessX = 10;
    private GraphicsContext gc;
    private int coordinateW = 5;

    private int MAXMOVES = 200;

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

    // Creates the file and filewriter class to save the fitness of pacman over generations
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
            pacWriter.write("Popsize: " + popSize + "\nMutationChance: " + mutationChance + "\nTopGhosts: " + topPacman + "\nLowerGhosts: " + lowerPacman + "\nNNSizes ");

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
            test();
            sort();
            clear();
            breedPopulation();
            mutate();
            generation++;
        }

        // Write all fitness records to file
        writeAllFitnesses();
        // Close file writer for fitness
        pacWriter.close();
        oosPac.close();
    }

    // Make a fresh population of Pacmans
    public void makePopulation() {
        for (int i = 0; i < populationSize; i++) {
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

        // Add all Pacman to the baby array
        for (int i = 0; i < populationSize; i++) {
            pacmanBabys.add(gamePopulation.get(i));
        }

        // Sort the array by fitness (lowest -> highest)
        pacmanBabys.sort(new PacmanFitnessComparator());

        // Save the top Pacman's weights and biases
        //PacmanGame topPac = pacmanBabys.get(pacmanBabys.size() - 1);
        //topPac.getIs().initializeNNStorage(topPac.pacman.brain.getArrayWeights().length, topPac.pacman.brain.getArrayBias().length);

        try {
            // Save file records of the best Pacman/Inky
            //pacmanBabys.get(pacmanBabys.size() - 1).saveInformation(topPac.getIs(), oosPac);

        } catch (Exception ex) {}

        recordFitness();

        // Remove non-top pacman
        while (pacmanBabys.size() > topPacman) {
            pacmanBabys.remove(0);
        }

        // Get random lower pacman indexes to allow to live on to the next generation
        ArrayList<Integer> randPacmen = NetworkTools.randomValues(0, populationSize - 1 - topPacman, lowerPacman);

        // Add the random lower pacman to the baby array
        for (int i = 0; i < randPacmen.size(); i++) {
            pacmanBabys.add(gamePopulation.get(randPacmen.get(i)));
        }

        pacmanBabys.sort(new PacmanFitnessComparator());
    }

    public void mutate() {
        // Mutate some pacman babies (change a weight by a random double between -1 and 1)
        for (int i = 0; i < pacmanBabys.size(); i++) {
            gamePopulation.get(i).pacman.brain.mutate(mutationChance);
        }
    }

    // Create parents from breeding (crossing over the weights and biases of) the Pacman babies (it is baby incest but....I guess it's for science?)
    public void breedPopulation() {
        pacmanBrains.clear();

        // Add in top pacman brains
        for (int i = 0; i < pacmanBabys.size(); i++) {
            pacmanBrains.add(pacmanBabys.get(i).pacman.brain);
        }

        // Choose random parents to breed to make new pacman
        while (pacmanBrains.size() < populationSize) {
            parent1 = getRandParent();
            parent2 = getRandParent();
            // Make sure the parents aren't the same
            while (parent2 == parent1) parent2 = getRandParent();

            if (parent1 != null && parent2 != null) pacmanBrains.add(parent1.makeChild(parent2));
        }

        // Repopulate game population with new inky and pacman brains starting with 0 fitness
        for (int i = 0; i < populationSize; i++) {
            gamePopulation.add(new PacmanGame(PacmanDataPath, MAXMOVES, pacmanBrains.get(i)));
        }
    }

    // Gets a random parent based on the probability array (higher chance of the highest fitness parents being chosen)
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

    private void writeAllFitnesses() throws IOException {
        pacWriter.write(pacFitnessStr);
    }

    public void clear() { gamePopulation.clear(); }

    // Records the fitness of the pacman to a file and prints it out in the console (every 10 generations)
    private void recordFitness() {
        // Lighter color is a lower mutation rate on the display
        if (mutationChance <= 0.5) gc.setFill(Color.rgb(204, 229, 255));
        else if (mutationChance <= 0.6) gc.setFill(Color.rgb(153, 204, 255));
        else if (mutationChance <= 0.7) gc.setFill(Color.rgb(102, 178, 255));
        else if (mutationChance <= 0.8) gc.setFill(Color.rgb(51, 153, 255));
        else if (mutationChance <= 0.9) gc.setFill(Color.rgb(0, 128, 255));
        else gc.setFill(Color.rgb(0, 102, 204));

        if (generation % 10 == 0) System.out.println("Gen " + generation + " " + pacmanBabys.get(pacmanBabys.size() - 1).pacman.fitness);
        if (pacmanBabys.get(pacmanBabys.size() - 1).pacman.fitness == 0) {
            gc.fillOval(fitnessX, 620, coordinateW, coordinateW);

        } else {
            gc.fillOval(fitnessX, 620 - (pacmanBabys.get(pacmanBabys.size() - 1).pacman.fitness / 2), coordinateW, coordinateW);
        }

        // The x coordinate of the points that are being graphed to show fitness progression
        fitnessX += 8;

        String pacmanFitness = Double.toString(Math.round(pacmanBabys.get(pacmanBabys.size() - 1).pacman.fitness));
        pacFitnessStr += (pacmanFitness + "\n");
    }

    // Calculates the r value (rate) of the factor that the probability distribution should be decreased in a geometric sequence
    //  this is how we calculate the probability that the first, second, ... nth parent should be chosen to be bred with a bias towards
    //      the highest fitness parent
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

    // Fills the probability array based on the rate calculated
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
