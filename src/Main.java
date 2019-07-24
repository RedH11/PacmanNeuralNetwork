import game.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

/*
    DOESN'T PRINT OUT THE LAST GEN
    AND I HAD THE CONSOLE PRINTING OUT SIDEWAYS THE WHOLE GODDAMN TIME
 */
public class Main extends Application {

    int stageW = 550;
    int stageH = 620;
    int currDir = 0;

    final int MAXMOVES = 600;

    /**
     * pacCoords - Pacman's Coordinate History
     * gCoords - Ghost's (Inky for now) Coordinate History
     * gDirs - Ghost's Direction History
     * gFits - Ghost's Fitness History
     */
    int[][] pacCoords = new int[MAXMOVES][2];
    int[][] gCoords = new int[MAXMOVES][2];
    int[][] g2Coords = new int[MAXMOVES][2];
    int[] gDirs = new int[MAXMOVES];
    int[] gFits = new int[MAXMOVES];
    int[] g2Dirs = new int[MAXMOVES];
    int[] g2Fits = new int[MAXMOVES];
    int[] pDirs = new int[MAXMOVES];
    int[] pFits = new int[MAXMOVES];
    boolean[] pPowered = new boolean[MAXMOVES];

    String gameFile = "";
    String PacmanDataPath;
    GraphicsContext gc;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        setup(stage);

        boolean evolve = false;

        // Settings for genetic algorithm
        int popSize = 100;
        int totalGens = 20;
        double mutationChance = 6;
        int lowerGhosts = 2;
        int upperGhosts = 10;
        int lowerPacmen = 2;
        int upperPacmen = 10;
        // Amount of genetic algorithms run
        int podAmount = 1;

        // When viewing what character to show
        boolean pacman = true;
        // File Settings
        int gameNum = 24;
        int generationNum = 0;

        if (evolve) {
            GeneticAlgorithm[] tests3 = new GeneticAlgorithm[podAmount];
            for (int i = 0; i < tests3.length; i++) {
                tests3[i] = new GeneticAlgorithm(PacmanDataPath, popSize, totalGens, mutationChance, lowerGhosts, upperGhosts, lowerPacmen, upperPacmen);
            }
            for (int i = 0; i < tests3.length; i++) {
                tests3[i].makeGenerations();
            }
        }

        // Viewing Setup
        if (pacman) gameFile = PacmanDataPath + "/Game" +  gameNum + "/Gens/PacGen_" + generationNum;
        else gameFile = PacmanDataPath + "/Game" +  gameNum + "/Gens/Inkgen_" + generationNum;

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

                // Trim off _I1:_
                str = str.substring(str.indexOf("I1:") + 4);
                // X coord
               // x = Integer.parseInt(str.substring(0, str.indexOf(" ")));
             //   gCoords[coordsCollected][0] = x;
                // Trim off space
                str = str.substring(str.indexOf(" ") + 1);
                // Y coord
              //  y = Integer.parseInt(str.substring(0, str.indexOf(" ")));
            //    gCoords[coordsCollected][1] = y;
                // Trim off I1dir:_
             //   str = str.substring(str.indexOf(":") + 2);
                // Ghost Direction int
               // gDirs[coordsCollected] = Integer.parseInt(str.substring(0, 1));
                // Trim off I2fit:_
             //   str = str.substring(str.indexOf(":") + 2);
                // Ghost Fitness int
                //gFits[coordsCollected] = Integer.parseInt(str.substring(0, str.indexOf(" ")));

                // Trim off _I2:_
               // str = str.substring(str.indexOf("I2:") + 4);
                // X coord
               // x = Integer.parseInt(str.substring(0, str.indexOf(" ")));
                //g2Coords[coordsCollected][0] = x;
                // Trim off space
                //str = str.substring(str.indexOf(" ") + 1);
                // Y coord
                //y = Integer.parseInt(str.substring(0, str.indexOf(" ")));
                //g2Coords[coordsCollected][1] = y;
                // Trim off I2dir:_
                //str = str.substring(str.indexOf(":") + 2);
                // Ghost Direction int
                //g2Dirs[coordsCollected] = Integer.parseInt(str.substring(0, 1));
                // Trim off I2fit:_
                //str = str.substring(str.indexOf(":") + 2);
                // Ghost Fitness int
                //g2Fits[coordsCollected] = Integer.parseInt(str.substring(0, str.indexOf(" ")));
                // Trim off Pdir:_
            //    str = str.substring(str.indexOf(":") + 2);
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

