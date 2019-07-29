package game;

import java.io.*;
import java.util.ArrayList;

public class PacmanGame implements Serializable {

    Pacman pacman;
    Ghost ghostOne;
    Ghost ghostTwo;
    Ghost ghostThree;
    Ghost ghostFour;

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
    private int pelletScore = 1;
    private int poweredScore = 1;

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
        ghostOne = new Ghost();
        ghostTwo = new Ghost();
        ghostThree = new Ghost();
        ghostFour = new Ghost();
    }

    public PacmanGame(String PacmanDataPath, int MAXMOVES, NeuralNetwork pacmanBrain, NeuralNetwork ghostBrain) {
        this.MAXMOVES = MAXMOVES;
        this.PacmanDataPath = PacmanDataPath;
        createMap();
        is = new InfoStorage(MAXMOVES);
        pacman = new Pacman(pacmanBrain);
        ghostOne = new Ghost(ghostBrain);
        ghostTwo = new Ghost(ghostBrain);
        ghostThree = new Ghost(ghostBrain);
        ghostFour = new Ghost(ghostBrain);
    }
    public PacmanGame(String PacmanDataPath, int MAXMOVES, NeuralNetwork pacmanBrain) {
        this.MAXMOVES = MAXMOVES;
        this.PacmanDataPath = PacmanDataPath;
        createMap();
        is = new InfoStorage(MAXMOVES);
        pacman = new Pacman(pacmanBrain);
        ghostOne = new Ghost();
        ghostTwo = new Ghost();
        ghostThree = new Ghost();
        ghostFour = new Ghost();
    }


    public void simulateGame() {
        gameMoves = 0;
        while ((numPellets < MAX_PELLETS) &&  (gameMoves < MAXMOVES)&&pacman.lives > 0) {
            // Add all of the game information
            simulateTurn();
            is.addAllCoords(pacman.x, pacman.y, ghostOne.x, ghostOne.y, ghostTwo.x, ghostTwo.y, ghostThree.x, ghostThree.y, ghostFour.x, ghostFour.y);
            is.addAllInfo(pacman.moveChoice, ghostOne.moveChoice, ghostTwo.moveChoice, ghostThree.moveChoice, ghostFour.moveChoice, pacman.getDir(), pacman.fitness,ghostOne.fitness, ghostTwo.fitness, ghostThree.fitness, ghostFour.fitness, pacman.powered);
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
        pacman.move(map, is);
        checkStates();
        ghostOne.move(map, is, pacman.x, pacman.y);
        checkStates();
        ghostTwo.move(map, is, pacman.x, pacman.y);
        checkStates();
        ghostThree.move(map, is, pacman.x, pacman.y);
        checkStates();
        ghostThree.move(map, is, pacman.x, pacman.y);
        checkStates();
    }

    private void checkStates() {

        if (gameMoves - scaredStart == poweredMoves) { pacman.powered = false; }

        // Pacman on a pellet
        if (map[pacman.y][pacman.x].bigDot && !map[pacman.y][pacman.x].eaten) {
            pacman.powered = true;
            pacman.addFitness(poweredScore);
            map[pacman.y][pacman.x].eaten = true;
            scaredStart = gameMoves;
        } else if (map[pacman.y][pacman.x].dot && !map[pacman.y][pacman.x].eaten) {
            pacman.addFitness(pelletScore);
            map[pacman.y][pacman.x].eaten = true;
            numPellets++;
        }
        if((pacman.x == ghostOne.x)&&(pacman.y == ghostOne.y)){
            if(pacman.powered){
                ghostOne.addFitness(-20);
                ghostOne.respawn();

            }
            else{
                ghostOne.addFitness(100);
                pacman.addFitness(-100);
                pacman.lives--;
                pacman.respawn();
                ghostOne.respawn();
                ghostTwo.respawn();
                ghostThree.respawn();
                ghostFour.respawn();
            }
        }
        if((pacman.x == ghostTwo.x)&&(pacman.y == ghostTwo.y)){
            if(pacman.powered){
                ghostTwo.addFitness(-20);
                ghostTwo.respawn();

            }
            else{
                ghostTwo.addFitness(100);
                pacman.addFitness(-100);
                pacman.lives--;
                pacman.respawn();
                ghostOne.respawn();
                ghostTwo.respawn();
                ghostThree.respawn();
                ghostFour.respawn();
            }
        }
        if((pacman.x == ghostThree.x)&&(pacman.y == ghostThree.y)){
            if(pacman.powered){
                ghostThree.addFitness(-20);
                ghostThree.respawn();

            }
            else{
                ghostThree.addFitness(100);
                pacman.addFitness(-100);
                pacman.lives--;
                pacman.respawn();
                ghostOne.respawn();
                ghostTwo.respawn();
                ghostThree.respawn();
                ghostFour.respawn();
            }
        }
        if((pacman.x == ghostFour.x)&&(pacman.y == ghostFour.y)){
            if(pacman.powered){
                ghostFour.addFitness(-20);
                ghostFour.respawn();

            }
            else{
                ghostFour.addFitness(100);
                pacman.addFitness(-100);
                pacman.lives--;
                pacman.respawn();
                ghostOne.respawn();
                ghostTwo.respawn();
                ghostThree.respawn();
                ghostFour.respawn();
            }
        }
    }

    public InfoStorage getIs() {
        return is;
    }
    public Ghost getBestGhost(){
        Ghost[]temp = {ghostOne, ghostTwo, ghostThree, ghostFour};
        for(int i = 0; i < 3; i++){
            if(temp[i+1].fitness < temp[i].fitness){
                Ghost flip = temp[i];
                temp[i] = temp[i+1];
                temp[i+1] =flip;
            }
        }
        return temp[3];
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


