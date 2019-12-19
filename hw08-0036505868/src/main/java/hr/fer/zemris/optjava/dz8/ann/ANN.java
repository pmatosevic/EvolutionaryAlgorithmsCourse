package hr.fer.zemris.optjava.dz8.ann;

import hr.fer.zemris.optjava.dz8.Dataset;
import hr.fer.zemris.optjava.dz8.Example;

import java.util.stream.IntStream;

public class ANN {

    protected int[] layers;
    protected TransferFunction[] transferFunctions;
    protected Dataset dataset;
    protected int weightsCount = 0;

    public ANN(int[] layers, TransferFunction[] transferFunctions, Dataset dataset) {
        this.layers = layers;
        this.transferFunctions = transferFunctions;
        this.dataset = dataset;

        weightsCount = calculateWeightCount(layers);
    }

    protected int calculateWeightCount(int[] layers) {
        int count = 0;
        for (int i = 0; i < layers.length - 1; i++) {
            count += (layers[i] + 1) * layers[i+1];
        }
        return count;
    }

    public int getWeightsCount() {
        return weightsCount;
    }

    public double calculateError(double[] weights) {
        double error = 0;
        for (Example example : dataset.getExamples()) {
            double[] expected = example.output;
            double[] actual = new double[expected.length];
            calcOutputs(example.input, weights, actual);

            double currError = IntStream.range(0, expected.length)
                    .mapToDouble(idx -> expected[idx] - actual[idx])
                    .map(x -> x*x)
                    .sum();
            error += currError;
        }
        error /= dataset.examplesCount();
        return error;
    }

    public void calcOutputs(double[] inputs, double[] weights, double[] outputs){
        if (weights.length != weightsCount) {
            throw new IllegalArgumentException("Wrong number of weights.");
        }

        double[] lastLayer = inputs;
        int offset = 0;
        for (int i = 1; i < layers.length; i++) {
            lastLayer = calculateLayerOutput(lastLayer, layers[i], weights, offset, transferFunctions[i-1]);
            offset += (layers[i-1] + 1) * layers[i];
        }

        for (int i = 0; i < outputs.length; i++) {
            outputs[i] = lastLayer[i];
        }
    }

    public double[] calcClassification(double[] inputs, double[] weights) {
        double[] result = new double[layers[layers.length - 1]];
        calcOutputs(inputs, weights, result);
        for (int i = 0; i < result.length; i++) {
            if (result[i] <= 0.5) result[i] = 0;
            else result[i] = 1;
        }
        return result;
    }

    protected double[] calculateLayerOutput(double[] inputs, int outputCount, double[] weights, int offset, TransferFunction function) {
        double[] output = new double[outputCount];
        for (int i = 0; i < outputCount; i++) {
            output[i] = calculateSingleOutput(inputs, weights, offset + i * (inputs.length + 1), function);
        }
        return output;
    }

    protected double calculateSingleOutput(double[] inputs, double[] weights, int offset, TransferFunction function) {
        return function.calculate(weights[offset] + IntStream.range(0, inputs.length)
                .mapToDouble(idx -> inputs[idx] * weights[offset + idx + 1])
                .sum()
        );
    }

}
