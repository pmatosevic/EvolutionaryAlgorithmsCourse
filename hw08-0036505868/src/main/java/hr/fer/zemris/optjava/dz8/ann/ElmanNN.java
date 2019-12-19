package hr.fer.zemris.optjava.dz8.ann;

import hr.fer.zemris.optjava.dz8.Dataset;

import java.util.Arrays;

public class ElmanNN extends ANN {

    private double[] hiddenState;

    public ElmanNN(int[] layers, TransferFunction[] transferFunctions, Dataset dataset) {
        super(layers, transferFunctions, dataset);
    }

    @Override
    protected int calculateWeightCount(int[] layers) {
        int count = 0;
        count += (layers[0] + layers[1] + 1) * layers[1];
        for (int i = 1; i < layers.length - 1; i++) {
            count += (layers[i] + 1) * layers[i+1];
        }
        count += layers[1];
        return count;
    }

    public void resetHiddenState() {
        hiddenState = null;
    }

    @Override
    public double calculateError(double[] weights) {
        resetHiddenState();
        return super.calculateError(weights);
    }

    @Override
    public void calcOutputs(double[] inputs, double[] weights, double[] outputs) {
        if (weights.length != weightsCount) {
            throw new IllegalArgumentException("Wrong number of weights.");
        }
        if (hiddenState == null) initializeHiddenState(weights);

        double[] lastLayer = Arrays.copyOf(inputs, inputs.length + layers[1]);
        for (int i = 0; i < layers[1]; i++) lastLayer[i + inputs.length] = hiddenState[i];
        int offset = 0;
        for (int i = 1; i < layers.length; i++) {
            lastLayer = calculateLayerOutput(lastLayer, layers[i], weights, offset, transferFunctions[i-1]);
            offset += (layers[i-1] + 1) * layers[i];
            if (i == 1) hiddenState = Arrays.copyOf(lastLayer, lastLayer.length);
        }

        for (int i = 0; i < outputs.length; i++) {
            outputs[i] = lastLayer[i];
        }
    }

    private void initializeHiddenState(double[] weights) {
        hiddenState = new double[layers[1]];
        for (int i = 0; i < layers[1]; i++) {
            hiddenState[i] = weights[weights.length - layers[1] + i];
        }
    }
}
