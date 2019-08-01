package game.NEAT;

import java.util.*;

public class Genome {

    private Map<Integer, ConnectionGene> connections;
    private Map<Integer, NodeGene> nodes;

    private static List<Integer> tmpList1 = new ArrayList<Integer>();
    private static List<Integer> tmpList2 = new ArrayList<Integer>();

    public double ADJUST_WEIGHT_RATE = 0.9; // 90% chance of adjusting node weight,
                                            // else replaces weight with random one
    private static int INPUTS;

    public Genome(int INPUTS) {
        this.INPUTS = INPUTS;
        nodes = new HashMap<Integer, NodeGene>();
        connections = new HashMap<Integer, ConnectionGene>();
    }

    public Genome(Genome toBeCopied, int INPUTS) {
        this.INPUTS = INPUTS;
        nodes = new HashMap<Integer, NodeGene>();
        connections = new HashMap<Integer, ConnectionGene>();

        for (Integer index : toBeCopied.getNodes().keySet()) {
            nodes.put(index, new NodeGene(toBeCopied.getNodes().get(index)));
        }

        for (Integer index : toBeCopied.getConnections().keySet()) {
            connections.put(index, new ConnectionGene(toBeCopied.getConnections().get(index)));
        }
    }

    public double calculate(double[] input) {

        if (input.length != INPUTS) {
            System.out.println("Wrong Input Data Size");
            return 0;
        }

        int inputIndx = 0;

        // Put the inputs into the NEAT input nodes
        for (NodeGene n : nodes.values()) {
            if (n.getType() == NodeGene.TYPE.INPUT){
                n.value = input[inputIndx];
                inputIndx++;
            }
        }

        double sum = 0;

        // Run through the hidden nodes and set their values using sigmoid function from sum of previous nodes
        for (NodeGene currentNode : nodes.values()) {
            if (currentNode.getType() == NodeGene.TYPE.HIDDEN) {
                // Run through all expressed connections with Hidden Nodes
                for (ConnectionGene currentConnection : connections.values()) {
                    if (currentConnection.isExpressed() && currentConnection.getOutNode() == currentNode.getId()) {
                            sum += nodes.get(currentConnection.getInNode()).value * currentConnection.getWeight();
                    }
                }
                currentNode.value = sigmoid(sum);
            }
        }

        int outputIndex = 0;

        for (NodeGene n : nodes.values()) {
            if (n.getType() == NodeGene.TYPE.OUTPUT) outputIndex = n.getId();
        }

        sum = 0;

        // Run through connections to the output node
        for (ConnectionGene currentConnection : connections.values()) {
            // If the node is expressed and is connected to the output node
            if (currentConnection.isExpressed() && currentConnection.getOutNode() == outputIndex) {
                sum += nodes.get(currentConnection.getInNode()).value * currentConnection.getWeight();
            }
        }
        // And put them into a sigmoid function
        double output = sigmoid(sum);

        return output;
    }

    // Compatibility distances allow us to define which genes are in which species
    // Equation -> Distance = (c1 * Excess) / Num of genes in structure + (c2 * Disjointed) / Num of genes in structure + c3 * (Avg Weight Difference)
    public static double compatibilityDistance(Genome genome1, Genome genome2, double c1, double c2, double c3) {
        int excessGenes = countExcessGenes(genome1, genome2);
        int disjointGenes = countDisjointGenes(genome1, genome2);
        double avgWeightDiff = averageWeightDiff(genome1, genome2);

        // If small enough shouldn't need to divide by total amount of neurons
        return excessGenes * c1 + disjointGenes * c2 + avgWeightDiff * c3;
    }

    private int countMatchingGenes(Genome genome1, Genome genome2) {
        int matchingGenes = 0;

        List<Integer> nodeKeys1 = asSortedList(genome1.getNodes().keySet(), tmpList1);
        List<Integer> nodeKeys2 = asSortedList(genome2.getNodes().keySet(), tmpList2);

        int highestInnovation1 = nodeKeys1.get(nodeKeys1.size()-1);
        int highestInnovation2 = nodeKeys2.get(nodeKeys2.size()-1);
        int indices = Math.max(highestInnovation1, highestInnovation2);

        for (int i = 0; i <= indices; i++) {  // loop through genes -> i is innovation numbers
            NodeGene node1 = genome1.getNodes().get(i);
            NodeGene node2 = genome2.getNodes().get(i);
            if (node1 != null && node2 != null) {
                // both genomes has the gene w/ this innovation number
                matchingGenes++;
            }
        }

        List<Integer> conKeys1 = asSortedList(genome1.getConnections().keySet(), tmpList1);
        List<Integer> conKeys2 = asSortedList(genome2.getConnections().keySet(), tmpList2);

        highestInnovation1 = conKeys1.get(conKeys1.size()-1);
        highestInnovation2 = conKeys2.get(conKeys2.size()-1);

        indices = Math.max(highestInnovation1, highestInnovation2);
        for (int i = 0; i <= indices; i++) { 					// loop through genes -> i is innovation numbers
            ConnectionGene connection1 = genome1.getConnections().get(i);
            ConnectionGene connection2 = genome2.getConnections().get(i);
            if (connection1 != null && connection2 != null) {
                // both genomes has the gene w/ this innovation number
                matchingGenes++;
            }
        }

        return matchingGenes;
    }

    private static int countDisjointGenes(Genome genome1, Genome genome2) {
        int disjointGenes = 0;

        List<Integer> nodeKeys1 = asSortedList(genome1.getNodes().keySet(), tmpList1);
        List<Integer> nodeKeys2 = asSortedList(genome2.getNodes().keySet(), tmpList2);

        int highestInnovation1 = nodeKeys1.get(nodeKeys1.size()-1);
        int highestInnovation2 = nodeKeys2.get(nodeKeys2.size()-1);
        int indices = Math.max(highestInnovation1, highestInnovation2);

        for (int i = 0; i <= indices; i++) {
            NodeGene node1 = genome1.getNodes().get(i);
            NodeGene node2 = genome2.getNodes().get(i);
            if (node1 == null && highestInnovation1 > i && node2 != null) {
                // genome 1 lacks gene, genome 2 has gene, genome 1 has more genes w/ higher innovation numbers
                disjointGenes++;
            } else if (node2 == null && highestInnovation2 > i && node1 != null) {
                disjointGenes++;
            }
        }

        List<Integer> conKeys1 = asSortedList(genome1.getConnections().keySet(), tmpList1);
        List<Integer> conKeys2 = asSortedList(genome2.getConnections().keySet(), tmpList2);

        highestInnovation1 = conKeys1.get(conKeys1.size()-1);
        highestInnovation2 = conKeys2.get(conKeys2.size()-1);

        indices = Math.max(highestInnovation1, highestInnovation2);
        for (int i = 0; i <= indices; i++) {
            ConnectionGene connection1 = genome1.getConnections().get(i);
            ConnectionGene connection2 = genome2.getConnections().get(i);
            if (connection1 == null && highestInnovation1 > i && connection2 != null) {
                disjointGenes++;
            } else if (connection2 == null && highestInnovation2 > i && connection1 != null) {
                disjointGenes++;
            }
        }

        return disjointGenes;
    }

    private static int countExcessGenes(Genome genome1, Genome genome2) {
        int excessGenes = 0;

        List<Integer> nodeKeys1 = asSortedList(genome1.getNodes().keySet(), tmpList1);
        List<Integer> nodeKeys2 = asSortedList(genome2.getNodes().keySet(), tmpList2);

        int highestInnovation1 = nodeKeys1.get(nodeKeys1.size()-1);
        int highestInnovation2 = nodeKeys2.get(nodeKeys2.size()-1);
        int indices = Math.max(highestInnovation1, highestInnovation2);

        for (int i = 0; i <= indices; i++) {
            NodeGene node1 = genome1.getNodes().get(i);
            NodeGene node2 = genome2.getNodes().get(i);
            if (node1 == null && highestInnovation1 < i && node2 != null) {
                excessGenes++;
            } else if (node2 == null && highestInnovation2 < i && node1 != null) {
                excessGenes++;
            }
        }

        List<Integer> conKeys1 = asSortedList(genome1.getConnections().keySet(), tmpList1);
        List<Integer> conKeys2 = asSortedList(genome2.getConnections().keySet(), tmpList2);

        highestInnovation1 = conKeys1.get(conKeys1.size()-1);
        highestInnovation2 = conKeys2.get(conKeys2.size()-1);

        indices = Math.max(highestInnovation1, highestInnovation2);
        for (int i = 0; i <= indices; i++) {
            ConnectionGene connection1 = genome1.getConnections().get(i);
            ConnectionGene connection2 = genome2.getConnections().get(i);
            if (connection1 == null && highestInnovation1 < i && connection2 != null) {
                excessGenes++;
            } else if (connection2 == null && highestInnovation2 < i && connection1 != null) {
                excessGenes++;
            }
        }

        return excessGenes;
    }

    public static double averageWeightDiff(Genome genome1, Genome genome2) {
        int matchingGenes = 0;
        double weightDifference = 0;

        List<Integer> conKeys1 = asSortedList(genome1.getConnections().keySet(), tmpList1);
        List<Integer> conKeys2 = asSortedList(genome2.getConnections().keySet(), tmpList2);

        int highestInnovation1 = conKeys1.get(conKeys1.size()-1);
        int highestInnovation2 = conKeys2.get(conKeys2.size()-1);

        int indices = Math.max(highestInnovation1, highestInnovation2);
        for (int i = 0; i <= indices; i++) { 					// loop through genes -> i is innovation numbers
            ConnectionGene connection1 = genome1.getConnections().get(i);
            ConnectionGene connection2 = genome2.getConnections().get(i);
            if (connection1 != null && connection2 != null) {
                // both genomes has the gene w/ this innovation number
                matchingGenes++;
                weightDifference += Math.abs(connection1.getWeight()-connection2.getWeight());
            }
        }

        return weightDifference/matchingGenes;
    }

    /**
     * @param parent1	More fit parent
     * @param parent2	Less fit parent
     */
    public static Genome crossover(Genome parent1, Genome parent2, Random r) {
        Genome child = new Genome(INPUTS);

        for (NodeGene parent1Node : parent1.getNodes().values()) {
            child.addNodeGene(new NodeGene(parent1Node));
        }

        for (ConnectionGene parent1Node : parent1.getConnections().values()) {
            if (parent2.getConnections().containsKey(parent1Node.getInnovation())) { // matching gene
                ConnectionGene childConGene = r.nextBoolean() ? new ConnectionGene(parent1Node) : new ConnectionGene(parent2.getConnections().get(parent1Node.getInnovation()));
                child.addConnectionGene(childConGene);
            } else { // disjoint or excess gene
                ConnectionGene childConGene = new ConnectionGene(parent1Node);
                child.addConnectionGene(childConGene);
            }
        }
        return child;
    }


    public void mutation(Random r) {
        for (ConnectionGene con : connections.values()) {
            if (r.nextDouble() < ADJUST_WEIGHT_RATE) {            // Changes weight by a number between -0.5 and 0.5
                con.setWeight(con.getWeight() + (r.nextDouble() * 0.5 + r.nextDouble() * -0.5));
            } else {                                                // assigning new weight
                con.setWeight(r.nextDouble() * 2 + r.nextDouble() * -2);
            }
        }
    }

    public void addConnectionMutation(Random r, Counter innovation, int maxAttempts) {
        int tries = 0;
        boolean success = false;
        while (tries < maxAttempts && success == false) {
            tries++;

            Integer[] nodeInnovationNumbers = new Integer[nodes.keySet().size()];
            nodes.keySet().toArray(nodeInnovationNumbers);
            Integer keyNode1 = nodeInnovationNumbers[r.nextInt(nodeInnovationNumbers.length)];
            Integer keyNode2 = nodeInnovationNumbers[r.nextInt(nodeInnovationNumbers.length)];

            NodeGene node1 = nodes.get(keyNode1);
            NodeGene node2 = nodes.get(keyNode2);
            double weight = r.nextDouble();

            boolean reversed = false;
            if (node1.getType() == NodeGene.TYPE.HIDDEN && node2.getType() == NodeGene.TYPE.INPUT) {
                reversed = true;
            } else if (node1.getType() == NodeGene.TYPE.OUTPUT && node2.getType() == NodeGene.TYPE.HIDDEN) {
                reversed = true;
            } else if (node1.getType() == NodeGene.TYPE.OUTPUT && node2.getType() == NodeGene.TYPE.INPUT) {
                reversed = true;
            }

            boolean connectionImpossible = false;
            if (node1.getType() == NodeGene.TYPE.INPUT && node2.getType() == NodeGene.TYPE.INPUT) {
                connectionImpossible = true;
            } else if (node1.getType() == NodeGene.TYPE.OUTPUT && node2.getType() == NodeGene.TYPE.OUTPUT) {
                connectionImpossible = true;
            } else if (node1.getId() == node2.getId()) {
                connectionImpossible = true; // Should fix problem with genes connecting to themselves
            }

            boolean connectionExists = false;
            for (ConnectionGene con : connections.values()) {
                if (con.getInNode() == node1.getId() && con.getOutNode() == node2.getId()) { // existing connection
                    connectionExists = true;
                    break;
                } else if (con.getInNode() == node2.getId() && con.getOutNode() == node1.getId()) { // existing connection
                    connectionExists = true;
                    break;
                }
            }

            if (connectionExists || connectionImpossible) {
                continue;
            }

            ConnectionGene newCon = new ConnectionGene(reversed ? node2.getId() : node1.getId(), reversed ? node1.getId() : node2.getId(), weight, true, innovation.getInnovation());
            connections.put(newCon.getInnovation(), newCon);
            success = true;
        }

        if (success == false) {
            //System.out.println("Tried, but could not add more connections");
        }
    }

    public void addNodeMutation(Random r, Counter connectionInnovation, Counter nodeInnovation) {
        ConnectionGene con = (ConnectionGene) connections.values().toArray()[r.nextInt(connections.size())];

        NodeGene inNode = nodes.get(con.getInNode());
        NodeGene outNode = nodes.get(con.getOutNode());

        con.disable();

        NodeGene newNode = new NodeGene(NodeGene.TYPE.HIDDEN, nodeInnovation.getInnovation());
        ConnectionGene inToNew = new ConnectionGene(inNode.getId(), newNode.getId(), 1, true, connectionInnovation.getInnovation());
        ConnectionGene newToOut = new ConnectionGene(newNode.getId(), outNode.getId(), con.getWeight(), true, connectionInnovation.getInnovation());

        nodes.put(newNode.getId(), newNode);
        connections.put(inToNew.getInnovation(), inToNew);
        connections.put(newToOut.getInnovation(), newToOut);
    }

    public int[] countLayers() {
        int[] layerSizes = new int[3];

        for (NodeGene node : nodes.values()) {
            if (node.getType() == NodeGene.TYPE.INPUT) layerSizes[0]++;
            else if (node.getType() == NodeGene.TYPE.HIDDEN) layerSizes[1]++;
            else layerSizes[2]++;
        }

        return layerSizes;
    }

     // Sorts in ascending order
    private static List<Integer> asSortedList(Collection<Integer> c, List<Integer> list) {
        list.clear();
        list.addAll(c);
        java.util.Collections.sort(list);
        return list;
    }

    public Map<Integer, ConnectionGene> getConnections() {
        return connections;
    }

    public Map<Integer, NodeGene> getNodes() {
        return nodes;
    }

    public void addNodeGene(NodeGene gene) {
        nodes.put(gene.getId(), gene);
    }

    public void addConnectionGene(ConnectionGene gene) { connections.put(gene.getInnovation(), gene); }

    // Modified sigmoid function according ot the paper
    private double sigmoid(double x) {
        return (1 / (1 + Math.pow(Math.E, -4.9*x)));
    }

}

