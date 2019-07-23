package game;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class GeneticAlgorithm {

    ArrayList<PacmanGame> gamePopulation = new ArrayList<>();
    ArrayList<PacmanGame> inkyBabys = new ArrayList<>();
    ArrayList<PacmanGame> pacmanBabys = new ArrayList<>();
    ArrayList<NeuralNetwork> inkyBrains = new ArrayList<>();
    ArrayList<NeuralNetwork> pacmanBrains = new ArrayList<>();
    ArrayList<Inky> inkies = new ArrayList<>();
    ArrayList<Pacman> pacmen = new ArrayList<>();

    int populationSize;
    int totalGens;
    int generation = 0;
    int lowerGhosts;
    int topGhosts;
    int lowerPacman;
    int topPacman;
    double mutationChance;

    Random random = new Random();
    String PacmanDataPath;
    int round = 0;

    private FileWriter pacWriter;
    private String pacFitnessStr;
    private FileWriter inkyWriter;
    private String inkyFitnessStr;

    final int fileNum;
    final int MAXMOVES = 600;
    boolean lastRoundRobin = false;

    public GeneticAlgorithm(String PacmanDataPath, int popSize, int totalGens, double mutationChance, int lowerGhosts, int topGhosts, int lowerPacman, int topPacman) throws IOException {
        this.PacmanDataPath = PacmanDataPath;
        this.populationSize = popSize;
        this.totalGens = totalGens;
        this.mutationChance = mutationChance;
        this.lowerGhosts = lowerGhosts;
        this.topGhosts = topGhosts;
        this.lowerPacman = lowerPacman;
        this.topPacman = topPacman;

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
            inkyWriter = new FileWriter(this.PacmanDataPath + "/inkyFits" + fileNum + ".txt");
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
            pacWriter.write(new Pacman().HIDDEN_TWO + " ");
            pacWriter.write(new Pacman().OUTPUTS + "\n");
            inkyWriter.write(new Inky().INPUTS + " ");
            inkyWriter.write(new Inky().HIDDEN_ONE + " ");
            inkyWriter.write(new Inky().HIDDEN_TWO + " ");
            inkyWriter.write(new Inky().OUTPUTS + "\n");

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

        makePopulation();
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
            inkies.add(game.inky);
            inkies.add(game.inkyTwo);
            pacmen.add(game.pacman);
        }

        // Shuffle around pacmen to hit new inky ghosts
        Collections.shuffle(pacmen);
        Collections.shuffle(inkies);

        if (!lastRoundRobin) gamePopulation.clear();

        int ghostIndx = 0;

        for (int i = 0; i < populationSize; i++) {
            // Put all of the inkies and pacmen back into new games with different opponents
            gamePopulation.add(new PacmanGame(PacmanDataPath, MAXMOVES, inkies.get(ghostIndx).brain, inkies.get(ghostIndx).fitness, inkies.get(ghostIndx + 1).brain, inkies.get(ghostIndx + 1).fitness, pacmen.get(i).brain, pacmen.get(i).fitness));
            ghostIndx += 2;
        }
    }

    public void sort() {

        inkyBabys.clear();
        pacmanBabys.clear();

        // Add all inkys and pacmen to the baby arrays
        for (int i = 0; i < populationSize; i++) {
            inkyBabys.add(gamePopulation.get(i));
            pacmanBabys.add(gamePopulation.get(i));
        }

        // Sort the inky babies by the best inky in each game
        inkyBabys.sort(new InkyFitnessComparator());
        // Sort the pacman babies by their fitnesses
        pacmanBabys.sort(new PacmanFitnessComparator());

        // Make file records of the best Pacman/Inky
        inkyBabys.get(inkyBabys.size() - 1).writeFileContent(generation, fileNum, false);
        pacmanBabys.get(pacmanBabys.size() - 1).writeFileContent(generation, fileNum, true);

        // Print and Record the best fitness
        System.out.println("I Gen " + generation + " fitness " + inkyBabys.get(inkyBabys.size() - 1).getBestInky().fitness);
        System.out.println("P Gen " + generation + " fitness " + pacmanBabys.get(pacmanBabys.size() - 1).getPacmanFitness());
        System.out.println();
        String inkyFitness = Double.toString(Math.round(inkyBabys.get(inkyBabys.size() - 1).getBestInky().fitness));
        String pacmanFitness = Double.toString(Math.round(pacmanBabys.get(pacmanBabys.size() - 1).getPacmanFitness()));
        if (inkyFitness != null) inkyFitnessStr += (inkyFitness + "\n");
        if (pacmanFitness != null) pacFitnessStr += (pacmanFitness + "\n");

        // Remove non top pacman/inky babys
        while(inkyBabys.size() > topGhosts) {
            inkyBabys.remove(0);
        }
        while (pacmanBabys.size() > topPacman) {
            pacmanBabys.remove(0);
        }

        // Get _ lower scoring ghosts/pacmen to be bred
        ArrayList<Integer> randInkys = NetworkTools.randomValues(0, populationSize - 1 - topGhosts, lowerGhosts);
        ArrayList<Integer> randPacmen = NetworkTools.randomValues(0, populationSize - 1 - topPacman, lowerPacman);

        // Add random inkys and pacmen
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

    public void clear() {
        gamePopulation.clear();
    }

    public void breedPopulation() {

        ArrayList<Integer> parents = new ArrayList<>();
        int randNum;
        randNum = random.nextInt(topGhosts + lowerGhosts - 1);
        parents.add(randNum);
        while (parents.size() < topGhosts + lowerGhosts) {
            randNum = random.nextInt(topGhosts + lowerGhosts - 1);
            if (randNum != parents.get(parents.size() - 1)) parents.add(randNum);
        }

        NeuralNetwork parent1;
        NeuralNetwork parent2;
        int parentIndx = 0;

        // Inky Breeding
        inkyBrains.clear();
        // Add in top inky brains
        for (int i = 0; i < inkyBabys.size(); i++) {
            inkyBrains.add(inkyBabys.get(i).getBestInky().brain);
        }
        // Chooses random parents to breed to make new ghosts
        while (inkyBrains.size() < populationSize*2) {

            parent1 = inkyBabys.get(parents.get(parentIndx)).getBestInky().brain;
            parent2 = inkyBabys.get(parents.get(parentIndx + 1)).getBestInky().brain;
            if (parentIndx == (topGhosts + lowerGhosts - 2)) {
                parentIndx = 0;
            }
            inkyBrains.add(parent1.makeChild(parent2));
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
            parent1 = pacmanBabys.get(parents.get(parentIndx)).pacman.brain;
            parent2 = pacmanBabys.get(parents.get(parentIndx + 1)).pacman.brain;
            if (parentIndx == (topPacman + lowerPacman - 2)) {
                parentIndx = 0;
            }
            pacmanBrains.add(parent1.makeChild(parent2));
            parentIndx++;
        }

        int ghostIndex = 0;

        // Repopulated game population with new inky and pacman brains starting with 0 fitness
        for (int i = 0; i < populationSize; i++) {
            gamePopulation.add(new PacmanGame(PacmanDataPath, MAXMOVES, inkyBrains.get(ghostIndex), 0, inkyBrains.get(ghostIndex + 1), 0,  pacmanBrains.get(i), 0));
            ghostIndex += 2;
        }

    }
}
