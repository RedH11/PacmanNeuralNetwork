import game.GameArray;
import game.MapLayout;
import game.VisualMap;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {

    /**TODO: Make Grid 31 length so that there is a middle, and make map
     *
     *
     */
    int stageW = 660;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {

        int gameFPS = 16;

        stage.setWidth(stageW - 20);
        stage.setHeight(stageW);

        // The canvas for the pacman game
        Canvas canvas = new Canvas(stageW, stageW);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        MapLayout map = new MapLayout();
        GameArray gameMap = new GameArray(map.getLayout());
        VisualMap pacmanGame = new VisualMap(map.getLayout(), gc, 0, 0);

        Pane root = new Pane();
        root.getChildren().addAll(canvas);
        Scene scene = new Scene(root, stageW - 20, stageW);
        stage.setScene(scene);
        stage.show();

        Thread gameThread = new Thread(() -> {
            pacmanGame.updateGrid();
            try {
                Thread.sleep(1000/gameFPS);
            } catch (InterruptedException ex) {}
        });

        Thread networkThread = new Thread(() -> {

        });
    }

}
