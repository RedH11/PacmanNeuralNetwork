package game;

import java.util.Random;

public class Pacman {
    Random num = new Random();
    int x;
    int y;
    private int dir = num.nextInt(4); // Start pacman in a random direction
    int fitness = 0;
    int fitness2 = 0;
    int fitness3 = 0;
    int lives = 3;
    // Accessible traits
    boolean powered = false;
    boolean alive = true;

    NeuralNetwork brain;

    // Neural Network Settings
    final int INPUTS = 9;
    final int HIDDEN_ONE = 80;
    final int HIDDEN_TWO = 50;
    final int OUTPUTS = 4;

    public Pacman() {
        brain = new NeuralNetwork(INPUTS, HIDDEN_ONE, HIDDEN_TWO, OUTPUTS);
        respawn();
        lives = 3;
    }

    public Pacman(NeuralNetwork brain) {
        this.brain = brain;
        respawn();
        lives = 3;
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

        while (wallInWay(highestDir, map)) {
            highestDir++;
        }

        if (!(highestDir == 3)) {
            // Pacman isn't restricted to always be moving like the ghosts are
            for (int i = highestDir + 1; i < outputs.length; i++) {
                if (outputs[highestDir] < outputs[i]) {
                    if (!wallInWay(i, map)) highestDir = i;
                }
            }
        }

        /*
        while (wallInWay(highestDir, map)) {
            highestDir = randDir(highestDir);
        }*/

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

    private double[] see(Tile[][] map, int Ix, int Iy, int I2x, int I2y) {

        int visionDistance = 1;

        // The inputs are the size of all of the tiles around packman depending on the vision distance and whether or not he is powered
        double[] inputs = new double[(int) (Math.pow(2, visionDistance + 2) + 1)];

        lookAround(map, inputs, Ix, Iy, I2x, I2y, visionDistance);

        /*
        // Whether or not there are walls in the four blocks around it
        inputs[0] = lookUp(map);
        inputs[1] = lookLeft(map);
        inputs[2] = lookRight(map);
        inputs[3] = lookDown(map);
        inputs[4] = lookTwoUp(map);
        inputs[5] = lookTwoLeft(map);
        inputs[6] = lookTwoRight(map);
        inputs[7] = lookTwoDown(map);
        // Distance from the ghost to pacman
        inputs[4] = distanceFromInky(Ix,Iy);
        // Angle from ghost to pacman
        inputs[5] = closestDirToInky(Ix, Iy);
        inputs[6] = distanceFromInky(I2x, I2y);
        inputs[7] = closestDirToInky(I2x, I2y);*/
        if (powered) inputs[inputs.length - 1] = 1;
        else inputs[inputs.length - 1] = 0;


        /*System.out.println("Inputs");

        for (int i = 0; i < inputs.length; i++) {
            System.out.println(inputs[i]);
        }
        System.out.println();
        System.out.println("Outputs");*/

        return inputs;
    }

    private void lookAround(Tile[][] map, double[] inputs, int Ix, int Iy, int I2x, int I2y, int visionDist) {

        int inputsIndex = 0;

        for (int j = -1; j < visionDist + 1; j++) {
            for (int i = -1; i < visionDist + 1; i++) {
                if (!(i == 0 && j == 0)) {
                    if ((y + j == Iy && x + i == Ix) || (y + j == I2y && x + i == I2x)) inputs[inputsIndex] = 3;
                    else if (map[y + j][x + i].wall) inputs[inputsIndex] = 2;
                    else if (map[y + j][x + i].dot) inputs[inputsIndex] = 1;
                    else if (map[y + j][x + i].bigDot) inputs[inputsIndex] = 0;
                    inputsIndex++;
                }
            }
        }
    }

    //Calculates distance from Inky
    private double distanceFromInky(int ix, int iy) {
        return Math.sqrt(Math.pow(ix-x, 2) + Math.pow(iy-y, 2));
    }

    // Calculates angle to Inky
    private double closestDirToInky(int ix, int iy) {
        return (Math.asin((iy-y)/distanceFromInky(ix, iy)));
    }

    private double lookUp(Tile[][] map) {
        if (wallInWay(0, map)) return 1;
        else if(map[y-1][x].dot){
            return 0.33;
        }
        else if(map[y-1][x].bigDot){
            return 0.66;
        }
        else {
            return 0;
        }
    }

    private double lookDown(Tile[][] map) {
        if (wallInWay(3, map)) return 1;
        else if(map[y+1][x].dot){
            return 0.33;
        }
        else if(map[y+1][x].bigDot){
            return 0.66;
        }
        else {
            return 0;
        }
    }

    private double lookRight(Tile[][] map) {
        if (wallInWay(2, map)) return 1;
        else if(map[y][x+1].dot){
            return 0.33;
        }
        else if(map[y][x+1].bigDot){
            return 0.66;
        }
        else {
            return 0;
        }
    }

    public double lookLeft(Tile[][] map) {
        if (wallInWay(1, map)) return 1;
        else if(map[y][x-1].dot){
            return 0.33;
        }
        else if(map[y][x-1].bigDot){
            return 0.66;
        }
        else {
            return 0;
        }
    }

    public void move(Tile[][] map, int Ix, int Iy, int I2x, int I2y) {
        if (!alive) respawn();

        dir = think(see(map, Ix, Iy, I2x, I2y), map);

        int prevX = x;
        int prevY = y;

        // map[x][y]

        // 0: Up / 1: Left / 2: Right / 3: Down
        switch (dir) {
            case 0:
                y--;
                break;
            case 1:
                x--;
                break;
            case 2:
                x++;
                break;
            case 3:
                y++;
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

    public void addFitness(int add, int round) {
        if (round == 1) fitness += add;
        else if (round == 2) fitness2 += add;
        else fitness3 += add;
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