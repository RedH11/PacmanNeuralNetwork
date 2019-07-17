package game;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.Random;

public class GameArray {
    int stageW = 660;
    int currDir = 0;
    int gameFPS = 2;
    int pacmanMPS = 2;
    int ghostsMPS = 2;
    // Make a 32x32 2D grid as the game
    Grid[][] gameMap;
    Pacman pacman;
    InkyGhost inkyGhost;

    int scaredMoves = 0;
    // 12 seconds of moves
    final int totalScaredMoves = 240;

    final int TOTAL_MOVES = 3600;
    int moves = 0;


    public GameArray(InkyGhost inky) {
      //  gameMap = map.getLayout();
        // Make new pacman for the game
        pacman = new Pacman();
        inkyGhost = inky;
    }
    public void runGame(){
        MapLayout map = new MapLayout();
        //GameArray game = new GameArray();
       // VisualMap visualGame = new VisualMap(game, gc, 0, 0);
        NeuralNetwork nn = new NeuralNetwork(3, 5, 4);
        double[] outputs = nn.calculate(15, 2 , 3);

        for (int i = 0; i < outputs.length; i++) {
            System.out.println(outputs[i]);
        }

        // Figure out how to stop garbage collection

        Thread gameThread = new Thread(() -> {
            while (true) {
                //visualGame.drawGrid();
                try {
                    Thread.sleep(1000 / gameFPS);
                } catch (InterruptedException ex) {
                }
            }
        });

        Thread pacmanThread = new Thread(() -> {
            while (pacmanIsAlive()) {
                updatePacman();
                try {
                    Thread.sleep(1000/pacmanMPS);
                } catch (InterruptedException ex) {}
            }
        });

        Thread ghostThread = new Thread(() -> {
            while (pacmanIsAlive()) {

                if (isScraredMode()) {
                    updateInky(currDir);
                    try {
                        Thread.sleep(1000/pacmanMPS + 200);
                    } catch (InterruptedException ex) {}
                } else {
                    updateInky(currDir);
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
    public void showGame(Stage stage){
        stage.setWidth(stageW - 20);
        stage.setHeight(stageW);

        // The canvas for the pacman game
        Canvas canvas = new Canvas(stageW, stageW);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        MapLayout map = new MapLayout();
      //  GameArray game = new GameArray(map);
        VisualMap visualGame = new VisualMap(this, gc, 0, 0);

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
            while (pacmanIsAlive()) {
                updatePacman();
                try {
                    Thread.sleep(1000/pacmanMPS);
                } catch (InterruptedException ex) {}
            }
        });

        Thread ghostThread = new Thread(() -> {
            while (pacmanIsAlive()) {

                if (isScraredMode()) {
                    updateInky(currDir);
                    try {
                        Thread.sleep(1000/pacmanMPS + 200);
                    } catch (InterruptedException ex) {}
                } else {
                    updateInky(currDir);
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

    public void updatePacman() {
        // Deletes space behind pacman
        if (pacman.getDir() != -1) gameMap[pacman.getCurrentPosY()][pacman.getCurrentPosX()].setEmpty(true);
        pacman.moveBot(gameMap);
        gameMap[pacman.getCurrentPosY()][pacman.getCurrentPosX()].setPacman(true);

        if (pacman.isPowered()) scaredMoves++;
        if (pacman.isPowered() && !inkyGhost.getScared()) inkyGhost.setScared();

        if (scaredMoves >= totalScaredMoves - 1) {
            scaredMoves = 0;
            pacman.setPowered(false);
            inkyGhost.setNormal();
        }

        if (gameMap[inkyGhost.getCurrentY()][inkyGhost.getCurrentX()].isPacman()) {
            if (!inkyGhost.getScared()) {
                pacman.setAlive(false);
                inkyGhost.addScore(200);
                System.out.println("Fitness:" + inkyGhost.getScore());
            }
        }
    }

    public void eatGhost() {
        System.out.println("NOMNOM GHOST");
        inkyGhost.respawn(gameMap);
        pacman.setPowered(false);
        //pacman.changeScore(200)
        inkyGhost.addScore(-100);
    }

    public void updateGhosts() {
        // Spawn ghosts in the corner
        /*gameMap[pinkyGhost.getCurrentY()][pinkyGhost.getCurrentX()].setPinky(false);
        pinkyGhost.move(pacman.getCurrentPosX(), pacman.getCurrentPosY(), false);
        gameMap[pinkyGhost.getCurrentY()][pinkyGhost.getCurrentX()].setPinky(true);

        gameMap[inkyGhost.getCurrentY()][inkyGhost.getCurrentX()].setInky(false);
        inkyGhost.move(pacman.getCurrentPosX(), pacman.getCurrentPosY(), false);
        gameMap[inkyGhost.getCurrentY()][inkyGhost.getCurrentX()].setInky(true);

        gameMap[blinkyGhost.getCurrentY()][blinkyGhost.getCurrentX()].setBlinky(false);
        blinkyGhost.move(pacman.getCurrentPosX(), pacman.getCurrentPosY(), false);
        gameMap[blinkyGhost.getCurrentY()][blinkyGhost.getCurrentX()].setBlinky(true);

        gameMap[clydeGhost.getCurrentY()][clydeGhost.getCurrentX()].setClyde(false);
        clydeGhost.move(pacman.getCurrentPosX(), pacman.getCurrentPosY(), false);
        gameMap[clydeGhost.getCurrentY()][clydeGhost.getCurrentX()].setClyde(true);*/

    }

    public void updateInky(int currDir) {
        gameMap[inkyGhost.getCurrentY()][inkyGhost.getCurrentX()].setInky(false);
        inkyGhost.move(currDir, gameMap);
        gameMap[inkyGhost.getCurrentY()][inkyGhost.getCurrentX()].setInky(true);

        inkyGhost.addScore(Math.sqrt(Math.pow(
                (inkyGhost.distanceFromPac(pacman.getCurrentPosX(), pacman.getCurrentPosY()) - 46), 2)) / 20);

        System.out.println("Fitness:" + inkyGhost.getScore());

        if (gameMap[inkyGhost.getCurrentY()][inkyGhost.getCurrentX()].isPacman()) {
            if (inkyGhost.getScared()) {
                inkyGhost.respawn(gameMap);
                inkyGhost.addScore(-100);
                System.out.println("Ghost got eaten");
            } else {
                inkyGhost.addScore(200);
                System.out.println("Ate pacman");
                pacman.setAlive(false);
            }
        }
    }

    public void setInkyDir(int dir) {
        inkyGhost.move(dir, gameMap);
    }

    public void setPacmanDir(int dir) {
        pacman.setDir(dir);
    }

    public void setScaredMode() {

    }

    public boolean isScraredMode() {


        return pacman.isPowered();
    }

    public boolean pacmanIsAlive() {
        return pacman.isAlive();
    }

    public Grid[][] getLayout() {
        return gameMap;
    }

    public void outputConsoleMap() {
        for (int r = 0; r < gameMap.length; r++) {
            for (int c = 0; c < gameMap.length; c++) {
                if (gameMap[r][c].isvWall()) System.out.print("| ");
                    // Doubled to make a square because the vertical is long
                else if (gameMap[r][c].ishWall()) System.out.print("- ");
                else if (gameMap[r][c].isPacman()) System.out.print("C ");
                else if (gameMap[r][c].isbLWall()) System.out.print("\\ ");
                else if (gameMap[r][c].isbRWall()) System.out.print("/ ");
                else if (gameMap[r][c].istLWall()) System.out.print("/ ");
                else if (gameMap[r][c].istRWall()) System.out.print("\\ ");
                    // Is ghost
                else if (gameMap[r][c].isPinky()) System.out.println("P ");
                else if (gameMap[r][c].isBlinky()) System.out.println("B ");
                else if (gameMap[r][c].isInky()) System.out.println("I ");
                else if (gameMap[r][c].isClyde()) System.out.println("8 ");
                else if (gameMap[r][c].isEmpty()) System.out.print("  ");
                else if (gameMap[r][c].isPellet()) System.out.print(". ");
                else if (gameMap[r][c].isPowerPellet()) System.out.print("* ");

            }
            System.out.println();
        }
    }
    public void setCurrDir(int dir) {
        currDir = dir;
    }
}
