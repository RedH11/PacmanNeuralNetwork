package game;

import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class GeneticAlgorithm {

    Stage stage;

    ArrayList<GameArray> pop = new ArrayList<>();
    ArrayList<GameArray> children = new ArrayList<>();

    int popSize;
    int totalGens;
    int generation = 0;
    int lowerGhosts;
    int topGhosts;

    double mutationChance;

    Random random = new Random();

    public GeneticAlgorithm(int popSize, int totalGens, double mutationChance, int lowerGhosts, int topGhosts, Stage stage) {
        this.popSize = popSize;
        this.stage = stage;
        this.totalGens = totalGens;
        this.mutationChance = mutationChance;
        this.lowerGhosts = lowerGhosts;
        this.topGhosts = topGhosts;

        makePopulation();
    }

    public void makeGenerations() {

        while (generation < totalGens) {
            //System.out.println("New Gen");
            if (generation > 0) {
                recreatePopulation();
                System.out.println("Recreated");
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
    }

    public void testInkys() {
        for (GameArray game : pop) {
            game.runGame();
        }
    }

    // Make a fresh population
    public void makePopulation() {
        for (int i = 0; i < popSize; i++) {
            pop.add(new GameArray());
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

            if (parentIndx == 13) {
                parentIndx = 0;
            }
            // Add the newly bred child to the children array (won't be selected because of the parents array boundary)
            children.add(new GameArray(parent1.makeChild(parent2)));
            parentIndx++;
        }
    }

    public void sortPopulation() {

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
    }
}
