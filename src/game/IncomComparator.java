package game;

import java.util.Comparator;

class IncomComparator implements Comparator<InkyGhost> {

    @Override
    public int compare(InkyGhost I1, InkyGhost I2) {

        /* compare logic goes here
           return a negative number when o1 < o2
                  a positive number when o1 > o2
                  0 when o1 == o2
        */

        if (I1.getScore() < I2.getScore()) return -1;
        else if (I1.getScore() > I2.getScore()) return 1;
        return 0;
    }

}
