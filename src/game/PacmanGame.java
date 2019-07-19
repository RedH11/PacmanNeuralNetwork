package game;

import com.sun.xml.internal.bind.v2.runtime.reflect.Lister;

import java.io.FileWriter;
import java.io.IOException;

public class PacmanGame {

    Pacman pacman;
    Inky inky;

    // 28 rows, 31 columns
    private Tile[][] map = new Tile[31][28];

    // 0: Pellet / 1: Wall / 6: Empty / 8: Power Pellet

    final private int[][] tilesRepresentation = {
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1},
            {1, 8, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 8, 1},
            {1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 0, 1},
            {1, 0, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 1},
            {1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 6, 1, 1, 6, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 6, 1, 1, 6, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 0, 1, 1, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 1, 1, 0, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 0, 1, 1, 6, 1, 1, 1, 1, 1, 1, 1, 1, 6, 1, 1, 0, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 0, 1, 1, 6, 1, 1, 1, 1, 1, 1, 1, 1, 6, 1, 1, 0, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 0, 6, 6, 6, 1, 1, 1, 1, 1, 1, 1, 1, 6, 6, 6, 0, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 0, 1, 1, 6, 1, 1, 1, 1, 1, 1, 1, 1, 6, 1, 1, 0, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 0, 1, 1, 6, 1, 1, 1, 1, 1, 1, 1, 1, 6, 1, 1, 0, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 0, 1, 1, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 1, 1, 0, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 0, 1, 1, 6, 1, 1, 1, 1, 1, 1, 1, 1, 6, 1, 1, 0, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 0, 1, 1, 6, 1, 1, 1, 1, 1, 1, 1, 1, 6, 1, 1, 0, 1, 1, 1, 1, 1, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1},
            {1, 8, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 8, 1},
            {1, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 1},
            {1, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 1},
            {1, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 1},
            {1, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1},
            {1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}};

    // How much fitness Pacman gets for these achievements
    private int pelletScore = 10;
    private int poweredScore = 100;
    private int eatGhost = 200;
    private int pacEaten = -200;

    // How much fitness Inky gets for these achievements
    private int eatPacman = 400;
    private int nearPacman = 1;
    private int inkyEaten = -200;

    private int gameMoves = 0;
    private int MAXMOVES = 500;

    boolean recordGame = false;
    int generation;
    FileWriter moveWriter;

    public PacmanGame(String PacmanDataPath, int MAXMOVES, boolean recordGame, int generation, int pacNum) {
        this.MAXMOVES = MAXMOVES;
        this.recordGame = recordGame;
        this.generation = generation;
        createMap();
        pacman = new Pacman();
        inky = new Inky();

        if (recordGame) {

            try {
                moveWriter = new FileWriter(PacmanDataPath + "pacGens" + pacNum + "/gen_" + generation);
            } catch (Exception ex) {}
        }
    }

    public PacmanGame(String PacmanDataPath, int MAXMOVES, boolean recordGame, int generation, int pacNum, NeuralNetwork brain) {
        this.MAXMOVES = MAXMOVES;
        this.recordGame = recordGame;
        this.generation = generation;
        createMap();
        pacman = new Pacman();
        inky = new Inky(brain);

        if (recordGame) {

            try {
                moveWriter = new FileWriter(PacmanDataPath + "pacGens" + pacNum + "/gen_" + generation);
            } catch (Exception ex) {}
        }
    }

    public PacmanGame(int MAXMOVES, NeuralNetwork brain) {
        this.MAXMOVES = MAXMOVES;
        this.recordGame = recordGame;
        this.generation = generation;
        createMap();
        pacman = new Pacman();
        inky = new Inky(brain);
    }

    public PacmanGame(int MAXMOVES) {
        this.MAXMOVES = MAXMOVES;
        createMap();
        pacman = new Pacman();
        inky = new Inky();
    }

    public void simulateGame() throws IOException {
        while (gameMoves < MAXMOVES) {
            if (recordGame) {
                writeTurns();
            }
            //printMap();
            simulateTurn();
            gameMoves++;
        }

        if (recordGame) moveWriter.close();
    }

    public void writeTurns() {
        try {
            moveWriter.write("P: " + pacman.x + " " + pacman.y + " I: " + inky.x + " " + inky.y + " Idir: " + inky.getDir() + " Ifit: " + inky.fitness + "\n");
        } catch (Exception ex) {
            System.out.println("Error Writing Moves" + ex);
        }
    }

    public int getInkyFitness() {
        return inky.fitness;
    }

    private void createMap() {

        // Initialize tiles
        for (int r = 0; r < 31; r++) {
            for (int c = 0; c < 28; c++) {
                map[r][c] = new Tile(r, c);
                switch(tilesRepresentation[r][c]) {
                    case 1: //1 is a wall
                        map[r][c].wall = true;
                        break;
                    case 0: // 0 is a dot
                        map[r][c].dot = true;
                        break;
                    case 8: // 8 is a big dot
                        map[r][c].bigDot = true;
                        break;
                    case 6://6 is a blank space
                        map[r][c].eaten = true;
                        break;
                }
            }
        }
    }

    private void printMap() {
        for (int y = 0; y < 31; y++) {
            for (int x = 0; x < 28; x++) {
                if (x == pacman.x && y == pacman.y) System.out.print("C\t");
                else if (x == inky.x && y == inky.y) System.out.print("8\t");
                else if (map[y][x].wall) System.out.print("0\t");
                else if (map[y][x].eaten) System.out.print("\t");
                else if (map[y][x].dot) System.out.print("-\t");
                else if (map[y][x].bigDot) System.out.print("+\t");
            }
            System.out.println();
        }
        System.out.println("\n\n");
    }

    private void simulateTurn() {
        pacman.moveBot(map, inky.x, inky.y);
        checkStates();
        inky.move(map, pacman.x, pacman.y);
        checkStates();
    }

    private void checkStates() {
        // Interaction when Pacman and Inky Collide
        if (pacman.x == inky.x && pacman.y == inky.y) {
            if (!pacman.powered) {
                pacman.alive = false;
                pacman.addFitness(pacEaten);
                inky.addFitness(eatPacman);
                inky.respawn(); // Inky respawns to stop it from camping pacmans spawn
            } else {
                inky.alive = false;
                pacman.addFitness(eatGhost);
                inky.addFitness(inkyEaten);
            }
        }

        // Pacman on a pellet
        if (map[pacman.y][pacman.x].bigDot && !map[pacman.y][pacman.x].eaten) {
            pacman.powered = true;
            pacman.addFitness(poweredScore);
            map[pacman.y][pacman.x].eaten = true;
        } else if (map[pacman.y][pacman.x].dot && !map[pacman.y][pacman.x].eaten) {
            pacman.addFitness(pelletScore);
            map[pacman.y][pacman.x].eaten = true;
        }

        // Inky in proximity to pacman (within 2 tiles)
        if (inky.distanceFromPac(pacman.x, pacman.y) <= Math.sqrt(5.0)) inky.addFitness(nearPacman);
    }
}
