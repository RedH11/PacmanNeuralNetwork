package game;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class PacmanUI {
    int stageW = 550;
    int stageH = 620;
    int currDir = 0;

    final int MAXMOVES = 60;

    int[][] pacCoords = new int[MAXMOVES][2];
    int[][] gCoords = new int[MAXMOVES][2];
    int[][] g2Coords = new int[MAXMOVES][2];
    int[] pDirs = new int[MAXMOVES];
    int[] pFits = new int[MAXMOVES];
    boolean[] pPowered = new boolean[MAXMOVES];

    String gameFile = "";
    String PacmanDataPath;
    GraphicsContext gc;

    int popSize = 600;
    int genNumber;
    double mutationChance = 0.5;
    int lowerPacmen = 4;
    int upperPacmen = 20;
    // Amount of genetic algorithms run
    int podAmount = 1;

    private JPanel PacmanTextPanel;
    private JTextPane PacmanText;
    private JRadioButton gen20;
    private JRadioButton gen100;
    private JRadioButton gen200;
    private JButton playButton;

    public PacmanUI() {

        //Run game with 20 generations ghost when clicked
        gen20.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                genNumber = 20;

            }
        });
        //Run game with 100 generations ghost when clicked
        gen100.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                genNumber = 100;

            }
        });
        //Run game with 200 generations ghost when clicked
        gen200.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                genNumber = 200;

            }
        });
        //Start game
        playButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                // Get latest game & generation
                int gameNum = 54;
                int generationNum = genNumber;
                try {
                    GeneticAlgorithm game = new GeneticAlgorithm(PacmanDataPath, popSize, generationNum, mutationChance, lowerPacmen, upperPacmen);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                gameFile = PacmanDataPath + "/Game" +  gameNum + "/Gens/PacGen_" + generationNum;



            }
        });
    }
    public void parseFile(String fileName) throws FileNotFoundException {
        String str = "";

        int coordsCollected = 0;

        File file = new File(fileName);
        Scanner sc = new Scanner(file);

        while (sc.hasNextLine()) {
            str = sc.nextLine();
            if (str.length() > 0) {
                // Trim off P:_
                str = str.substring(str.indexOf("P") + 3);

                // X coord
                int x = Integer.parseInt(str.substring(0, str.indexOf(" ")));
                pacCoords[coordsCollected][0] = x;
                // Trim off space between
                str = str.substring(str.indexOf(" ") + 1);
                // Y coord
                int y = Integer.parseInt(str.substring(0, str.indexOf(" ")));
                pacCoords[coordsCollected][1] = y;
                str = str.substring(str.indexOf("I1:") + 4);
                // Trim off space
                str = str.substring(str.indexOf(" ") + 1);
                // Pacman direction int
                pDirs[coordsCollected] = Integer.parseInt(str.substring(0, str.indexOf(" ")));
                // Trim off Pfit:_
                str = str.substring(str.indexOf(":") + 2);
                pFits[coordsCollected] = Integer.parseInt(str.substring(0, str.indexOf(" ")));
                // Trim off Ppowered:
                str = str.substring(str.indexOf(":") + 2);
                pPowered[coordsCollected] = Boolean.valueOf(str);
                coordsCollected++;
            }
        }

    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("PacmanUI");
        frame.setContentPane(new PacmanUI().PacmanTextPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

    }

    public void start(Stage stage) throws Exception {

        setup(stage);

        boolean evolve = false;

        // Settings for genetic algorithm
        int popSize = 600;
        int totalGens = 150;
        double mutationChance = 0.5;
        int lowerPacmen = 4;
        int upperPacmen = 20;
        // Amount of genetic algorithms run
        int podAmount = 1;

        // File Settings
        int gameNum = 54;
        int generationNum = 140;

        if (evolve) {
            GeneticAlgorithm[] tests3 = new GeneticAlgorithm[podAmount];
            for (int i = 0; i < tests3.length; i++) {
                tests3[i] = new GeneticAlgorithm(PacmanDataPath, popSize, totalGens, mutationChance, lowerPacmen, upperPacmen);
            }
            for (int i = 0; i < tests3.length; i++) {
                tests3[i].makeGenerations();
            }
        }

        // Viewing Setup
        gameFile = PacmanDataPath + "/Game" +  gameNum + "/Gens/PacGen_" + generationNum;

        Thread GameViewer = new Thread(() -> {
            try {
                parseFile(gameFile);
            } catch (FileNotFoundException ex) {
                gc.setFill(Color.BLACK);
                gc.fillText("NO GAME SELECTED", stageW / 2 - 60, stageH / 2);
            }

            VisualGame vg = new VisualGame(500, pacCoords,  pDirs, pFits, pPowered, gc, generationNum, 12);
            gc.setFill(Color.WHITE);
            gc.fillText("GAME OVER", stageW / 2 - 60, stageH / 2);

        });

        if (!evolve) {
            stage.show();
            GameViewer.start();
        }
    }
    public void setup(Stage stage) {
        stage.setWidth(stageW);
        stage.setHeight(stageH + 20);

        // The canvas for the pacman game
        Canvas canvas = new Canvas(stageW, stageH);
        gc = canvas.getGraphicsContext2D();

        Pane root = new Pane();
        root.getChildren().addAll(canvas);
        Scene scene = new Scene(root, stageW, stageH + 20);

        stage.setScene(scene);

        String pathToDesktop = System.getProperty("user.home") + "/Desktop/";

        File PacmanData = new File(pathToDesktop + "PacmanData");
        PacmanDataPath = PacmanData.getPath();
        if (!PacmanData.exists()) PacmanData.mkdir();

        for (int i = 0; i < MAXMOVES; i++) {
            pacCoords[i][0] = 0;
            pacCoords[i][1] = 0;
            gCoords[i][0] = 0;
            gCoords[i][1] = 0;
            g2Coords[i][0] = 0;
            g2Coords[i][1] = 0;
        }
    }

}
