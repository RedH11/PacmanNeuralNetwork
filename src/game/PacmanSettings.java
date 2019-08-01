package game;

import game.NEAT.*;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class PacmanSettings extends Pane {

    int GHOST_INPUTS = 6;

    private Alert invalAlert = new Alert(Alert.AlertType.INFORMATION, null);
    String PacmanDataPath;
    VisualGame vg = null;

    Counter nodeInnovation = new Counter();
    Counter connectionInnovation = new Counter();

    //Genome genome = new Genome(6);
    Genome genome = new Genome(GHOST_INPUTS);

    public PacmanSettings( String PacmanDataPath, GraphicsContext gameGC) {
        this.PacmanDataPath = PacmanDataPath;

        invalAlert.setTitle("Error Warning");
        setupGenome();

        Label gameExplanation = new Label("Welcome to ThInky! \nChoose your population amount and amount of generations you want to train them for to see the " +
                "results! (Tip: More Generations = Longer time but better ghosts)\n To View Generation Progress input the generation " +
                "you want to view after your training has completed");
        gameExplanation.setPrefWidth(500);
        gameExplanation.setWrapText(true);

        Label neatExplanation = new Label("What is ThInky though?! Great question! In Pacman the Inky ghost is programmed " +
                "to go in random directions when they get too close to pacman making it the least effective ghost" +
                ", so we decided that we were going to give Inky the best brain possible (aka A Thinky Inky) using a NEAT" +
                " algorithm. A Neural Evolution Augmented Topologies is a neural network that evolves through creating" +
                " new nodes and connections to find the optimal structure to solve a given problem");
        neatExplanation.setPrefWidth(500);
        neatExplanation.setWrapText(true);
        VBox settings = new VBox();
        settings.setPadding(new Insets(5, 10, 5, 10));
        settings.setSpacing(10);
        settings.setPrefWidth(250);
        VBox viewer = new VBox();
        viewer.setPadding(new Insets(5, 10, 5, 10));
        viewer.setSpacing(10);
        viewer.setPrefWidth(250);
        // SETTINGS

        Label evolLabel = new Label("Evolution Settings");

        Label popLbl = new Label("Population");
        TextField popTF = new TextField("400");
        popTF.setPromptText("Population Size");

        Label genLbl = new Label("Generations");
        TextField gensTF = new TextField("100");
        gensTF.setPromptText("Total Generations");

        Button evolve = new Button("Evolve");
        evolve.setOnAction(ev -> {
            Thread tests = new Thread(() -> {
                GA ga = new GA(PacmanDataPath, Integer.parseInt(popTF.getText()), Integer.parseInt(gensTF.getText()), genome, GHOST_INPUTS);
                ga.evolveGhosts();
            });
            tests.start();
            try {
                tests.join();
            } catch (InterruptedException ex){}
        });

        // VIEWER
        Label viewLabel = new Label("Evolution Settings");

        Label gameNumLbl = new Label("Game Number");

        TextField gameNum = new TextField();
        gameNum.setPromptText("Most Recent Game");
        gameNum.setDisable(true);

        // Gets amount of files in the folder already
        File folder = new File(PacmanDataPath);
        File[] listOfFiles = folder.listFiles();
        ArrayList<File> fileList = new ArrayList<>();

        // Starts at one to skip DS.Store file
        for (int i = 1; i < listOfFiles.length; i++) {
            fileList.add(listOfFiles[i]);
        }

        Label gameGenLbl = new Label("Ghost Generation");
        TextField genNum = new TextField();
        genNum.setPromptText("Insert Generation Number");

        Button showGame = new Button("Show Game");
        showGame.setOnAction(ev -> {
            // If text is entered
            if (genNum.getText().length() > 0) {
                if (vg != null) vg.stop();
                // Gets amount of files in the folder already
                File folder2 = new File(PacmanDataPath);
                File[] listOfFiles2 = folder2.listFiles();
                Thread showing = new Thread(() -> {
                    try {
                        int fileAmount = listOfFiles2.length;
                        if (listOfFiles2[0].getName().contains("DS")) fileAmount--;
                        GhostInfoStorage is = parseFile(fileAmount, Integer.parseInt(genNum.getText()) - 1);
                        vg = new VisualGame(gameGC, is, Integer.parseInt(genNum.getText()) - 1, 10);
                    } catch (Exception ex) {
                        // Avoid throwing IllegalStateException by running from a non-JavaFX thread.
                        Platform.runLater(() -> {
                            invalAlert.setTitle("Error Evolving" + ex);
                            ex.printStackTrace();
                            invalAlert.show();
                        });
                    }
                });
                showing.start();
            }
        });

        VBox root = new VBox();

        root.setPadding(new Insets(0, 5, 0, 5));

        BorderPane pane = new BorderPane();

        settings.getChildren().addAll(evolLabel, popLbl, popTF, genLbl, gensTF,  evolve);
        viewer.getChildren().addAll(viewLabel, gameNumLbl, gameNum, gameGenLbl, genNum, showGame);
        pane.setLeft(settings);
        pane.setRight(viewer);

        root.getChildren().addAll(gameExplanation, pane, neatExplanation);
        root.setSpacing(20);

        getChildren().add(root);
    }

    // Setting up the start genome for the NEAT evolution
    public void setupGenome() {
        Random random = new Random();

        // Input layer and single output node
        int[] inputNodes = new int[GHOST_INPUTS];
        int outputNode;

        // Add the input nodes to the genome as well as giving them unique innovation numbers
        for (int i = 0; i < inputNodes.length; i++) {
            inputNodes[i] = nodeInnovation.getInnovation();
            genome.addNodeGene(new NodeGene(NodeGene.TYPE.INPUT, inputNodes[i]));
        }

        // Add the output node to the genome
        outputNode = nodeInnovation.getInnovation();
        genome.addNodeGene(new NodeGene(NodeGene.TYPE.OUTPUT, outputNode));

        // Create two random connections from an input node to the output
        int c1 = connectionInnovation.getInnovation();
        int c2 = connectionInnovation.getInnovation();

        int randNodeOne = random.nextInt(inputNodes.length);
        int randNodeTwo = random.nextInt(inputNodes.length);

        while (randNodeOne == randNodeTwo) randNodeTwo = random.nextInt(inputNodes.length);

        genome.addConnectionGene(new ConnectionGene(inputNodes[randNodeOne], outputNode, 0.5, true, c1));
        genome.addConnectionGene(new ConnectionGene(inputNodes[randNodeTwo], outputNode, 0.5, true, c2));
    }

    private static ArrayList<GhostInfoStorage> readObjectsFromFile(String filename) throws IOException, ClassNotFoundException {
        ArrayList<GhostInfoStorage> objects = new ArrayList<>();
        InputStream is = null;
        try {
            is = new FileInputStream(filename);
            ObjectInputStream ois = new ObjectInputStream(is);
            while (true) {
                try {
                    GhostInfoStorage object = (GhostInfoStorage) ois.readObject();
                    objects.add(object);
                } catch (EOFException ex) {
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


    public GhostInfoStorage parseFile(int gameNum, int generationNum) {
        String gameFile = "";

        // Viewing Setup
        gameFile = PacmanDataPath + "/Game" +  gameNum + "/Gens/GhostGens";

        try {
            ArrayList<GhostInfoStorage> allGames = readObjectsFromFile(gameFile);
            return allGames.get(generationNum);
        } catch (Exception ex) {
            System.out.println("FILE NOT FOUND");
        }
        return null;
    }
}
