package game;

import game.NEAT.*;
import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class PacmanSettings extends Pane {

    private Alert invalAlert = new Alert(Alert.AlertType.INFORMATION, null);
    String PacmanDataPath;
    VisualGame vg = null;

    Counter nodeInnovation = new Counter();
    Counter connectionInnovation = new Counter();

    Genome genome = new Genome(6);

    public PacmanSettings( String PacmanDataPath, GraphicsContext gameGC) {
        this.PacmanDataPath = PacmanDataPath;

        setupGenome();

        // The canvas for the pacman game
        Canvas gameCanvas = new Canvas(950, 620);
        // Hide it at first
        GraphicsContext gc = gameCanvas.getGraphicsContext2D();

        gc.setFill(Color.rgb(211, 211,211));
        gc.fillRect(0, 0, gameCanvas.getWidth(), gameCanvas.getHeight());

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
                GA ga = new GA(PacmanDataPath, Integer.parseInt(popTF.getText()), Integer.parseInt(gensTF.getText()), genome);
                ga.evolveGhosts();
            });
            tests.start();
            try {
                tests.join();
            } catch (InterruptedException ex){}
        });

        Button clearDisplay = new Button("Clear");
        clearDisplay.setOnAction( event -> {
            gc.clearRect(0, 0, gameCanvas.getWidth(), gameCanvas.getHeight());
            gc.setFill(Color.rgb(211, 211,211));
            gc.fillRect(0, 0, gameCanvas.getWidth(), gameCanvas.getHeight());
        });

        // VIEWER
        Label viewLabel = new Label("Evolution Settings");

        TextField gameNum = new TextField();
        gameNum.setPromptText("Insert Game Number");

        Label possibleGames = new Label("Options:\n");
        // Gets amount of files in the folder already
        File folder = new File(PacmanDataPath);
        File[] listOfFiles = folder.listFiles();
        ArrayList<File> fileList = new ArrayList<>();

        // Starts at one to skip DS.Store file
        for (int i = 1; i < listOfFiles.length; i++) {
            fileList.add(listOfFiles[i]);
        }

        Collections.sort(fileList, new Comparator<File>() {
            @Override
            public int compare(File s1, File s2) {
                int letter = Integer.parseInt(s1.getName().substring(4)) - Integer.parseInt(s2.getName().substring(4));
                if (letter != 0) return letter;
                else return 0;
            }
        });

        for (int i = 1; i < fileList.size() - 1; i++) {
            possibleGames.setText(possibleGames.getText() + fileList.get(i).getName() + ", ");
        }

        if (fileList.size() > 0) {
            possibleGames.setText(possibleGames.getText() + fileList.get(fileList.size() - 1).getName());
            possibleGames.setWrapText(true);
            possibleGames.setPrefWidth(175);
        }

        TextField genNum = new TextField();
        genNum.setPromptText("Insert Generation Number");

        Button gameType = new Button("Pacman");
        gameType.setOnAction(ev -> {
            if (gameType.getText().contains("Pacman")) gameType.setText("Inky");
            else gameType.setText("Pacman");
        });

        Button showGame = new Button("Show Game");
        showGame.setOnAction(ev -> {
            Thread showing = new Thread(() -> {
                try {
                    if (vg != null) vg.stop();
                    boolean pacmanGame = gameType.getText().contains("Pacman");
                    InfoStorage is = parseFile(Integer.parseInt(gameNum.getText()), Integer.parseInt(genNum.getText()), pacmanGame);
                    vg = new VisualGame(gameGC, is, Integer.parseInt(genNum.getText()), 5);
                } catch (Exception ex) {}
            });
            showing.start();
        });

        BorderPane root = new BorderPane();

        settings.getChildren().addAll(evolLabel, popLbl, popTF, genLbl, gensTF,  evolve, clearDisplay);
        viewer.getChildren().addAll(viewLabel, possibleGames, gameNum, genNum, gameType, showGame);
        root.setCenter(gameCanvas);
        root.setLeft(settings);
        root.setRight(viewer);

        getChildren().add(root);
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


    public InfoStorage parseFile(int gameNum, int generationNum, boolean pacman) {
        String gameFile = "";

        // Viewing Setup
        if (pacman) gameFile = PacmanDataPath + "/Game" +  gameNum + "/Gens/PacGens";
        else gameFile = PacmanDataPath + "/Game" +  gameNum + "/Gens/InkGens";

        try {
            ArrayList<InfoStorage> allGames = readObjectsFromFile(gameFile);

            return allGames.get(generationNum);

        } catch (Exception ex) {
            invalAlert.setContentText("File Not Found");
            invalAlert.show();
        }
        return null;
    }

    // Setting up the start genome for the NEAT evolution
    public void setupGenome() {
        Random random = new Random();

        // Input layer and single output node
        int[] inputNodes = new int[6];
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
}
