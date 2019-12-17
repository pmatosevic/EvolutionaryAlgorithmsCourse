package hr.fer.zemris.optjava.dz7.ann;

import hr.fer.zemris.optjava.dz7.Dataset;

import java.util.stream.IntStream;

public class FFANN {

    private int[] layers;
    private TransferFunction[] transferFunctions;
    private Dataset dataset;
    private int weightsCount = 0;

    public FFANN(int[] layers, TransferFunction[] transferFunctions, Dataset dataset) {
        this.layers = layers;
        this.transferFunctions = transferFunctions;
        this.dataset = dataset;

        for (int i = 0; i < layers.length - 1; i++) {
            weightsCount += (layers[i] + 1) * layers[i+1];
        }
    }

    public int getWeightsCount() {
        return weightsCount;
    }

    public double calculateError(double[] weights) {
        double error = 0;
        for (Dataset.SingleExample example : dataset.getExamples()) {
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

    private double[] calculateLayerOutput(double[] inputs, int outputCount, double[] weights, int offset, TransferFunction function) {
        double[] output = new double[outputCount];
        for (int i = 0; i < outputCount; i++) {
            output[i] = calculateSingleOutput(inputs, weights, offset + i * (inputs.length + 1), function);
        }
        return output;
    }

    private double calculateSingleOutput(double[] inputs, double[] weights, int offset, TransferFunction function) {
        return function.calculate(weights[offset] + IntStream.range(0, inputs.length)
                .mapToDouble(idx -> inputs[idx] * weights[offset + idx + 1])
                .sum()
        );
    }

}
