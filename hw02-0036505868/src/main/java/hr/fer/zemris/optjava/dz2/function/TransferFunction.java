package hr.fer.zemris.optjava.dz2.function;

import Jama.Matrix;
import hr.fer.zemris.optjava.dz2.IHFunction;

import java.util.Arrays;

import static java.lang.Math.*;

public class TransferFunction implements IHFunction {

    public static final int NUM_X = 5;
    public static final int NUM_PARAMS = 6;
    private static final double GRAD_DIVIDING_CONSTANT = 1000000;

    private double[][] inputs;
    private double[] outputs;
    private int samples;

    public TransferFunction(double[][] inputs, double[] output) {
        this.inputs = inputs;
        this.outputs = output;
        this.samples = inputs.length;
    }

    public double[][] getInputs() {
        return inputs;
    }

    public double[] getOutputs() {
        return outputs;
    }

    public int getSamples() {
        return samples;
    }

    @Override
    public int getNumberOfVariables() {
        return NUM_PARAMS;
    }

    @Override
    public double calculateValue(Matrix params) {
        double[] diff = calculateDifference(params);
        return Arrays.stream(diff).map(d -> d*d).sum();
    }

    @Override
    public Matrix calculateGradient(Matrix params) {
        double[] diff = calculateDifference(params);
        double[] grad = new double[NUM_PARAMS];

        double a = params.get(0, 0);
        double b = params.get(1, 0);
        double c = params.get(2, 0);
        double d = params.get(3, 0);
        double e = params.get(4, 0);
        double f = params.get(5, 0);

        for (int i = 0; i < samples; i++) {
            double df = diff[i];

            double x1 = inputs[i][0];
            double x2 = inputs[i][1];
            double x3 = inputs[i][2];
            double x4 = inputs[i][3];
            double x5 = inputs[i][4];

            grad[0] += 2 * df * x1;
            grad[1] += 2 * df * x1*x1*x1* x2;
            grad[2] += 2 * df * exp(d*x3) * (1 + cos(e*x4));
            grad[3] += 2 * df * c * (1 + cos(e*x4)) * exp(d*x3) * x3;
            grad[4] += -2 * df * c * exp(d*x3) * sin(e*x4) * x4;
            grad[5] += 2 * df * x4 * x5*x5;
        }

        for (int i = 0; i < NUM_PARAMS; i++) grad[i] /= GRAD_DIVIDING_CONSTANT;

        return new Matrix(grad, NUM_PARAMS);
    }

    @Override
    public Matrix calculateHessMatrix(Matrix params) {
        double[] diff = calculateDifference(params);
        Matrix hessMatrix = new Matrix(NUM_PARAMS, NUM_PARAMS);
        double[][] hess = hessMatrix.getArray();

        double a = params.get(0, 0);
        double b = params.get(1, 0);
        double c = params.get(2, 0);
        double d = params.get(3, 0);
        double e = params.get(4, 0);
        double f = params.get(5, 0);

        for (int i = 0; i < samples; i++) {
            double x1 = inputs[i][0];
            double x2 = inputs[i][1];
            double x3 = inputs[i][2];
            double x4 = inputs[i][3];
            double x5 = inputs[i][4];

            hess[0][0] += 2 * pow(x1, 2);
            hess[1][1] += 2 * pow(x1*x1*x1* x2, 2);
            hess[2][2] += 2 * pow(exp(d*x3) * (1 + cos(e*x4)), 2);
            hess[3][3] += 2 * c * (1 + cos(e*x4)) * exp(d*x3) * x3 * (1 + c * (1 + cos(e*x4)));
            hess[4][4] += -2 * c * exp(d*x3) * x4 * (cos(e*x4) - c*exp(d*x3)*sin(e*x4)*x4);
            hess[5][5] += 2 * pow(x4 * x5*x5, 2);
        }

        return hessMatrix;
    }

    public double[] calculateDifference(Matrix params) {
        double[] diff = new double[samples];
        for (int i = 0; i < samples; i++) {
            diff[i] = calculateYValue(params, inputs[i]) - outputs[i];
        }
        return diff;
    }

    public double calculateYValue(Matrix params, double[] x) {
        double a = params.get(0, 0);
        double b = params.get(1, 0);
        double c = params.get(2, 0);
        double d = params.get(3, 0);
        double e = params.get(4, 0);
        double f = params.get(5, 0);

        double y = 0;
        y += a * x[0];
        y += b * pow(x[0], 3) * x[1];
        y += c * exp(d * x[2]) * (1 + cos(e * x[3]));
        y += f * x[3] * x[4] * x[4];
        return y;
    }

}
