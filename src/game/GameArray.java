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

    Grid emptyGrid = new Grid();
    Grid hWallGrid = new Grid();
    Grid vWallGrid = new Grid();
    Grid pelletGrid = new Grid();
    Grid powerPelletGrid = new Grid();
    Grid pacmanGrid = new Grid();
    Grid inkyGrid = new Grid();

    public GameArray() {
        hWallGrid.sethWall(true);
        vWallGrid.setvWall(true);
        pelletGrid.setPellet(true);
        powerPelletGrid.setPowerPellet(true);
        pacmanGrid.setPacman(true);
        inkyGrid.setInky(true);

        MapLayout mapDefault = new MapLayout();
        gameMap = mapDefault.getLayout();
        printMap();
        pacman = new Pacman();
        inkyGhost = new InkyGhost(gameMap);


    }

    public GameArray(NeuralNetwork brain) {
        hWallGrid.sethWall(true);
        vWallGrid.setvWall(true);
        pelletGrid.setPellet(true);
        powerPelletGrid.setPowerPellet(true);
        pacmanGrid.setPacman(true);
        inkyGrid.setInky(true);

        MapLayout mapDefault = new MapLayout();
        gameMap = mapDefault.getLayout();
        pacman = new Pacman();
        inkyGhost = new InkyGhost(gameMap, brain);
    }

    public void runGame() {

        inkyGhost.respawn(gameMap);

        while (pacmanIsAlive() && moves < TOTAL_MOVES) {
            updatePacman();
            updateInky(inkyGhost.think(inkyGhost.see(pacman.getCurrentPosX(), pacman.getCurrentPosY())));
            printMap();
            moves++;
        }
    }

    public void showGame(Stage stage){

        stage.setWidth(stageW - 20);
        stage.setHeight(stageW);

        // The canvas for the pacman game
        Canvas canvas = new Canvas(stageW, stageW);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        VisualMap visualGame = new VisualMap(this, gc, 0, 0);

        Pane root = new Pane();
        root.getChildren().addAll(canvas);
        Scene scene = new Scene(root, stageW - 20, stageW);

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
            }
        }
    }

    public void eatGhost() {
        inkyGhost.respawn(gameMap);
        pacman.setPowered(false);
        //pacman.changeScore(200)
        // Punish inky for being eaten
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
                (inkyGhost.distanceFromPac(pacman.getCurrentPosX(), pacman.getCurrentPosY()) - 46), 2)) / 40);

        if (gameMap[inkyGhost.getCurrentY()][inkyGhost.getCurrentX()].isPacman()) {
            if (inkyGhost.getScared()) {
                inkyGhost.respawn(gameMap);
                inkyGhost.addScore(-100);
            } else {
                inkyGhost.addScore(200);
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

    public boolean isScraredMode() {
        return pacman.isPowered();
    }

    public boolean pacmanIsAlive() {
        return pacman.isAlive();
    }

    public void printMap() {
        for (int r = 0; r < gameMap.length; r++) {
            for (int c = 0; c < gameMap.length; c++) {
                if (gameMap[r][c].isvWall()) System.out.print("| ");
                    // Doubled to make a square because the vertical is long
                else if (gameMap[r][c].ishWall()) System.out.print("- ");
                else if (gameMap[r][c].isPacman()) System.out.print("C ");
                else if (gameMap[r][c].isInky()) System.out.print("8 ");
                else if (gameMap[r][c].isEmpty()) System.out.print("  ");
                else if (gameMap[r][c].isPellet()) System.out.print(". ");
                else if (gameMap[r][c].isPowerPellet()) System.out.print("* ");
            }
            System.out.println();
        }
        System.out.println("\n\n\n");
    }
    
    public void setCurrDir(int dir) {
        currDir = dir;
    }

    public Grid[][] getGameMapLayout() {
        return gameMap;
    }
}
