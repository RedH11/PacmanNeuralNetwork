package game.NEAT;

public class FitnessGenome {

    public double fitness;
    public Genome genome;

    public FitnessGenome(Genome genome, double fitness) {
        this.genome = genome;
        this.fitness = fitness;
    }
}

