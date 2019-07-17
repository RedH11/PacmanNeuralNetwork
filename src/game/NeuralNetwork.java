package game;

import java.util.Random;

public class NeuralNetwork {

    // First is layer and second is neuron
    private double[][] output;
    // Three (1. Layer 2. Neuron 3. Neuron in prev. layer connected with this one)
     double[][][] weights;
    // Every neuron has one bias
     double[][] bias;
     
    public final int[] NETWORK_LAYER_SIZES;
    public final int INPUT_SIZE;
    public final int OUTPUT_SIZE;
    public final int NETWORK_SIZE;

    public NeuralNetwork(int... NETWORK_LAYER_SIZES) {
        // Size of the different layers of the neural network
        this.NETWORK_LAYER_SIZES = NETWORK_LAYER_SIZES;
        // Amount of input nodes
        this.INPUT_SIZE = NETWORK_LAYER_SIZES[0];
        this.NETWORK_SIZE = NETWORK_LAYER_SIZES.length;
        // Amount of output nodes
        this.OUTPUT_SIZE = NETWORK_LAYER_SIZES[NETWORK_SIZE-1];

        // Output nodes
        this.output = new double[NETWORK_SIZE][];
        // Weight values
        this.weights = new double[NETWORK_SIZE][][];
        // Bias values
        this.bias = new double[NETWORK_SIZE][];
        
        for (int i = 0; i < NETWORK_SIZE; i++) {
            this.output[i] = new double[NETWORK_LAYER_SIZES[i]];
            this.bias[i] = new double[NETWORK_LAYER_SIZES[i]];

            // Generate random biases and fill array
            this.bias[i] = NetworkTools.createRandomArray(NETWORK_LAYER_SIZES[i], 0.3, 0.7);


            if (i > 0) {
                weights[i] = new double[NETWORK_LAYER_SIZES[i]][NETWORK_LAYER_SIZES[i - 1]];
                // Generate random weights and fill array
                weights[i] = NetworkTools.createRandomArray(NETWORK_LAYER_SIZES[i], NETWORK_LAYER_SIZES[i - 1], -0.3, 0.5);
            }
        }
    }

    public double[] calculate(double... input) {
        // If there is too much/ too little data passed in
        if (input.length != this.INPUT_SIZE) return null;

        this.output[0] = input;

        // Iterate through each layer
        for (int layer = 1; layer < NETWORK_SIZE; layer++) {
            // And each neuron in that layer
            for(int neuron = 0; neuron < NETWORK_LAYER_SIZES[layer]; neuron++) {
                // Adding bias to the sum initially
                double sum = bias[layer][neuron];
                // Adding to the sum the output of the last layer
                for (int prevNeuron = 0; prevNeuron < NETWORK_LAYER_SIZES[layer - 1]; prevNeuron++) {
                    sum += output[layer - 1][prevNeuron] * weights[layer][neuron][prevNeuron];
                }

                // The output of this neuron is equal to the sigmoid of the sum
                output[layer][neuron] = sigmoid(sum);
            }
        }
        // Last layer outputs
        return output[NETWORK_SIZE - 1];
    }

    public void mutate(double mutationChance) {

        Random rand = new Random();

        for (int layer = 1; layer < NETWORK_SIZE; layer++) {
            // And each neuron in that layer
            for (int neuron = 0; neuron < NETWORK_LAYER_SIZES[layer]; neuron++) {
                // Adding bias to the sum initially

                if(rand.nextDouble()*100 < mutationChance){
                    bias[layer][neuron] += rand.nextDouble();
                    System.out.println("Bias Weights");}
                // Adding to the sum the output of the last layer
                for (int prevNeuron = 0; prevNeuron < NETWORK_LAYER_SIZES[layer - 1]; prevNeuron++) {
                    if(rand.nextDouble()*100 < mutationChance) {
                        weights[layer][neuron][prevNeuron] += rand.nextDouble();
                        System.out.println("Mutated Weights");}
                }

                // The output of this neuron is equal to the sigmoid of the sum

            }
        }
    }


    private double sigmoid(double x) {
        return (1 / (1 + Math.pow(Math.E, -x)));
    }
    
    
}
