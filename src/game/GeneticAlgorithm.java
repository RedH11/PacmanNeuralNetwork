package game;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class GeneticAlgorithm {

    ArrayList<Inky> inkies = new ArrayList<>();
    ArrayList<Pacman> pacmen = new ArrayList<>();
    ArrayList<Integer> parents = new ArrayList<>();
    ArrayList<PacmanGame> inkyBabys = new ArrayList<>();
    ArrayList<PacmanGame> pacmanBabys = new ArrayList<>();
    ArrayList<NeuralNetwork> inkyBrains = new ArrayList<>();
    ArrayList<PacmanGame> gamePopulation = new ArrayList<>();
    ArrayList<NeuralNetwork> pacmanBrains = new ArrayList<>();

    private int populationSize;
    private int totalGens;
    private int generation = 0;
    private int lowerGhosts;
    private int topGhosts;
    private int lowerPacman;
    private int topPacman;
    private double mutationChance;

    Random random = new Random();
    String PacmanDataPath;
    int round = 0;

    private FileWriter pacWriter;
    private String pacFitnessStr;
    private FileWriter inkyWriter;
    private String inkyFitnessStr;
    private OutputStream opsPac;
    private OutputStream opsInk;
    private ObjectOutputStream oosPac = null;
    private ObjectOutputStream oosInk = null;
    private NeuralNetwork parent1;
    private NeuralNetwork parent2;
    private int ghostIndex = 0;

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
        this.lowerGhosts = lowerGhosts;
        this.topGhosts = topGhosts;
        this.lowerPacman = lowerPacman;
        this.topPacman = topPacman;
        this.gc = gc;

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
            inkyWriter = new FileWriter(this.PacmanDataPath + "/inkyFits" + fileNum + ".txt");
            opsPac = new FileOutputStream(PacmanDataPath + "/Gens/PacGens");
            oosPac = new ObjectOutputStream(opsPac);
            opsInk = new FileOutputStream(PacmanDataPath +"/Gens/InkGens");
            oosInk = new ObjectOutputStream(opsInk);

        } catch (FileNotFoundException ex) {
            System.out.println("File Not Found");
        }

        // Write in test data to file
        try {
            pacWriter.write("Pacman - Popsize: " + popSize + "\nMutationChance: " + mutationChance + "\nTopGhosts: " + topPacman + "\nLowerGhosts: " + lowerPacman + "\nNNSizes ");
            inkyWriter.write("Inky - Popsize: " + popSize + "\nMutationChance: " + mutationChance + "\nTopGhosts: " + topGhosts + "\nLowerGhosts: " + lowerGhosts + "\nNNSizes ");

            // Write in neural network proportions
            pacWriter.write(new Pacman().INPUTS + " ");
            pacWriter.write(new Pacman().HIDDEN_ONE + " ");
            pacWriter.write(new Pacman().OUTPUTS + "\n");

            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

            // File write the time
            pacWriter.write(new SimpleDateFormat("dd-MMM-YYYY").format(new Date()) + "\n");
            inkyWriter.write(new SimpleDateFormat("dd-MMM-YYYY").format(new Date()) + "\n");
            pacWriter.write( sdf.format(cal.getTime()) + "\n");
            inkyWriter.write( sdf.format(cal.getTime()) + "\n");

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
        inkyWriter.close();
        oosInk.close();
        oosPac.close();
    }

    private void writeAllFitnesses() throws IOException {
        pacWriter.write(pacFitnessStr);
        inkyWriter.write(inkyFitnessStr);
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
        inkies.clear();
        pacmen.clear();

        for (PacmanGame game : gamePopulation) {
            pacmen.add(game.pacman);
        }

        // Shuffle around pacmen to hit new inky ghosts
        Collections.shuffle(pacmen);
        Collections.shuffle(inkies);

        if (!lastRoundRobin) gamePopulation.clear();

        int ghostIndx = 0;

        for (int i = 0; i < populationSize; i++) {
            // Put all of the inkies and pacmen back into new games with different opponents
            gamePopulation.add(new PacmanGame(PacmanDataPath, MAXMOVES, inkies.get(ghostIndx).brain, inkies.get(ghostIndx + 1).brain, pacmen.get(i).brain));
            ghostIndx += 2;
        }
    }

    public void sort() {

        // Clear arrays
        inkyBabys.clear();
        pacmanBabys.clear();

        // Add all inkies and pacmen to the baby arrays
        for (int i = 0; i < populationSize; i++) {
            inkyBabys.add(gamePopulation.get(i));
            pacmanBabys.add(gamePopulation.get(i));
        }

        // Sort the inky babies by the best inky in each game
        inkyBabys.sort(new InkyFitnessComparator());
        // Sort the pacman babies by their fitnesses
        pacmanBabys.sort(new PacmanFitnessComparator());

        int[] topTenInk = new int[10];
        int[] topTenPac = new int[10];

        for (int i = 0; i < 10; i++) {
            topTenInk[i] = inkyBabys.get(inkyBabys.size() - 1 - i).getBestInkyAverageFitness();
            pacmanBabys.get(pacmanBabys.size() - 1 - i).getPacmanAverageFitness();
        }

        PacmanGame topInky = inkyBabys.get(inkyBabys.size() - 1);
        PacmanGame topPac = pacmanBabys.get(pacmanBabys.size() - 1);

        topInky.getIs().setTopTenFitness(topTenPac, topTenInk);
        topPac.getIs().setTopTenFitness(topTenPac, topTenInk);

        topInky.getIs().initializeNNStorage(topInky.getBestInky().brain.getArrayWeights().length, topInky.getBestInky().brain.getArrayBias().length);
        topPac.getIs().initializeNNStorage(topPac.pacman.brain.getArrayWeights().length, topPac.pacman.brain.getArrayBias().length);

        topInky.getIs().setNNInfo(topInky.getBestInky().brain.getArrayWeights(), topInky.getBestInky().brain.getArrayBias());

        try {
            // Make file records of the best Pacman/Inky
            inkyBabys.get(inkyBabys.size() - 1).saveInformation(topInky.getIs(), oosInk);
            pacmanBabys.get(pacmanBabys.size() - 1).saveInformation(topPac.getIs(), oosPac);

        } catch (Exception ex) {}
        recordFitness();

        // Remove non top pacman/inky babys
        while (inkyBabys.size() > topGhosts) {
            inkyBabys.remove(0);
        }
        while (pacmanBabys.size() > topPacman) {
            pacmanBabys.remove(0);
        }

        // Get a set number of lower scoring ghosts/pacmen to be kept alive
        ArrayList<Integer> randInkys = NetworkTools.randomValues(0, populationSize - 1 - topGhosts, lowerGhosts);
        ArrayList<Integer> randPacmen = NetworkTools.randomValues(0, populationSize - 1 - topPacman, lowerPacman);

        // Add random inkys and pacmen from the lower scoring group
        for (int i = 0; i < randInkys.size(); i++) {
            inkyBabys.add(gamePopulation.get(randInkys.get(i)));
        }

        for (int i = 0; i < randPacmen.size(); i++) {
            pacmanBabys.add(gamePopulation.get(randPacmen.get(i)));
        }

        // Sort inkys and pacmen
        inkyBabys.sort(new InkyFitnessComparator());
        pacmanBabys.sort(new PacmanFitnessComparator());
    }

    public void mutate() {
        // Mutate some inky babies
        for (int i = 0; i < inkyBabys.size(); i++) {
            inkyBabys.get(i).getBestInky().brain.mutate(mutationChance);
        }
        // Mutate some pacman babies
        for (int i = 0; i < pacmanBabys.size(); i++) {
            pacmanBabys.get(i).pacman.brain.mutate(mutationChance);
        }
    }

    private void recordFitness() {

        // Draw the fitnesses on the canvas
        if (inkyBabys.get(inkyBabys.size() - 1).getBestInkyAverageFitness() == 0) {
            gc.setFill(Color.DARKBLUE);
            gc.fillOval(fitnessX, 620, coordinateW, coordinateW);
            gc.fillText("0", fitnessX - coordinateW, 620-15);

        } else {
            gc.setFill(Color.DARKBLUE);
            gc.fillOval(fitnessX, 620 - (inkyBabys.get(inkyBabys.size() - 1).getBestInkyAverageFitness() / 2), coordinateW, coordinateW);
            gc.fillText(Double.toString(inkyBabys.get(inkyBabys.size() - 1).getBestInkyAverageFitness()), fitnessX - coordinateW, 620 - (inkyBabys.get(inkyBabys.size() - 1).getBestInkyAverageFitness() / 2 - 15));
        }

        if (pacmanBabys.get(pacmanBabys.size() - 1).getPacmanAverageFitness() == 0) {
            gc.setFill(Color.YELLOW);
            gc.fillOval(fitnessX, 620, coordinateW, coordinateW);
            gc.fillText("0", fitnessX - coordinateW, 620-15);

        } else {
            gc.setFill(Color.YELLOW);
            gc.fillOval(fitnessX, 620 - (pacmanBabys.get(pacmanBabys.size() - 1).getPacmanAverageFitness() / 2), coordinateW, coordinateW);
            gc.fillText(Double.toString(pacmanBabys.get(pacmanBabys.size() - 1).getPacmanAverageFitness()), fitnessX - coordinateW, 620 - (pacmanBabys.get(pacmanBabys.size() - 1).getPacmanAverageFitness() / 2 - 15));
        }

        fitnessX += 40;

        String inkyFitness = Double.toString(Math.round(inkyBabys.get(inkyBabys.size() - 1).getBestInkyAverageFitness()));
        String pacmanFitness = Double.toString(Math.round(pacmanBabys.get(pacmanBabys.size() - 1).getPacmanAverageFitness()));
        if (inkyFitness != null) inkyFitnessStr += (inkyFitness + "\n");
        if (pacmanFitness != null) pacFitnessStr += (pacmanFitness + "\n");
    }

    public void clear() {
        gamePopulation.clear();
    }

    public void breedPopulation() {

        parents.clear();

        int randNum;
        randNum = random.nextInt(topGhosts + lowerGhosts);
        parents.add(randNum);
        while (parents.size() < topGhosts + lowerGhosts) {
            randNum = random.nextInt(topGhosts + lowerGhosts - 1);
            if (randNum != parents.get(parents.size() - 1)) parents.add(randNum);
        }

        int parentIndx = 0;

        // Inky Breeding
        inkyBrains.clear();
        // Add in top inky brains
        for (int i = 0; i < inkyBabys.size(); i++) {
            inkyBrains.add(inkyBabys.get(i).getBestInky().brain);
        }

        // Chooses random parents to breed to make new ghosts (twice as many because there are two inkies per game)
        while (inkyBrains.size() < populationSize * 2) {

            Collections.shuffle(inkyBabys);

            parent1 = inkyBabys.get(parentIndx).getBestInky().brain;
            parent2 = inkyBabys.get(parentIndx + 1).getBestInky().brain;

            if (parentIndx == (topGhosts + lowerGhosts - 2)) {
                parentIndx = 0;
            }

            inkyBrains.add(parent1.makeChild(parent2));
            inkyBrains.add(parent2.makeChild(parent1));
            parentIndx++;
        }

        // Pacman Breeding
        pacmanBrains.clear();
        // Add in top pacman brains
        for (int i = 0; i < pacmanBabys.size(); i++) {
            pacmanBrains.add(pacmanBabys.get(i).pacman.brain);
        }
        parentIndx = 0;
        // Choose random parents to breed to make new pacmen
        while (pacmanBrains.size() < populationSize) {

            Collections.shuffle(pacmanBabys);

            parent1 = pacmanBabys.get(parentIndx).pacman.brain;
            parent2 = pacmanBabys.get(parentIndx + 1).pacman.brain;

            if (parentIndx == (topPacman + lowerPacman - 2)) {
                parentIndx = 0;
            }

            pacmanBrains.add(parent1.makeChild(parent2));
            parentIndx++;
        }

        ghostIndex = 0;

        // Repopulated game population with new inky and pacman brains starting with 0 fitness
        for (int i = 0; i < populationSize; i++) {
            gamePopulation.add(new PacmanGame(PacmanDataPath, MAXMOVES, inkyBrains.get(ghostIndex), inkyBrains.get(ghostIndex + 1),  pacmanBrains.get(i)));
            ghostIndex += 2;
        }
    }
}
