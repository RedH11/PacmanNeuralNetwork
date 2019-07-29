package game;

import java.util.ArrayList;
import java.util.Random;

public class NetworkTools {

    public static double[] createArray(int size, double init_value){
        if(size < 1){
            return null;
        }
        double[] ar = new double[size];
        for(int i = 0; i < size; i++){
            ar[i] = init_value;
        }
        return ar;
    }

    public static double[] createRandomArray(int size, double lower_bound, double upper_bound){
        if(size < 1){
            return null;
        }
        double[] ar = new double[size];
        for(int i = 0; i < size; i++){
            ar[i] = randomValue(lower_bound,upper_bound);
        }
        return ar;
    }

    public static double[][] createRandomArray(int sizeX, int sizeY, double lower_bound, double upper_bound){
        if(sizeX < 1 || sizeY < 1){
            return null;
        }
        double[][] ar = new double[sizeX][sizeY];
        for(int i = 0; i < sizeX; i++){
            ar[i] = createRandomArray(sizeY, lower_bound, upper_bound);
        }
        return ar;
    }

    public static double randomValue(double lower_bound, double upper_bound){
        return Math.random()*(upper_bound-lower_bound) + lower_bound;
    }

    // Random values that aren't repeated
    public static ArrayList<Integer> randomValues(int lowerBound, int upperBound, int amount) {

        if (upperBound < lowerBound) return null;

        Random random = new Random();
        ArrayList<Integer> arrayList = new ArrayList<Integer>();

        while (arrayList.size() < amount) { // how many numbers u need - it will 6
            int a = random.nextInt(upperBound)+lowerBound; // this will give numbers between 1 and 50.

            if (!arrayList.contains(a)) {
                arrayList.add(a);
            }
        }

        return arrayList;
    }

    public static int indexOfHighestValue(double[] values){
        int index = 0;
        for(int i = 1; i < values.length; i++){
            if(values[i] > values[index]){
                index = i;
            }
        }
        return index;
    }



}



