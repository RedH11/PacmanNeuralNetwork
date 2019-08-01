package game;

import java.util.Random;
/*
This is the pacman object. The pacman object has the move, thinking, and fitness functions for simulating the game.
 */
public class Pacman {
    Random num = new Random();
    int x;
    int y;
    // The cardinal direction that pacman is currently travelling
    private int dir = 1; // Starts going west
    // 0: Left Turn, 1: Right Turn
    int moveChoice = 0;

    int fitness = 0;
    boolean powered = false;
    boolean alive = true;
    NeuralNetwork brain;

    // Neural Network Settings
    final int INPUTS = 16;
    final int HIDDEN_ONE = 40;
    final int HIDDEN_TWO = 20;
    final int OUTPUTS = 2;

    /**
     * Makes a pacman brain and respawns Pacman
     */
    public Pacman() {
        brain = new NeuralNetwork(INPUTS, HIDDEN_ONE, HIDDEN_TWO, OUTPUTS);
        respawn();
    }

    /**
     *
     * @param brain Initialize pacman brain
     */
    public Pacman(NeuralNetwork brain) {
        this.brain = brain;
        respawn();
    }

    /**
     * Respawns pacman in the middle
     */
    private void respawn() {
        x = 13;
        y = 17;
        powered = false;
        alive = true;
    }

    /**
     *
     * @param input input vector
     * @param is infostorage object to save info to
     * @param map the map object
     * @return returns highest output from pacman's brain
     */
    private int think(double[] input, InfoStorage is, Tile[][] map){

        // Calculate the outputs
        double[] outputs = brain.calculate(input);
        // Save the outputs to the info storage
        is.setNNOutputs(outputs);
        // Saves the weights and biases to the info storage
        is.setNNInfo(brain.getArrayWeights(), brain.getArrayBias());
        // Find the highest output
        int highestIndex = 0;
        for (int i = 1; i < outputs.length; i++) {
            if (outputs[highestIndex] < outputs[i]) highestIndex = i;
        }

        return highestIndex;
    }

    /**
     * Processes map data around Pacman into inputs
     * @param map the man
     * @return inputs as a vector
     */
    private double[] see(Tile[][] map) {

        double[] inputs = new double[INPUTS];

        // First 8 inputs are the distance from pacman to the walls around him
        //  as well as how many pellets are between him and the wall in the direction he is looking
        inputs[0] = distWallUp(map);
        inputs[1] = numPUp(map);
        inputs[2] = distWallDown(map);
        inputs[3] = numPDown(map);
        inputs[4] = distWallLeft(map);
        inputs[5] = numPLeft(map);
        inputs[6] = distWallRight(map);
        inputs[7] = numPRight(map);
        // Gives Pacman a view of each tile around him
        lookAround(map, inputs);

        return inputs;
    }

    /**
     * Lets pacman see the wall inputs around him
     * @param map map object
     * @param inputs
     */
    private void lookAround(Tile[][] map, double[] inputs) {

        int inputsIndex = 8;

        for (int j = -1; j < 2; j++) {
            for (int i = -1; i < 2; i++) {
                if (!(i == 0 && j == 0)) {
                    if (map[y + j][x + i].wall) inputs[inputsIndex] = 1;
                    else inputs[inputsIndex] = 0;
                    inputsIndex++;
                }
            }
        }
    }

    /**
     * Pacman's move function, where he moves depending on the output from think()
     * @param map map
     * @param is infostorage object to save moves to
     */
    public void move(Tile[][] map, InfoStorage is) {
        if (!alive) respawn();

        moveChoice = think(see(map), is, map);

        makeTurn(map);

        switch (dir) {
           case 0:
               if (!wallInWay(0, map)) y--;
               break;
           case 1:
               if (!wallInWay(1, map)) x--;
               break;
           case 2:
               if (!wallInWay(2, map)) x++;
               break;
           case 3:
               if (!wallInWay(3, map)) y++;
               break;
        }


    }

    /**
     * Move function for Pacman's turn
     * @param map
     */
    private void makeTurn(Tile[][] map) {
        // If travelling west, wants to turn left, and there is no wall below allow it to go down
        if (dir == 1 && moveChoice == 0 && !wallInWay(3, map)) dir = 3;
        // If travelling west, wants to turn right, and there is no wall above allow it to go up
        else if (dir == 1 && moveChoice == 1 && !wallInWay(0, map)) dir = 0;
        // If travelling east, wants to turn left, and there is no wall above allow it to go up
        else if (dir == 2 && moveChoice == 0 && !wallInWay(0, map)) dir = 0;
        // If travelling east, wants to turn right, and there is no wall below allow it to go down
        else if (dir == 2 && moveChoice == 1 && !wallInWay(3, map)) dir = 3;
        // If travelling south, wants to turn left, and there is no wall to the east allow it to go east
        else if (dir == 3 && moveChoice == 0 && !wallInWay(2, map)) dir = 2;
        // If travelling south, wants to turn right, and there is no wall to it's left allow it to go west
        else if (dir == 3 && moveChoice == 1 && !wallInWay(1, map)) dir = 1;
        // If travelling up, wants to turn left, and there is no wall to the left allow it to go west
        else if (dir == 0 && moveChoice == 0 && !wallInWay(1, map)) dir = 1;
        // If travelling up, wants to turn right, and there is no wall to the right allow it to go east
        else if (dir == 0 && moveChoice == 1 && !wallInWay(2, map)) dir = 2;
    }

    /**
     * Checks for a wall in the way
     * @param dir pacman's direction
     * @param map
     * @return boolean
     */
    private boolean wallInWay(int dir, Tile[][] map) {
        if (dir == 0) {
            final int testX = x;
            final int testY = y - 1;
            if (map[testY][testX].wall || testY == 0) return true;
        } else if (dir == 1) {
            final int testX = x - 1;
            final int testY = y;
            if (map[testY][testX].wall) return true;
        } else if (dir == 2) {
            final int testX = x + 1;
            final int testY = y;
            if (map[testY][testX].wall) return true;
        } else if (dir == 3) {
            final int testX = x;
            final int testY = y + 1;
            if (map[testY][testX].wall) return true;
        }
        return false;
    }

    /**
     * Adds to Pacman's fitness
     * @param add
     */
    public void addFitness(int add) {
        fitness += add;
    }

    /**
     * Generates a random direction that isn't the last one
     * @param lastDir Pacman's last direction
     * @return a new direction
     */
    private int randDir(int lastDir) {
        int dir = num.nextInt(4);
        while (dir == lastDir) {
            dir = num.nextInt(4);
        }

        return dir;
    }


    public int getDir() {
        return dir;
    }

    /**
     * @param map
     * @return distance from wall above pacman
     */
    public int distWallUp(Tile [][]map){
        int dist = 0;
        for(int i = 1; i < 26; i ++){
            if(map[y-i][x].wall){
                dist = i-1;
                break;
            }
        }
        return dist / 26;
    }

    /**
     * @param map
     * @return number of pellets above pacman
     */
    public double numPUp(Tile[][]map){
        int numP = 0;
        for(int i = y; i > 1; i--) {
            if (!map[i][x].wall) {
                if (map[i][x].dot || map[i][x].bigDot) {
                    numP++;
                }
            }
            else{
                break;
            }
        }

        return sigmoid(numP);
    }
    /**
     * @param map
     * @return distance from wall below Pacman
     */
    public double distWallDown(Tile [][]map){
        int dist = 0;
        for(int i = 1; i < 26; i ++){
            if(map[y+i][x].wall){
                dist = i-1;
                break;
            }
        }
        return dist / 26;
    }
    /**
     * @param map
     * @return number of pellets below pacman
     */
    public double numPDown(Tile[][]map){
        int numP = 0;
        for(int i = y; i < 26; i++) {
            if (!map[i][x].wall) {
                if (map[i][x].dot||map[i][x].bigDot) {
                    numP++;
                }
            }
            else{
                break;
            }
        }

        return sigmoid(numP);
    }
    /**
     * @param map
     * @return distance from wall to the left of Pacman
     */
    public double distWallLeft(Tile [][]map){
        int dist = 0;
        for(int i = 1; i < 26; i ++){
            if(map[y][x-i].wall){
                dist = i-1;
                break;
            }
        }
        return dist/26;
    }

    /**
     *
     * @param map
     * @return number of pellets to the left of Pacman
     */
    public double numPLeft(Tile[][]map){
        int numP = 0;
        for(int i = x; i > 1; i--) {
            if (!map[y][i].wall) {
                if (map[y][i].dot||map[y][i].bigDot) {
                    numP++;
                }
            }
            else{
                break;
            }
        }

        return sigmoid(numP);
    }

    /**
     * @param map
     * @return distance from wall to the right of Pacman
     */    public double distWallRight(Tile [][]map){
        int dist = 0;
        for(int i = 1; i < 26; i ++){
            if(map[y][x+i].wall){
                dist = i-1;
                break;
            }
        }
        return dist / 26;
    }

    /**
     *
     * @param map
     * @return number of pellets to the right of Pacman
     */
    public double numPRight(Tile[][]map){
        int numP = 0;
        for(int i = x; i < 26; i++) {
            if (!map[y][i].wall) {
                if (map[y][i].dot||map[y][i].bigDot) {
                    numP++;
                }
            }
            else{
                break;
            }
        }

        return sigmoid(numP);
    }

    // Debugging Methods

    /**
     * Prints direction
     */
    private void outputDecision(int highestDir) {
        if (highestDir == 0) System.out.println("Up output");
        if (highestDir == 1) System.out.println("Left output");
        if (highestDir == 2) System.out.println("Right output");
        if (highestDir == 3) System.out.println("Down output");
    }

    /**
     * Prints walls in the way of Pacman
     * @param map
     */
    private void wallsAround(Tile[][] map) {
        if (wallInWay(0, map)) System.out.println("Wall Above");
        if (wallInWay(1, map)) System.out.println("Wall Left");
        if (wallInWay(2, map)) System.out.println("Wall Right");
        if (wallInWay(3, map)) System.out.println("Wall Below");
    }

    /**
     * Sigmoid function
     * @param x variable to pass in
     * @return sigmoid of x
     */
    private double sigmoid(double x) {
        return (1 / (1 + Math.pow(Math.E, -x)));
    }
}
