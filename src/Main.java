import game.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {

    int stageW = 660;
    int currDir = 0;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {

        //GeneticAlgorithm ga = new GeneticAlgorithm(15, 10, 0.1, 3, 12, stage);
        //ga.makeGenerations();

        GameArray ga = new GameArray();
        ga.runGame();

    }


}
