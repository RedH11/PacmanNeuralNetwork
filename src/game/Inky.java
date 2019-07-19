package game;

import java.util.Random;

public class Inky {
    Random num = new Random();
    int x;
    int y;
    int fitness = 0;
    private int dir;
    boolean scared = false;
    boolean alive = true;
    NeuralNetwork brain;

    // Neural Network Settings
    final int INPUTS = 7;
    final int HIDDEN_ONE = 30;
    final int HIDDEN_TWO = 15;
    final int OUTPUTS = 4;

    private int moveCounter = 0;

    private int scaredMoves = 0;
    private int maxScaredMoves = 50;

    //Initialize Inky brain
    public Inky() {
        brain = new NeuralNetwork(INPUTS, HIDDEN_ONE, HIDDEN_TWO, OUTPUTS);
        respawn();
    }

    public Inky (NeuralNetwork brain) {
        this.brain = brain;
        respawn();
    }

    //Respawns Inky
    public void respawn() {
        x = 14;
        y = 11;
        alive = true;
        scared = false;
    }

    /**
     * processes inputs through Inky's brain into outputs array
     * @param input the input data
     * @return the direction with the highest output, which Inky takes
     */
    private int think(double[] input){
        double[] outputs = brain.calculate(input);
        //double up, down, left, right;
        int highestDir = 0;

        for(int i = 1; i < outputs.length; i++){
            if(outputs[highestDir]<outputs[i]){
                highestDir = i;
            }

        }
        // outputDecision(highestDir);
        return highestDir;
    }

    /**
     * Lets Inky check immediate adjacent tiles + Pacman's position
     * @param map the game map
     * @param px pacman's x coordinate
     * @param py pacman's y coordinate
     * @return
     */
    private double[] see(Tile[][] map, int px, int py) {

        double[] inputs = new double[INPUTS];

        // Whether or not there are walls in the four blocks around it
        inputs[0] = lookUp(map);
        inputs[1] = lookLeft(map);
        inputs[2] = lookRight(map);
        inputs[3] = lookDown(map);

        // Distance from the ghost to pacman
        inputs[4] = distanceFromPac(px, py);
        // Angle from ghost to pacman
        inputs[5] = closestDirToPac(px, py);
        if (scared) inputs[6] = 1;
        else inputs[6] = 0;

        return inputs;
    }

    /**
     * Inky's move function
     * @param map the game map
     * @param px pacman's x coordinate
     * @param py pacman's y coordinate
     */
    public void move(Tile[][] map, int px, int py) {
        if (!alive) respawn();
        dir = think(see(map, px, py));

        int prevX = x;
        int prevY = y;

        // Check scared state
        if (scared) {
            scaredMoves++;
            // Moves every other turn when scared
            if (scaredMoves % 2 != 0) {
                switch (dir) {
                    case 0:
                        if (!wallInWay(dir, map)) y--;
                        break;
                    case 1:
                        if (!wallInWay(dir, map)) x--;
                        break;
                    case 2:
                        if (!wallInWay(dir, map)) x++;
                        break;
                    case 3:
                        if (!wallInWay(dir, map)) y++;
                        break;
                }
            }
        } else {
            switch (dir) {
                case 0:
                    if (!wallInWay(dir, map)) y--;
                    break;
                case 1:
                    if (!wallInWay(dir, map)) x--;
                    break;
                case 2:
                    if (!wallInWay(dir, map)) x++;
                    break;
                case 3:
                    if (!wallInWay(dir, map)) y++;
                    break;
            }
        }
        if (scaredMoves == maxScaredMoves) scared = false;

        if (prevX - x == 2) x++;
        else if (prevX - x == -2) x--;
        else if (prevY - y == 2) y++;
        else if (prevY - y == -2) y--;
    }

    /**
     * Checks for walls around Inky
     * @param dir Inky's direction
     * @param map game map
     * @return if there is a wall in the way
     */
    public boolean wallInWay(int dir, Tile[][] map) {
        if (dir == 0) {
            final int testX = x;
            final int testY = y - 1;
            if (map[testY][testX].wall) return true;
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

    public void addFitness(double add) {
        fitness += add;
    }

    public NeuralNetwork getBrain() {
        return brain;
    }

    //Calculates distance from Pacman
    public double distanceFromPac(int px, int py) {
        return Math.sqrt(Math.pow(px-x, 2) + Math.pow(py-y, 2));
    }

    // Calculates angle to pacman
    public double closestDirToPac(int px, int py) {
        return (Math.asin((py-y)/distanceFromPac(px, py)));
    }


    public double lookUp(Tile[][] map) {
        if (map[y--][x].wall) return 1;
        else return 0;
    }

    public double lookDown(Tile[][] map) {
        if (map[y++][x].wall) return 1;
        else return 0;
    }

    public double lookRight(Tile[][] map) {
        if (map[y][x++].wall) return 1;
        else return 0;
    }

    public double lookLeft(Tile[][] map) {
        if (map[y][x--].wall) return 1;
        else return 0;
    }

    // Print direction
    private void outputDecision(int highestDir) {
        if (highestDir == 0) System.out.println("Up output");
        if (highestDir == 1) System.out.println("Left output");
        if (highestDir == 2) System.out.println("Right output");
        if (highestDir == 3) System.out.println("Down output");
    }

    public int getDir() {
        return dir;
    }
}