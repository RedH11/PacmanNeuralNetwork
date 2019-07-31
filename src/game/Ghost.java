package game;

import game.NEAT.Genome;

import java.util.Random;

public class Ghost {
    Genome brain;
    Random num = new Random();
    boolean alive = true;
    boolean scared = false;
    int fitness;
    int x;
    int y;
    private int dir = 1;
    int moveChoice = 0;
    int INPUTS;

    public Ghost(Genome brain, int INPUTS){
        this.brain = brain;
        this.INPUTS = INPUTS;
        respawn();
    }
    public void respawn() {
        x = 13;
        y= 17;
        scared = false;
        alive = true;
    }

    private int think(double[] input){

        // Calculate the outputs
        double output = brain.calculate(input);
        // Save the outputs to the info storage
//        is.setNNOutputs(outputs);

        // Return left
        if (output > 0.5) return 0;
        // Return right
        else return 1;
    }

    private double[] see(Tile[][] map, int px, int py) {

        double[] inputs = new double[INPUTS];

        // First 8 inputs are the distance from pacman to the walls around him
        //  as well as how many pellets are between him and the wall in the direction he is looking
        inputs[0] = distWallUp(map);

        inputs[1] = distWallDown(map);

        inputs[2] = distWallLeft(map);

        inputs[3] = distWallRight(map);
        inputs[4] = distFromPac(px, py);
        inputs[5] = angleFromPac(px, py);
        // Gives Pacman a view of each tile around him
       // lookAround(map, inputs);

        return inputs;
    }

    // Allow Pacman to see the wall tiles around him
    private void lookAround(Tile[][] map, double[] inputs) {

        int inputsIndex = 8;

        for (int j = -1; j < 2; j++) {
            for (int i = -1; i < 2; i++) {
                if (!(i == 0 && j == 0)) {
                    if (map[y + j][x + i].wall) inputs[inputsIndex] = 1;
                    else inputs[inputsIndex] = 0;
                    //else if(map[y + j][x + i].eaten) inputs[inputsIndex] = 1;
                    //else if (map[y + j][x + i].dot || map[y + j][x + i].bigDot) inputs[inputsIndex] = 2;
                    inputsIndex++;
                }
            }
        }
    }

    public void move(Tile[][] map, GhostInfoStorage is, int px, int py) {
        if (!alive) respawn();

        moveChoice = think(see(map, px, py));

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

    public int distWallUp(Tile [][]map){
        int dist = 0;
        for(int i = 1; i < 26; i ++){
            if(map[y-i][x].wall){
                dist = i-1;
                break;
            }
        }
        return dist;
    }

    public int numPUp(Tile[][]map){
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

        return numP;
    }

    public int distWallDown(Tile [][]map){
        int dist = 0;
        for(int i = 1; i < 26; i ++){
            if(map[y+i][x].wall){
                dist = i-1;
                break;
            }
        }
        return dist;
    }

    public int numPDown(Tile[][]map){
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

        return numP;
    }

    public int distWallLeft(Tile [][]map){
        int dist = 0;
        for(int i = 1; i < 26; i ++){
            if(map[y][x-i].wall){
                dist = i-1;
                break;
            }
        }
        return dist;
    }
    public int numPLeft(Tile[][]map){
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

        return numP;
    }

    //right
    public int distWallRight(Tile [][]map){
        int dist = 0;
        for(int i = 1; i < 26; i ++){
            if(map[y][x+i].wall){
                dist = i-1;
                break;
            }
        }
        return dist;
    }
    public int numPRight(Tile[][]map){
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

        return numP;
    }
    private double distFromPac(int px, int py){
        return Math.sqrt(Math.pow(x-px, 2)+Math.pow(y-py, 2));
    }
    private double angleFromPac(int px, int py){
        return Math.asin((y-py)/distFromPac(px, py));
    }
    // Debugging Methods
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
}
