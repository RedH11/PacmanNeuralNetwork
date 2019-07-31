package game.NEAT;

import java.io.Serializable;

public class ConnectionGene implements Serializable {

    private int inNode;
    private int outNode;
    private int innovation;
    private double weight;
    private boolean expressed;

    public ConnectionGene(int inNode, int outNode, double weight, boolean expressed, int innovation) {
        this.inNode = inNode;
        this.outNode = outNode;
        this.weight = weight;
        this.expressed = expressed;
        this.innovation = innovation;
    }

    public ConnectionGene(ConnectionGene con) {
        this.inNode = con.inNode;
        this.outNode = con.outNode;
        this.weight = con.weight;
        this.expressed = con.expressed;
        this.innovation = con.innovation;
    }

    public int getInNode() {
        return inNode;
    }

    public int getOutNode() {
        return outNode;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double newWeight) {
        this.weight = newWeight;
    }

    public boolean isExpressed() {
        return expressed;
    }

    public void disable() {
        expressed = false;
    }

    public int getInnovation() {
        return innovation;
    }

    public ConnectionGene copy() {
        return new ConnectionGene(inNode, outNode, weight, expressed, innovation);
    }
}
