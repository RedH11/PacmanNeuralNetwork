package game.NEAT;

// Counter for the innovation number of genomes (every time this method is called it adds one)
public class Counter {

    private int currentInnovation = 0;

    public int getInnovation() { return currentInnovation++; }

    public int getCurrentInnovation() { return currentInnovation; }
}
