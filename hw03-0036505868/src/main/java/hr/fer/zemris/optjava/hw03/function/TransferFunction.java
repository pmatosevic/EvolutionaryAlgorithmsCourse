package hr.fer.zemris.optjava.hw03.function;

import java.util.Arrays;

import static java.lang.Math.*;
import static java.lang.Math.cos;

public class TransferFunction implements IFunction {

    public static final int NUM_X = 5;
    public static final int NUM_PARAMS = 6;

    private double[][] inputs;
    private double[] outputs;
    private int numSamples;

    public TransferFunction(double[][] inputs, double[] output) {
        this.inputs = inputs;
        this.outputs = output;
        this.numSamples = inputs.length;
    }

    public double[][] getInputs() {
        return inputs;
    }

    public double[] getOutputs() {
        return outputs;
    }

    public int getNumSamples() {
        return numSamples;
    }

    @Override
    public double valueAt(double[] point) {
        double[] diff = calculateDifferences(point);
        return Arrays.stream(diff).map(d -> d*d).sum();
    }

    public double[] calculateDifferences(double[] point) {
        double[] diff = new double[numSamples];
        for (int i = 0; i < numSamples; i++) {
            diff[i] = calculateYValue(point, inputs[i]) - outputs[i];
        }
        return diff;
    }

    public double calculateYValue(double[] point, double[] x) {
        double a = point[0];
        double b = point[1];
        double c = point[2];
        double d = point[3];
        double e = point[4];
        double f = point[5];

        double y = 0;
        y += a * x[0];
        y += b * pow(x[0], 3) * x[1];
        y += c * exp(d * x[2]) * (1 + cos(e * x[3]));
        y += f * x[3] * x[4] * x[4];
        return y;
    }

}

