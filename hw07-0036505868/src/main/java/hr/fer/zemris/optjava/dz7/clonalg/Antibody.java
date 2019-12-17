package hr.fer.zemris.optjava.dz7.clonalg;

import java.util.Arrays;

public class Antibody implements Comparable<Antibody> {

    private double[] weights;
    private double error;

    public Antibody(double[] weights) {
        this.weights = weights;
    }

    public Antibody(double[] weights, double error) {
        this.weights = weights;
        this.error = error;
    }

    public Antibody clone() {
        return new Antibody(Arrays.copyOf(weights, weights.length), error);
    }

    public double[] getWeights() {
        return weights;
    }

    public void setWeights(double[] weights) {
        this.weights = weights;
    }

    public double getError() {
        return error;
    }

    public void setError(double error) {
        this.error = error;
    }

    @Override
    public int compareTo(Antibody o) {
        return Double.compare(this.error, o.error);
    }
}
