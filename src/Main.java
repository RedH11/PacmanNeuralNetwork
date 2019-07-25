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
import java.io.ObjectInputStream;
import java.util.Scanner;

/*
    https://stackoverflow.com/questions/33250413/javafx-stage-show-ending-in-program-freezing
    FOR FIXING THE FREEZING
 */

public class Main extends Application {

    int stageW = 400;
    int stageH = 400;
    int currDir = 0;

    final int MAXMOVES = 200;

    String PacmanDataPath;
    GraphicsContext gameGC;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        createGameStage();
        setup(stage);
        stage.setResizable(false);

        stage.show();
    }

    public void setup(Stage stage) {
        stage.setWidth(stageW);
        stage.setHeight(stageH + 20);

        String pathToDesktop = System.getProperty("user.home") + "/Desktop/";
        File PacmanData = new File(pathToDesktop + "PacmanData");
        PacmanDataPath = PacmanData.getPath();
        if (!PacmanData.exists()) PacmanData.mkdir();

        stage.setScene(new Scene(new PacmanSettings(stage, PacmanDataPath, gameGC), stageW, stageH));
    }

    public void createGameStage() {
        Stage game = new Stage();
        game.setWidth(550);
        game.setHeight(620);
        // The canvas for the pacman game
        Canvas canvas = new Canvas(550, 620);
        // Hide it at first
        gameGC = canvas.getGraphicsContext2D();
        Pane root = new Pane();
        root.getChildren().add(canvas);
        game.setScene(new Scene(root, 550, 620));
        game.show();
    }
}