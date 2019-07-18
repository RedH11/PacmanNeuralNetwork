package game;

import java.util.Comparator;

class IncomComparator implements Comparator<GameArray> {

    @Override
    public int compare(GameArray I1, GameArray I2) {

        /* compare logic goes here
           return a negative number when o1 < o2
                  a positive number when o1 > o2
                  0 when o1 == o2
        */

        if (I1.inkyGhost.getScore() < I2.inkyGhost.getScore()) return -1;
        else if (I1.inkyGhost.getScore() > I2.inkyGhost.getScore()) return 1;
        return 0;
    }

}
