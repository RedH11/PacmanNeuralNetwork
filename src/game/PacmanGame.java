package game;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class PacmanGame {

    Pacman pacman;
    Inky inky;
    Inky inkyTwo;
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

    FileWriter moveWriter;
    String fileContent = "";
    String PacmanDataPath;

    public PacmanGame(String PacmanDataPath, int MAXMOVES) {
        this.MAXMOVES = MAXMOVES;
        this.PacmanDataPath = PacmanDataPath;

        /*File folder = new File(PacmanDataPath);
        File[] listOfFiles = folder.listFiles();
        uniqueNum = listOfFiles.length;*/

        createMap();
        pacman = new Pacman();
      //  inky = new Inky();
       // inkyTwo = new Inky();
    }

    public PacmanGame(String PacmanDataPath, int MAXMOVES, NeuralNetwork pacmanBrain, int pacmanFitness) {
        this.MAXMOVES = MAXMOVES;
        this.PacmanDataPath = PacmanDataPath;
        createMap();
        pacman = new Pacman(pacmanBrain);
        pacman.fitness = pacmanFitness;
      //  inky = new Inky(inkyBrainOne);
       // inky.fitness = inkyOneFitness;
        //inkyTwo = new Inky(inkyBrainTwo);
        //inkyTwo.fitness = inkyTwoFitness;
    }


    public void simulateGame(int round) {
        gameMoves = 0;
        while ((numPellets < MAX_PELLETS) && (pacman.lives > 0) && (gameMoves < MAXMOVES)) {
            fileContent += "P: " + pacman.x + " " + pacman.y +  " Pdir: " + pacman.getDir() + " Pfit: " + pacman.fitness + " Ppowered: " + pacman.powered + "\n";
            simulateTurn(round);
            gameMoves++;
        }
    }

    public void writeFileContent(int generation, int fileNum, boolean pacmanFile) {

        try {
            if (pacmanFile) moveWriter = new FileWriter(PacmanDataPath +"/Gens/PacGen_" + generation);
            else moveWriter = new FileWriter(PacmanDataPath +"/Gens/Inkgen_" + generation);

        } catch (Exception ex) {
            System.out.println("Error creating move writer");
        }

        // Write the file content to the file
        try {
            moveWriter.write(fileContent);
            moveWriter.close();
        } catch (IOException ex) {}
    }

    public int getInkyFitness() {
        return inky.fitness;
    }
    public int getInkyTwoFitness(){
        return inkyTwo.fitness;
    }

    public int getPacmanFitness() {
        return pacman.fitness;
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
              //  else if (x == inky.x && y == inky.y) System.out.print("8\t");
               // else if (x == inkyTwo.x && y == inkyTwo.y) System.out.print("8\t");
                else if (map[y][x].wall) System.out.print("0\t");
                else if (map[y][x].eaten) System.out.print("\t");
                else if (map[y][x].dot) System.out.print("-\t");
                else if (map[y][x].bigDot) System.out.print("+\t");
            }
            System.out.println();
        }
        System.out.println("\n\n");
    }

    private void simulateTurn(int round) {
        pacman.move(map);
        checkStates(round);
       // inky.move(map, pacman.x, pacman.y);
        //checkStates(round);
       // inkyTwo.move(map, pacman.x, pacman.y);
        //checkStates(round);
    }

    private void checkStates(int round) {
        if (gameMoves - scaredStart == poweredMoves) {
            pacman.powered = false;
          //  inky.scared = false;
           // inkyTwo.scared = false;
        }

      /*  // Interaction when Pacman and Inky Collide
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
        //    inky.scared = true;
          //  inkyTwo.scared = true;
            pacman.addFitness(poweredScore);
            map[pacman.y][pacman.x].eaten = true;
            scaredStart = gameMoves;
        } else if (map[pacman.y][pacman.x].dot && !map[pacman.y][pacman.x].eaten) {
            pacman.addFitness(pelletScore);
            map[pacman.y][pacman.x].eaten = true;
            numPellets++;
        }

        // Inky in proximity to pacman (within 2 tiles)
      //  if (inky.distanceFromPac(pacman.x, pacman.y) <= Math.sqrt(5.0)) inky.addFitness(nearPacman, round);
      //  if (inkyTwo.distanceFromPac(pacman.x, pacman.y) <= Math.sqrt(5.0)) inkyTwo.addFitness(nearPacman, round);
    }
/*
    public Inky getBestInky(){

        double xAvg = (inky.fitness + inky.fitness2 + inky.fitness3) / 3;
        double yAvg = (inkyTwo.fitness + inkyTwo.fitness2 + inkyTwo.fitness3) / 3;

        if (xAvg < yAvg) return inkyTwo;
        else if (xAvg > yAvg) return inky;
        return inky;

    }

 */
    public void WriteObjectToFile(Object serObj, int generation) {

        try {

            FileOutputStream fileOut = new FileOutputStream(PacmanDataPath +"/Gens/InkGenes_" + generation);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(serObj);
            objectOut.close();
            System.out.println("The Object  was succesfully written to a file");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
