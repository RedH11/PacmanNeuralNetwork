package game;

import java.util.ArrayList;
import java.util.Random;

public class Pacman {
    Random num = new Random();
    int x;
    int y;
    private int []dir = {num.nextInt(4), 0};

    int fitness = 0;
    // Accessible traits
    boolean powered = false;
    boolean alive = true;
    int repeat=0;
    final int ALLOWED_REP = 5;
    // Start pacman in a random direction
    NeuralNetwork brain;

    // Neural Network Settings
    final int INPUTS = 8;
    final int HIDDEN_ONE = 20;
    int prevDir = 0;
    //final int HIDDEN_TWO = 30;
    final int OUTPUTS = 4;
    int counter = 0;
    int prevX=0;
    int prevY=0;
    int stuckCheck = 0;

    boolean escapingCorner = false;

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

    private int[] think(double[] input,InfoStorage is, Tile [][]map){

        double[] outputs = brain.calculate(input);
        is.setNNOutputs(outputs);
        ArrayList<Double> directions = new ArrayList<>();
        //double up, down, left, right;

        int highestDir = 0;
        int secondHigh=0;
        int thirdHigh = 0;
        int lastHigh = 0;

        // Pacman isn't restricted to always be moving like the ghosts are
        for (int i = 0; i < outputs.length; i++) {
            directions.add(outputs[i]);
        }
        directions.sort(Double::compareTo);
        for(int j = 0; j < outputs.length; j++){
            if(directions.get(3)==outputs[j]){
                highestDir = j;
            }
            else if(directions.get(2)== outputs[j]){
                secondHigh = j;
            }
            else if(directions.get(1)== outputs[j]){
                thirdHigh = j;
            }
            else if(directions.get(0) == outputs[j]){
                lastHigh = j;
            }
        }


        int rtDir[]={highestDir, secondHigh, thirdHigh, lastHigh};
        return  rtDir;
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

        //  int visionDistance = 1;

        // The inputs are the size of all of the tiles around packman depending on the vision distance and whether or not he is powered
        double[] inputs = new double[8];
        inputs[0] = distWallUp(map);
        inputs[1] = numPUp(map);
        inputs[2] = distWallDown(map);
        inputs[3] = numPDown(map);
        inputs[4] = distWallLeft(map);
        inputs[5] = numPLeft(map);
        inputs[6] = distWallRight(map);
        inputs[7] = numPRight(map);
        //  lookAround(map, inputs, visionDistance);

        //   if (powered) inputs[inputs.length - 1] = 1;
        // else inputs[inputs.length - 1] = 0;

        return inputs;
    }

    private void lookAround(Tile[][] map, double[] inputs,  int visionDist) {
        inputs[0] = distWallUp(map);
        inputs[1] = numPUp(map);
        inputs[2] = distWallDown(map);
        inputs[3] = numPDown(map);
        inputs[4] = distWallLeft(map);
        inputs[5] = numPLeft(map);
        inputs[6] = distWallRight(map);
        inputs[7] = numPRight(map);
    /*
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
        */
    }

    public void newMove(Tile[][] map, InfoStorage is) {
        if (!alive) respawn();

         int[] dir = think(see(map), is, map);

        cornerEscape(dir);

        if(counter == 0) {
           prevX = x;
           prevY = y;
           counter = 1;
        }
        else if(counter ==1){
            counter = 0;
        }

        // map[x][y]
        if(stuckCheck == 1 && repeat == ALLOWED_REP){
            repeat = 0;
            moveRep(map, prevDir);

        }
        // 0: Up / 1: Left / 2: Right / 3: Down
        if(repeat == ALLOWED_REP&&stuckCheck == 0){
            moveRep(map, prevDir);
            addFitness(-30);
            stuckCheck = 0;
        }

       else{
           for(int i = 0; i < dir.length; i ++){
               if(!wallInWay(dir[i], map)){
                   prevDir = dir[i];
                   switch (dir[i]) {
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
                   break;
               }
           }
           /*
            prevDir = dir[0];
            switch (dir[0]) {
                case 0:
                    if (!wallInWay(0, map)) y--;
                    else moveRep(map, dir[1]);
                    break;
                case 1:
                    if (!wallInWay(1, map)) x--;
                    else moveRep(map, dir[1]);
                    break;
                case 2:
                    if (!wallInWay(2, map)) x++;
                    else moveRep(map, dir[1]);
                    break;
                case 3:
                    if (!wallInWay(3, map)) y++;
                    else moveRep(map, dir[1]);
                    break;
            }

            */
        }


        if(x==prevX&&y==prevY){
            repeat++;
        }
        if(stuckCheck == 0){
            stuckCheck = 1;
        }
        if(stuckCheck == 1){
            stuckCheck = 0;
        }

        // Can't move twice in a turn
        if (prevX - x == 2) x++;
        else if (prevX - x == -2) x--;
        else if (prevY - y == 2) y++;
        else if (prevY - y == -2) y--;
    }

    public void cornerEscape(int[] dir) {
        // Bottom Right escape
        if (x == 26 && y == 29) dir[0] = 0;
        else if (x == 26 && y == 28) dir[0] = 0;
        else if (x == 26 && y == 27) dir[0] = 0;
        else if (x == 26 && y == 26) dir[0] = 1;
        // Bottom Left escape
        else if (x == 1 && y == 29) dir[0] = 0;
        else if (x == 1 && y == 28) dir[0] = 0;
        else if (x == 1 && y == 27) dir[0] = 0;
        else if (x == 1 && y == 26) dir[0] = 2;

    }

    public void moveRep(Tile[][] map, int dir) {
        if (!alive) respawn();
        prevDir = dir;
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
        return dir[0];
    }
    //up
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
    //down
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
    //left
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
}
