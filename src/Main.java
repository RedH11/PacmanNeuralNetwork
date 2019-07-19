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

    final int MAXMOVES = 500;

    /**
     * pacCoords - Pacman's Coordinate History
     * gCoords - Ghost's (Inky for now) Coordinate History
     * gDirs - Ghost's Direction History
     * gFits - Ghost's Fitness History
     */
    int[][] pacCoords = new int[MAXMOVES][2];
    int[][] gCoords = new int[MAXMOVES][2];
    int[] gDirs = new int[MAXMOVES];
    int[] gFits = new int[MAXMOVES];

    String gameFile = "";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        for (int i = 0; i < MAXMOVES; i++) {
            pacCoords[i][0] = 0;
            pacCoords[i][1] = 0;
            gCoords[i][0] = 0;
            gCoords[i][1] = 0;
        }

        stage.setWidth(stageW);
        stage.setHeight(stageH);

        // The canvas for the pacman game
        Canvas canvas = new Canvas(stageW, stageH);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        Pane root = new Pane();
        root.getChildren().addAll(canvas);
        Scene scene = new Scene(root, stageW, stageH);

        stage.setScene(scene);
        stage.show();

        String pathToDesktop = System.getProperty("user.home") + "/Desktop/";

        File PacmanData = new File(pathToDesktop + "PacmanData");
        String PacmanDataPath = PacmanData.getPath() + "/";
        if (!PacmanData.exists()) PacmanData.mkdir();

        boolean evolve = false;

        if (evolve) {
            GeneticAlgorithm ga = new GeneticAlgorithm(PacmanDataPath,100, 10, 1, 3, 15);
            ga.makeGenerations();
        }

        int gameNum = 3;
        int generationNum = 7;
        boolean show = true;

        gameFile = PacmanDataPath + "pacGens" + gameNum + "/gen_" + generationNum;

        Thread GameViewer = new Thread(() -> {
            if (show) {
                try {
                    parseFile(gameFile);
                } catch (FileNotFoundException ex) {}
                VisualGame vg = new VisualGame(500, pacCoords, gCoords, gDirs, gFits, gc, generationNum, 2);
            } else {
                gc.setFill(Color.BLACK);
                gc.fillText("NO GAME SELECTED", stageW/2 - 60, stageH/2);
            }
        });

        GameViewer.start();
    }

    public void parseFile(String fileName) throws FileNotFoundException {
        String str = "";

        int coordsCollected = 0;

        File file = new File(fileName);
        Scanner sc = new Scanner(file);

        while (sc.hasNextLine()) {
            str = sc.nextLine();
            if (str.length() > 0) {
                // "P: " + pacman.x + " " + pacman.y + " I: " + inky.x + " " + inky.y + "Idir: " + inky.getDir() + "Ifit: " + inky.fitness + "\n")
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

                // Trim off _I:_
                str = str.substring(str.indexOf("I") + 3);

                // X coord
                x = Integer.parseInt(str.substring(0, str.indexOf(" ")));
                gCoords[coordsCollected][0] = x;
                // Trim off space between2
                str = str.substring(str.indexOf(" ") + 1);
                // Y coord
                y = Integer.parseInt(str.substring(0, str.indexOf(" ")));
                gCoords[coordsCollected][1] = y;

                // Trim off Idir:_
                str = str.substring(str.indexOf(":") + 2);
                // Direction int
                gDirs[coordsCollected] = Integer.parseInt(str.substring(0, 1));
                // Trim off Ifit:_
                str = str.substring(str.indexOf(":") + 2);
                // Fitness int
                gFits[coordsCollected] = Integer.parseInt(str);
                coordsCollected++;
            }
        }

    }
}

