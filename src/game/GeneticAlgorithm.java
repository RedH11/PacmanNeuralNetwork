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
    ArrayList<InkyGhost> babyInkys = new ArrayList<>();

    int popSize;
    int totalGens;
    int generation;

    double mutationChance;

    Random random = new Random();

    public GeneticAlgorithm(int popSize, int totalGens, double mutationChance, Stage stage) {
        this.popSize = popSize;
        this.stage = stage;
        this.totalGens = totalGens;
        this.mutationChance = mutationChance;

        makePopulation();
    }

    public void makeGenerations() {

        while (generation < totalGens) {
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

    public void mutatePopulation() {
        System.out.println("Captain Mutato");
    }

    public void breedPopulation() {
        int inkysNeeded = popSize - babyInkys.size();
    }

    public void makeChild(NeuralNetwork parent1, NeuralNetwork parent2) {

    }

    public void sortPopulation() {

        // Sort all inkys by fitness (left worst -> right best)
        // Collections.sort(babyInkys, new IncomComparator());
        babyInkys.sort(new IncomComparator());



        Integer[] rand = NetworkTools.randomValues(0, 89, 5);
        InkyGhost ranOne = babyInkys.get(rand[0]);
        InkyGhost ranTwo = babyInkys.get(rand[1]);
        InkyGhost ranThree = babyInkys.get(rand[2]);
        InkyGhost ranFour = babyInkys.get(rand[3]);
        InkyGhost ranFive = babyInkys.get(rand[4]);

        babyInkys.subList(0,89).clear();

        babyInkys.add(ranOne);
        babyInkys.add(ranTwo);
        babyInkys.add(ranThree);
        babyInkys.add(ranFour);
        babyInkys.add(ranFive);

        babyInkys.sort(new IncomComparator());
    }
}
