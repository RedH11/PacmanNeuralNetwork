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
    int generation = 1;
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
            if (generation > 1) recreatePopulation();
            testInkys();
            sortPopulation();
            mutatePopulation();
            breedPopulation();
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

        ArrayList<Integer> parents = NetworkTools.randomValues(0, topGhosts + lowerGhosts, popSize - children.size());

        // Breeds the ghosts based on the parents indexes
        for (int i = 0; i < popSize - children.size(); i++) {

            parent1 = children.get(parents.get(i)).inkyGhost.getBrain();
            parent2 = children.get(parents.get(i + 1)).inkyGhost.getBrain();

            // Add the newly bred child to the children array (won't be selected because of the parents array boundary)
            children.add(new GameArray(parent1.makeChild(parent2)));
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
