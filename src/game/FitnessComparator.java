package game;

import java.util.Comparator;

class InkyFitnessComparator implements Comparator<PacmanGame> {

    @Override
    public int compare(PacmanGame x, PacmanGame y) {
        /* compare logic goes here
           return a negative number when o1 < o2
                  a positive number when o1 > o2
                  0 when o1 == o2
        */

        double xAvg = (x.getBestInky().fitness + x.getBestInky().fitness2 + x.getBestInky().fitness3) / 3;
        double yAvg = (y.getBestInky().fitness + y.getBestInky().fitness2 + y.getBestInky().fitness3) / 3;

        if (xAvg < yAvg) return -1;
        else if (xAvg > yAvg) return 1;
        return 0;
    }
}

class PacmanFitnessComparator implements Comparator<PacmanGame> {
    @Override
    public int compare(PacmanGame x, PacmanGame y) {
        /* compare logic goes here
           return a negative number when o1 < o2
                  a positive number when o1 > o2
                  0 when o1 == o2
        */

        double xAvg = (x.pacman.fitness + x.pacman.fitness2 + x.pacman.fitness3) / 3;
        double yAvg = (y.pacman.fitness + y.pacman.fitness2 + y.pacman.fitness3) / 3;

        if (xAvg < yAvg) return -1;
        else if (xAvg > yAvg) return 1;
        return 0;
    }
}
