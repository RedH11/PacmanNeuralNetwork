//import gamePack.GameArray;
//import gamePack.MapLayout;
//import gamePack.VisualMap;
//import gamePack.GameArray;
//import gamePack.MapLayout;
//import gamePack.VisualMap;
import gamePack.GameArray;
import gamePack.MapLayout;
import gamePack.VisualMap;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
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

        int gameFPS = 2;

        stage.setWidth(stageW - 20);
        stage.setHeight(stageW);

        // The canvas for the pacman gamePack
        Canvas canvas = new Canvas(stageW, stageW);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        MapLayout map = new MapLayout();
        GameArray game = new GameArray(map);
        VisualMap visualGame = new VisualMap(game, gc, 0, 0);

        Pane root = new Pane();
        root.getChildren().addAll(canvas);
        Scene scene = new Scene(root, stageW - 20, stageW);

        scene.setOnKeyPressed(ev -> {
            if (ev.getCode() == KeyCode.UP) game.setPacmanDir(0);
            else if (ev.getCode() == KeyCode.LEFT) game.setPacmanDir(1);
            else if (ev.getCode() == KeyCode.RIGHT) game.setPacmanDir(2);
            else if (ev.getCode() == KeyCode.DOWN) game.setPacmanDir(3);
        });

        stage.setScene(scene);
        stage.show();

        Thread gameThread = new Thread(() -> {
            while (true) {
                visualGame.drawGrid();
                System.out.println("\n\n");
                try {
                    Thread.sleep(50);
                } catch (InterruptedException ex) {}
            }
        });

        Thread networkThread = new Thread(() -> {
            while (true) {
            game.updateGrid();
            try {
                Thread.sleep(200);
            } catch (InterruptedException ex) {}
            }
        });

        gameThread.start();
        networkThread.start();
    }

}
