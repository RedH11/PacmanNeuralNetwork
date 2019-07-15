import game.GameArray;
import game.VisualMap;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {

    int stageW = 660;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {

        stage.setWidth(stageW - 20);
        stage.setHeight(stageW);

        // The canvas for the pacman game
        Canvas canvas = new Canvas(stageW, stageW);
        GraphicsContext gc = canvas.getGraphicsContext2D();


        // Add in array
        //VisualMap pacmanGame = new VisualMap(gc, 0, 0);

        GameArray gameMap = new GameArray();
        gameMap.outputMap();

        Pane root = new Pane();
        root.getChildren().addAll(canvas);
        Scene scene = new Scene(root, stageW - 20, stageW);
        stage.setScene(scene);
        stage.show();
    }

}
