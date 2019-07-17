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

        // Visual Effects
        int gameFPS = 2;
        int pacmanMPS = 2;
        int ghostsMPS = 2;

        stage.setWidth(stageW - 20);
        stage.setHeight(stageW);

        // The canvas for the pacman game
        Canvas canvas = new Canvas(stageW, stageW);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        MapLayout map = new MapLayout();
        GameArray game = new GameArray(map);
        VisualMap visualGame = new VisualMap(game, gc, 0, 0);

        Pane root = new Pane();
        root.getChildren().addAll(canvas);
        Scene scene = new Scene(root, stageW - 20, stageW);

        // Manual Controls
        /*scene.setOnKeyPressed(ev -> {
            if (ev.getCode() == KeyCode.UP) game.setInkyDir(0);
            else if (ev.getCode() == KeyCode.LEFT) game.setInkyDir(1);
            else if (ev.getCode() == KeyCode.RIGHT) game.setInkyDir(2);
            else if (ev.getCode() == KeyCode.DOWN) game.setInkyDir(3);
        });*/

        scene.setOnKeyPressed(ev -> {
            if (ev.getCode() == KeyCode.UP) setCurrDir(0);
            else if (ev.getCode() == KeyCode.LEFT) setCurrDir(1);
            else if (ev.getCode() == KeyCode.RIGHT) setCurrDir(2);
            else if (ev.getCode() == KeyCode.DOWN) setCurrDir(3);
        });

        stage.setScene(scene);
        stage.show();

        NeuralNetwork nn = new NeuralNetwork(3, 5, 4);
        double[] outputs = nn.calculate(15, 2 , 3);

        for (int i = 0; i < outputs.length; i++) {
            System.out.println(outputs[i]);
        }

        // Figure out how to stop garbage collection

        Thread gameThread = new Thread(() -> {
            while (true) {
                visualGame.drawGrid();
                try {
                    Thread.sleep(1000 / gameFPS);
                } catch (InterruptedException ex) {
                }
            }
        });

        Thread pacmanThread = new Thread(() -> {
            while (game.pacmanIsAlive()) {
                game.updatePacman();
                try {
                    Thread.sleep(1000/pacmanMPS);
                } catch (InterruptedException ex) {}
            }
        });

        Thread ghostThread = new Thread(() -> {
            while (game.pacmanIsAlive()) {

                if (game.isScraredMode()) {
                    game.updateInky(currDir);
                    try {
                        Thread.sleep(1000/pacmanMPS + 200);
                    } catch (InterruptedException ex) {}
                } else {
                    game.updateInky(currDir);
                    try {
                        Thread.sleep(1000/pacmanMPS);
                    } catch (InterruptedException ex) {}
                }
            }
        });

        gameThread.start();
        pacmanThread.start();
        ghostThread.start();

    }

    public void setCurrDir(int dir) {
        currDir = dir;
    }

}
