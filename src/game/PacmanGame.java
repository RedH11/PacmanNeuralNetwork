package game;

import game.NEAT.Genome;

import java.io.*;
import java.util.ArrayList;

public class PacmanGame implements Serializable {
    Pacman pacman;
    Ghost ghostOne;
    Ghost ghostTwo;

    final int MAX_PELLETS = 258;

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
    private int pelletScore = 1;
    private int poweredScore = 1;

    private int poweredMoves = 20;

    private int gameMoves = 0;
    private int scaredStart = 0;
    private int MAXMOVES;

    private GhostInfoStorage is;
    static String PacmanDataPath;

    int INPUTS;

    public PacmanGame(String PacmanDataPath, int MAXMOVES, NeuralNetwork pacmanBrain, Genome g1, Genome g2, int INPUTS) {
        this.MAXMOVES = MAXMOVES;
        this.PacmanDataPath = PacmanDataPath;
        is = new GhostInfoStorage(MAXMOVES);
        createMap();
        pacman = new Pacman(pacmanBrain);
        ghostOne = new Ghost(g1, INPUTS);
        ghostTwo = new Ghost(g2, INPUTS);
        this.INPUTS = INPUTS;
    }

    public void simulateGame() {
        gameMoves = 0;
        while ((gameMoves < MAXMOVES) && pacman.lives > 0) {
            // Add all of the game information
            simulateTurn();
            is.addAllCoords(pacman.x, pacman.y, ghostOne.x, ghostOne.y, ghostTwo.x, ghostTwo.y);
            is.addAllInfo(ghostOne.fitness, ghostTwo.fitness, pacman.powered);
            gameMoves++;
        }
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

    private void simulateTurn() {
        pacman.move(map);
        checkStates();
        ghostOne.move(map, pacman.x, pacman.y);
        checkStates();
        ghostTwo.move(map, pacman.x, pacman.y);
        checkStates();
    }

    private void checkStates() {

        if (gameMoves - scaredStart == poweredMoves) { pacman.powered = false; }

        // Pacman on a pellet
        if (map[pacman.y][pacman.x].bigDot && !map[pacman.y][pacman.x].eaten) {
            pacman.powered = true;
            map[pacman.y][pacman.x].eaten = true;
            scaredStart = gameMoves;
        } else if (map[pacman.y][pacman.x].dot && !map[pacman.y][pacman.x].eaten) {
            map[pacman.y][pacman.x].eaten = true;
        }

        if ((pacman.x == ghostOne.x)&&(pacman.y == ghostOne.y)){
            if (pacman.powered) {
                ghostOne.addFitness(-20);
                ghostOne.respawn();

            } else {
                ghostOne.addFitness((int)Math.sqrt(Math.pow(gameMoves - MAXMOVES, 2)));
                pacman.lives--;
                pacman.respawn();
                ghostOne.respawn();
                ghostTwo.respawn();
            }
        }
        if ((pacman.x == ghostTwo.x) && (pacman.y == ghostTwo.y)) {
            if(pacman.powered){
                ghostTwo.addFitness(-20);
                ghostTwo.respawn();
            } else {
                ghostTwo.addFitness((int)Math.sqrt(Math.pow(gameMoves - MAXMOVES, 2)));
                pacman.lives--;
                pacman.respawn();
                ghostOne.respawn();
                ghostTwo.respawn();

            }
        }

    }

    public GhostInfoStorage getIs() {
        return is;
    }

    public Ghost getBestGhost() {
            if (ghostOne.fitness > ghostTwo.fitness) return ghostOne;
            else return ghostTwo;
    }

    public static void saveInformation(GhostInfoStorage infoStorage, ObjectOutputStream oos) throws IOException, ClassNotFoundException {

        try {
            // Write the InformationStorage into the file
            oos.writeObject(infoStorage);
            oos.flush();
            // Catch errors
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static ArrayList<GhostInfoStorage> readObjectsFromFile(String filename) throws IOException, ClassNotFoundException {
        ArrayList<GhostInfoStorage> objects = new ArrayList<>();
        InputStream is = null;
        try {
            is = new FileInputStream(filename);
            ObjectInputStream ois = new ObjectInputStream(is);
            while (true) {
                try {
                    GhostInfoStorage object = (GhostInfoStorage) ois.readObject();
                    objects.add(object);
                } catch (EOFException ex) {
                    break;
                }
            }
        } finally {
            if (is != null) {
                is.close();
            }
        }
        return objects;
    }

    private void printMap() {
        for (int y = 0; y < 31; y++) {
            for (int x = 0; x < 28; x++) {
                if (x == pacman.x && y == pacman.y) System.out.print("C\t");
                else if (map[y][x].wall) System.out.print("0\t");
                else if (map[y][x].eaten) System.out.print("\t");
                else if (map[y][x].dot) System.out.print("-\t");
                else if (map[y][x].bigDot) System.out.print("+\t");
            }
            System.out.println();
        }
        System.out.println("\n\n");
    }
}


