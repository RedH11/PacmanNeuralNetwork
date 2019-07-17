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
    int generation;

    public GeneticAlgorithm(int popSize, Stage stage) {
        this.popSize = popSize;
        this.stage = stage;

        makePopulation();
    }

    // Make a fresh population
    public void makePopulation() {
        for (int i = 0; i < popSize; i++) {
            pop.add(new GameArray());
        }
    }

    public void breedPopulation() {

    }

    public void makeChild(NeuralNetwork parent1, NeuralNetwork parent2) {

    }

    public void sortPopulation() {
        // Sort all inkys by fitness (left worst -> right best)
        Collections.sort(babyInkys, new IncomComparator());
    }
}
