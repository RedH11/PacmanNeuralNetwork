package game.NEAT;

import java.util.*;

public class Species {

    public Genome mascot;
    public List<Genome> members;
    public List<FitnessGenome> fitnessPop;
    public double totalAdjustedFitness = 0;
    public int plateauCount = 0;

    public Species(Genome mascot) {
        this.mascot = mascot;
        this.members = new LinkedList<Genome>();
        this.members.add(mascot);
        this.fitnessPop = new ArrayList<FitnessGenome>();
    }

    /* Selects new random mascot + clear members + set totaladjustedfitness to 0 */
    public void reset(Random r) {
        //int newMascotIndex = r.nextInt(members.size());
        //this.mascot = members.get(newMascotIndex);
        members.clear();
        fitnessPop.clear();
        totalAdjustedFitness = 0;
    }

    public void setMascot(Genome mascot) {
        this.mascot = mascot;
    }

    public void addAdjustedFitness(double adjustedFitness) {
        this.totalAdjustedFitness += adjustedFitness;
    }
}


