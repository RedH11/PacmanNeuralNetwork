package game;

import java.util.Comparator;

class FitnessComparator implements Comparator<PacmanGame> {

    @Override
    public int compare(PacmanGame x, PacmanGame y) {
        /* compare logic goes here
           return a negative number when o1 < o2
                  a positive number when o1 > o2
                  0 when o1 == o2
        */
        if (x.getInkyFitness() < y.getInkyFitness()) return -1;
        else if (x.getInkyFitness() > y.getInkyFitness()) return 1;
        return 0;
    }

}
