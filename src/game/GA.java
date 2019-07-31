package game;

import game.NEAT.*;

import java.io.*;
import java.util.*;

/* Genetic Algorithm to Train the Ghosts using a NEAT Framework modified for this project */
public class GA {

    final int MAX_MOVES = 400;

    Random random = new Random();
    private FitnessGenomeComparator fitComp = new FitnessGenomeComparator();

    private String PacmanDataPath;
    private int populationSize;
    private int totalGens;
    private int currentGen = 0;

    private Counter nodeInnovation = new Counter();
    private Counter connectionInnovation = new Counter();
    private List<Species> species = new ArrayList<Species>();
    private List<PacmanGame> games = new ArrayList<PacmanGame>();
    private List<Genome> genomes;
    private List<Genome> nextGenGenomes;
    private double highestScore;
    private Genome fittestGenome;
    private Genome startingGenome;
    private Map<Genome, Species> mappedSpecies;
    private Map<Genome, Double> scoreMap;

    private OutputStream opsGhost;
    private ObjectOutputStream oosGhost = null;

    private ArrayList<NeuralNetwork> pacmanBrains = new ArrayList<>();

    // Adjustable Training Settings
    private double C1 = 1.0;
    private double C2 = 1.0;
    private double C3 = 0.4;
    private final double SPECIES_THRESHOLD = 10;

    // If adjusted must also adjust in Genome 
    private final double WEIGHT_MUTATION_RATE = 0.8;
    private final double ADJUST_WEIGHT_RATE = 0.9; // 90% chance of adjusting node weight, else replaces weight with random one
    private final double ADD_CONNECTION_RATE = 0.05;
    private final double ADD_NODE_RATE = 0.03;

    final int INPUTS = 16;
    final int HIDDEN_ONE = 40;
    final int OUTPUTS = 2;

    public GA(String PacmanDataPath, int populationSize, int totalGens, Genome startingGenome) {
        this.PacmanDataPath = PacmanDataPath;
        this.populationSize = populationSize;
        this.totalGens = totalGens;
        this.startingGenome = startingGenome;

        genomes = new ArrayList<Genome>(populationSize);
        nextGenGenomes = new ArrayList<Genome>(populationSize);
        nextGenGenomes = new ArrayList<Genome>(populationSize);
        mappedSpecies = new HashMap<Genome, Species>();
        scoreMap = new HashMap<Genome, Double>();

        constructPacmanBrains();
        constructFileWriters();
    }

    private void constructFileWriters() {

        // Gets amount of files int he folder already
        File folder = new File(PacmanDataPath);
        File[] listOfFiles = folder.listFiles();

        // Make file to hold the Game Data
        int gameNum = listOfFiles.length;
        new File(PacmanDataPath + "/Game" + gameNum).mkdir();

        // Make new file to store the generation game tracker
        new File(PacmanDataPath + "/Game" + gameNum + "/Gens").mkdir();

        try {
            opsGhost = new FileOutputStream(PacmanDataPath + "/Game" +  gameNum + "/Gens/GhostGens");
            oosGhost = new ObjectOutputStream(opsGhost);
        } catch (Exception ex) {
            System.out.println("File Not Found for Object Writer");
        }
    }

    // Create 20 Pacman Brains from already trained pacman
    private void constructPacmanBrains() {

        File folder = new File(PacmanDataPath + "/PacBrains");
        File[] brainFiles = folder.listFiles();

        ArrayList<InfoStorage> brains = new ArrayList<>();

        for (int i = 0; i < brainFiles.length - 1; i++) {
            try {
                brains.add(parseFile(i, 99, true));
            } catch (Exception ex) {
                System.out.println("Pacman Brains File Not Found " + ex);
            }
            pacmanBrains.add(new NeuralNetwork(INPUTS, HIDDEN_ONE, OUTPUTS));
            pacmanBrains.get(i).setWeights(brains.get(i).getWeights());
            pacmanBrains.get(i).setBias(brains.get(i).getBiases());
        }
    }

    public void evolveGhosts() {
        while (currentGen < totalGens) {
            clear();
            if (currentGen == 0) makePopulation();
            speciate();
            test();
            sort();
            breed();
            currentGen++;
        }

        try {
            oosGhost.close();
        } catch (IOException ex) {
            System.out.println("Error Closing Ghost Info Writer");
        }
    }

    private void makePopulation() {
        for (int i = 0; i < populationSize; i++) {
            genomes.add(new Genome(startingGenome, INPUTS));
        }
    }

    private void clear() {
        // Reset everything for next generation
        for (Species s : species) {
            s.reset(random);
        }
        games.clear();
        scoreMap.clear();
        mappedSpecies.clear();
        nextGenGenomes.clear();
        highestScore = Double.MIN_VALUE;
        fittestGenome = null;
    }

    private void speciate() {
        // Place genomes into species
        for (Genome g : genomes) {
            boolean foundSpecies = false;
            for (Species s : species) {
                if (Genome.compatibilityDistance(g, s.mascot, C1, C2, C3) < SPECIES_THRESHOLD) { // compatibility distance is less than DT, so genome belongs to this species
                    s.members.add(g);
                    mappedSpecies.put(g, s);
                    foundSpecies = true;
                    break;
                }
            }
            if (!foundSpecies) { // if there is no appropiate species for genome, make a new one
                Species newSpecies = new Species(g);
                species.add(newSpecies);
                mappedSpecies.put(g, newSpecies);
            }
        }

        // Remove unused species
        Iterator<Species> iter = species.iterator();
        while(iter.hasNext()) {
            Species s = iter.next();
            if (s.members.isEmpty()) {
                iter.remove();
            }
        }
    }

    // Note: Population has to be an even number for testing two ghosts per game
    private void test() {
        int gameIndx = 0;
        int bestGhostIndex = 0;
        int ghostIndx = 0;
        // Evaluate two genomes per game and assign score
        for (int i = 0; i < genomes.size()/2; i++) {
            int randomPacmanIndex = random.nextInt(pacmanBrains.size());

            Genome g1 = genomes.get(ghostIndx);
            Genome g2 = genomes.get(ghostIndx + 1);

            Species s1 = mappedSpecies.get(g1);		// Get species of the first genome
            Species s2 = mappedSpecies.get(g2);		// Get species of the second genome

            // Put a random pacman brain in the game that simulates two ghosts at a time
            games.add(new PacmanGame(PacmanDataPath, MAX_MOVES, pacmanBrains.get(randomPacmanIndex), g1, g2, INPUTS));
            games.get(i).simulateGame();

            double score1 = games.get(gameIndx).ghostOne.fitness;
            double score2 = games.get(gameIndx).ghostTwo.fitness;

            // Score is adjusted to make sure that dominant species don't take up too much of the population
            double adjustedScore1 = score1 / mappedSpecies.get(g1).members.size();
            double adjustedScore2 = score2 / mappedSpecies.get(g2).members.size();

            s1.addAdjustedFitness(adjustedScore1);
            s1.fitnessPop.add(new FitnessGenome(g1, adjustedScore1));
            s2.addAdjustedFitness(adjustedScore2);
            s2.fitnessPop.add(new FitnessGenome(g2, adjustedScore2));
            scoreMap.put(g1, adjustedScore1);
            scoreMap.put(g2, adjustedScore2);

            // Find the fittest genome in a population
            if (score1 > highestScore) {
                highestScore = score1;
                fittestGenome = g1;
                bestGhostIndex = i;
            }

            if (score2 > highestScore) {
                highestScore = score2;
                fittestGenome = g2;
                bestGhostIndex = i;
            }

            ghostIndx += 2;
        }

        System.out.println("Gen " + (currentGen + 1) + " Fitness " + highestScore);

        PacmanGame bestGhost = games.get(bestGhostIndex);
        try {
            // Store the amount of layers used for displaying the neural network in the visual display
            bestGhost.getIs().setLayerSizes(bestGhost.getBestGhost().brain.countLayers());
            // Store the nodes and connections of the best ghost to show the Neural Network
            bestGhost.getIs().setGenomeInfo(bestGhost.getBestGhost().brain.getNodes(), bestGhost.getBestGhost().brain.getConnections());
            // Save the information stored in the Ghost Info Storage to the file
            bestGhost.saveInformation(games.get(bestGhostIndex).getIs(), oosGhost);
        } catch (Exception ex) {
            System.out.println("Error saving game data " + ex);
            ex.printStackTrace();
        }
    }

    private void sort() {
        // put best genomes from each species into next generation
        for (Species s : species) {
            Collections.sort(s.fitnessPop, fitComp);
            Collections.reverse(s.fitnessPop);
            FitnessGenome fittestInSpecies = s.fitnessPop.get(0);
            nextGenGenomes.add(fittestInSpecies.genome);
        }
    }

    // Breeds and mutates some of the population
    private void breed() {
        // Breed the rest of the genomes
        while (nextGenGenomes.size() < populationSize) { // replace removed genomes by randomly breeding
            Species s = getRandomSpeciesBiasedAjdustedFitness(random);

            Genome p1 = getRandomGenomeBiasedAdjustedFitness(s, random);
            Genome p2 = getRandomGenomeBiasedAdjustedFitness(s, random);

            Genome child;
            if (scoreMap.get(p1) >= scoreMap.get(p2)) {
                child = Genome.crossover(p1, p2, random);
            } else {
                child = Genome.crossover(p2, p1, random);
            }
            if (random.nextDouble() < WEIGHT_MUTATION_RATE) {
                child.mutation(random);
            }
            if (random.nextDouble() < ADD_CONNECTION_RATE) {
                //System.out.println("Adding connection mutation...");
                child.addConnectionMutation(random, connectionInnovation, 10);
            }
            if (random.nextDouble() < ADD_NODE_RATE) {
                //System.out.println("Adding node mutation...");
                child.addNodeMutation(random, connectionInnovation, nodeInnovation);
            }
            nextGenGenomes.add(child);
        }

        genomes = nextGenGenomes;
        nextGenGenomes = new ArrayList<Genome>();
    }

    //Selects a random species from the species list, where species with a higher total adjusted fitness have a higher chance of being selected
    private Species getRandomSpeciesBiasedAjdustedFitness(Random random) {
        double completeWeight = 0.0;	// sum of probablities of selecting each species - selection is more probable for species with higher fitness
        for (Species s : species) {
            completeWeight += s.totalAdjustedFitness;
        }
        double r = Math.random() * completeWeight;
        double countWeight = 0.0;
        for (Species s : species) {
            countWeight += s.totalAdjustedFitness;
            if (countWeight >= r) {
                return s;
            }
        }
        throw new RuntimeException("Couldn't find a species... Number is species in total is "+species.size()+", and the total adjusted fitness is "+completeWeight);
    }


    //Selects a random genome from the species chosen, where genomes with a higher adjusted fitness have a higher chance of being selected
    private Genome getRandomGenomeBiasedAdjustedFitness(Species selectFrom, Random random) {
        double completeWeight = 0.0;	// sum of probablities of selecting each genome - selection is more probable for genomes with higher fitness
        for (FitnessGenome fg : selectFrom.fitnessPop) {
            completeWeight += fg.fitness;
        }
        double r = Math.random() * completeWeight;
        double countWeight = 0.0;
        for (FitnessGenome fg : selectFrom.fitnessPop) {
            countWeight += fg.fitness;
            if (countWeight >= r) {
                return fg.genome;
            }
        }
        throw new RuntimeException("Couldn't find a genome... Number is genomes in selected species is " + selectFrom.fitnessPop.size() + ", and the total adjusted fitness is "+completeWeight);
    }

    private static ArrayList<InfoStorage> readObjectsFromFile(String filename) throws IOException, ClassNotFoundException {
        ArrayList<InfoStorage> objects = new ArrayList<>();
        InputStream is = null;
        try {
            is = new FileInputStream(filename);
            ObjectInputStream ois = new ObjectInputStream(is);
            while (true) {
                try {
                    InfoStorage object = (InfoStorage) ois.readObject();
                    objects.add(object);
                } catch (Exception ex) {
                    break;
                }
            }
        } finally {
            if (is != null) {
                is.close();
            }
        }
        return objects;
    }

    public InfoStorage parseFile(int gameNum, int generationNum, boolean pacman) {
        String gameFile = "";

        // Viewing Setup
        if (pacman) gameFile = PacmanDataPath + "/PacBrains/Game" +  gameNum + "/Gens/PacGens";
        else gameFile = PacmanDataPath + "/Game" +  gameNum + "/Gens/PacGens";

        try {
            ArrayList<InfoStorage> allGames = readObjectsFromFile(gameFile);
            return allGames.get(allGames.size() - 1);

        } catch (Exception ex) {
            System.out.println("File Not Found");
        }
        return null;
    }
}

class FitnessGenomeComparator implements Comparator<FitnessGenome> {

    @Override
    public int compare(FitnessGenome one, FitnessGenome two) {
        if (one.fitness > two.fitness) {
            return 1;
        } else if (one.fitness < two.fitness) {
            return -1;
        }
        return 0;
    }

}

// Details of NEAT

// 25% of Genomes are cloned (based on exponential distribution)
// 75% are crossed over

// Genes are crossed over by taking two parents with different genes
//ex:
// (1) 1->4 / (2) 2 -> 4 / (6) 3 -> 4 /             (8) 1 -> 5
//(1) 1->4 / (2) 2 -> 4 / (6) 3 -> 4 / (7) 5 -> 6 /             (9) 3 -> 5
// 50% chance of either gene         // Disjointed genes of       Excess genes are only saved if the
// for matching ones (1,2,3)         fitter parent are saved (7,8) second parent has a higher fitness


// Types of mutation
// Change gene weight (80%)
// Adjust current weight (90%) +/- small number
// New random weight (10%) -2 -> 2
// Disable / Enable genes too
// 5% of adding a gene
// 3% of adding a node (new node splits a connection and becomes connected to both)
// The weight from the node backwards is 1 while the weight onwards is the same as before

// Speciation
// Calculate the similarity between genomes and if they are in the same species test them against each other
// Distance = (c1 * Excess) / Num of genes in structure + (c2 * Disjointed) / Num of genes in structure + c3 * (Avg Weight Difference)
// The division accounts for the fact that adding neurons to small NN chains is a big difference compared to adding another neuron to a huge one

// Constant population is kept across the whole species (better species get more species)
// Species can be killed (culled) by having them being stagnated where after N gens if there is no improvement they will die
// Or their population is too low


