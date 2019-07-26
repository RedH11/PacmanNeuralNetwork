package game;

import java.io.*;
import java.util.ArrayList;

public class PacmanGame implements Serializable {

    Pacman pacman;
    boolean repeat = false;
    int numPellets;
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
    private int pelletScore = 10;
    private int poweredScore = 10;
    private int eatGhost = 0;
    private int pacEaten = 0;

    // How much fitness Inky gets for these achievements
    private int eatPacman = 200;
    private int nearPacman = 1;
    private int inkyEaten = -20;

    private int poweredMoves = 20;

    private int gameMoves = 0;
    private int scaredStart = 0;
    private int MAXMOVES;

    private InfoStorage is;
    static String PacmanDataPath;

    public PacmanGame(String PacmanDataPath, int MAXMOVES) {
        this.MAXMOVES = MAXMOVES;
        this.PacmanDataPath = PacmanDataPath;
        is = new InfoStorage(MAXMOVES);
        createMap();
        pacman = new Pacman();
      //  inky = new Inky();
       // inkyTwo = new Inky();
    }

    public PacmanGame(String PacmanDataPath, int MAXMOVES, NeuralNetwork pacmanBrain) {
        this.MAXMOVES = MAXMOVES;
        this.PacmanDataPath = PacmanDataPath;
        createMap();
        is = new InfoStorage(MAXMOVES);
        pacman = new Pacman(pacmanBrain);
        //pacman.fitness = pacmanFitness;
       // inky = new Inky(inkyBrainOne);
        //inky.fitness = inkyOneFitness;
      //  inkyTwo = new Inky(inkyBrainTwo);
        //inkyTwo.fitness = inkyTwoFitness;
    }


    public void simulateGame(int round) {
        gameMoves = 0;
        while ((numPellets < MAX_PELLETS) &&  (gameMoves < MAXMOVES)) {
            // Add all of the game information
            simulateTurn(round);
             is.addAllCoords(pacman.x, pacman.y);
            is.addAllInfo(pacman.getDir(), pacman.fitness, pacman.powered);
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

    private void simulateTurn(int round) {

        pacman.newMove(map, is);
        checkStates(round);
     //   inky.move(map, pacman.x, pacman.y, si);
       // checkStates(round);
        //inkyTwo.move(map, pacman.x, pacman.y, is);
        //checkStates(round);
    }

    private void checkStates(int round) {
        if (gameMoves - scaredStart == poweredMoves) {
            pacman.powered = false;
          //  inky.scared = false;
            //inkyTwo.scared = false;
        }
/*
        // Interaction when Pacman and Inky Collide
        if (pacman.x == inky.x && pacman.y == inky.y) {
            if (!pacman.powered) {
                pacman.alive = false;
                pacman.addFitness(pacEaten, round);
                pacman.lives--;
                inky.addFitness(eatPacman, round);
                inky.respawn(); // Inky respawns to stop it from camping pacmans spawn
                inkyTwo.respawn();
            } else {
                inky.alive = false;
                pacman.addFitness(eatGhost, round);
                inky.addFitness(inkyEaten, round);
            }
        }

        if (pacman.x == inkyTwo.x && pacman.y == inkyTwo.y) {
            if (!pacman.powered) {
                pacman.alive = false;
                pacman.addFitness(pacEaten, round);
                pacman.lives--;
                inkyTwo.addFitness(eatPacman, round);
                inkyTwo.respawn(); // Inky respawns to stop it from camping pacmans spawn
                inky.respawn();
            } else {
                inkyTwo.alive = false;
                pacman.addFitness(eatGhost, round);
                inkyTwo.addFitness(inkyEaten, round);
            }
        }

 */

        // Pacman on a pellet
        if (map[pacman.y][pacman.x].bigDot && !map[pacman.y][pacman.x].eaten) {
            pacman.powered = true;
         //   inky.scared = true;
           // inkyTwo.scared = true;
            pacman.addFitness(poweredScore);
            map[pacman.y][pacman.x].eaten = true;
            scaredStart = gameMoves;
        } else if (map[pacman.y][pacman.x].dot && !map[pacman.y][pacman.x].eaten) {
            pacman.addFitness(pelletScore);
            map[pacman.y][pacman.x].eaten = true;
            numPellets++;
        }

        // Inky in proximity to pacman (within 2 tiles)
     //   if (inky.distanceFromPac(pacman.x, pacman.y) <= Math.sqrt(5.0)) inky.addFitness(nearPacman, round);
       // if (inkyTwo.distanceFromPac(pacman.x, pacman.y) <= Math.sqrt(5.0)) inkyTwo.addFitness(nearPacman, round);
    }
/*
    public Inky getBestInky(){
        double xAvg = (inky.fitness + inky.fitness2 + inky.fitness3) / 3;
        double yAvg = (inkyTwo.fitness + inkyTwo.fitness2 + inkyTwo.fitness3) / 3;

        if (xAvg < yAvg) return inkyTwo;
        else if (xAvg > yAvg) return inky;
        return inky;
    }

    public int getBestInkyAverageFitness() {
        Inky tempI = getBestInky();
        return ((tempI.fitness + tempI.fitness2 + tempI.fitness3) / 3);
    }

 */

    public InfoStorage getIs() {
        return is;
    }

    public static void saveInformation(InfoStorage infoStorage, ObjectOutputStream oos) throws IOException, ClassNotFoundException {

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

    private static ArrayList<InfoStorage> readObjectsFromFile(String filename) throws IOException, ClassNotFoundException {
        ArrayList<InfoStorage> objects = new ArrayList<>();
        InputStream is = null;
        try {
            is = new FileInputStream(filename);
            ObjectInputStream ois = new ObjectInputStream(is);
            while (true) {
                try {
                    InfoStorage object = (InfoStorage) ois.readObject();
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
              //  else if (x == inky.x && y == inky.y) System.out.print("8\t");
                //else if (x == inkyTwo.x && y == inkyTwo.y) System.out.print("8\t");
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


