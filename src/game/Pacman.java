package game;

import java.util.Random;

public class Pacman {
    Random num = new Random();
    int x;
    int y;
    private int dir = num.nextInt(4); // Start pacman in a random direction
    int fitness = 0;
    // Accessible traits
    boolean powered = false;
    boolean alive = true;

    NeuralNetwork brain;

    // Neural Network Settings
    final int INPUTS = 9;
    final int HIDDEN_ONE = 30;
    //final int HIDDEN_TWO = 30;
    final int OUTPUTS = 4;

    public Pacman() {
        brain = new NeuralNetwork(INPUTS, HIDDEN_ONE, OUTPUTS);
        respawn();
    }

    public Pacman(NeuralNetwork brain) {
        this.brain = brain;
        respawn();
    }

    private void respawn() {
        x = 13; // 9
        y = 17; // 20
        powered = false;
        alive = true;
    }

    private int think(double[] input, Tile[][] map){

        double[] outputs = brain.calculate(input);

        //double up, down, left, right;
        int highestDir = 0;

        // Pacman isn't restricted to always be moving like the ghosts are
        for (int i = highestDir + 1; i < outputs.length; i++) {
            if (outputs[highestDir] < outputs[i]) { highestDir = i; }
        }

        return highestDir;
    }

    private int think(double[] input, int prevDir){

        double[] outputs = brain.calculate(input);

        //double up, down, left, right;
        int highestDir = 0;

        // Pacman isn't restricted to always be moving like the ghosts are
        for (int i = highestDir + 1; i < outputs.length; i++) {
            if (outputs[highestDir] < outputs[i] && i != prevDir) { highestDir = i; }
        }

        return highestDir;
    }

    // Print direction
    private void outputDecision(int highestDir) {
        if (highestDir == 0) System.out.println("Up output");
        if (highestDir == 1) System.out.println("Left output");
        if (highestDir == 2) System.out.println("Right output");
        if (highestDir == 3) System.out.println("Down output");
    }

    private void wallsAround(Tile[][] map) {
        if (wallInWay(0, map)) System.out.println("Wall Above");
        if (wallInWay(1, map)) System.out.println("Wall Left");
        if (wallInWay(2, map)) System.out.println("Wall Right");
        if (wallInWay(3, map)) System.out.println("Wall Below");

    }

    private double[] see(Tile[][] map) {

        int visionDistance = 1;

        // The inputs are the size of all of the tiles around packman depending on the vision distance and whether or not he is powered
        double[] inputs = new double[(int) (Math.pow(2, visionDistance + 2) + 1)];

        lookAround(map, inputs, visionDistance);

        if (powered) inputs[inputs.length - 1] = 1;
        else inputs[inputs.length - 1] = 0;

        return inputs;
    }

    private void lookAround(Tile[][] map, double[] inputs,  int visionDist) {

        int inputsIndex = 0;

        for (int j = -1; j < visionDist + 1; j++) {
            for (int i = -1; i < visionDist + 1; i++) {
                if (!(i == 0 && j == 0)) {
                    if (map[y + j][x + i].wall) inputs[inputsIndex] = 0;
                    else if(map[y + j][x + i].eaten) inputs[inputsIndex] = 1;
                    else if (map[y + j][x + i].dot) inputs[inputsIndex] = 2;
                    else if (map[y + j][x + i].bigDot) inputs[inputsIndex] = 3;
                    inputsIndex++;
                }
            }
        }
    }

    public void newMove(Tile[][] map, int prevDir) {
        if (!alive) respawn();

        dir = think(see(map), prevDir);

        int prevX = x;
        int prevY = y;

        // map[x][y]

        // 0: Up / 1: Left / 2: Right / 3: Down
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

        // Can't move twice in a turn
        if (prevX - x == 2) x++;
        else if (prevX - x == -2) x--;
        else if (prevY - y == 2) y++;
        else if (prevY - y == -2) y--;
    }

    public void move(Tile[][] map) {
        if (!alive) respawn();

        dir = think(see(map), map);

        int prevX = x;
        int prevY = y;

        // map[x][y]

        // 0: Up / 1: Left / 2: Right / 3: Down
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

        // Can't move twice in a turn
        if (prevX - x == 2) x++;
        else if (prevX - x == -2) x--;
        else if (prevY - y == 2) y++;
        else if (prevY - y == -2) y--;
    }

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

    public void addFitness(int add) {
        fitness += add;
    }

    // Generates a random direction that isn't the last one
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
}
