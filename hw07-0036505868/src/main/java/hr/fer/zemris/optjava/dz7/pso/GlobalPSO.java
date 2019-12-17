package hr.fer.zemris.optjava.dz7.pso;

import hr.fer.zemris.optjava.dz7.Algorithm;
import hr.fer.zemris.optjava.dz7.ann.FFANN;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;

public class GlobalPSO implements Algorithm {

    private Random rand = new Random();
    int populationSize;
    FFANN ffann;
    int dimension;
    double c1;
    double c2;

    double[][] x;
    double[][] v;
    double[] f;
    double[][] pbest;
    double[] pbestF;
    double[] gbest;
    double gbestF;

    private static final double vMin = -1;
    private static final double vMax = 1;
    private static final double xMin = -1;
    private static final double xMax = 1;
    private static final double weightMax = 1;
    private static final double weightMin = 0.5;

    public GlobalPSO(int populationSize, FFANN ffann, double c1, double c2) {
        this.populationSize = populationSize;
        this.ffann = ffann;
        this.c1 = c1;
        this.c2 = c2;
    }

    public double[] run(int maxIter, double minErr) {
        initialize();
        for (int iter = 0; iter < maxIter; iter++) {
            for (int i = 0; i < populationSize; i++) {
                f[i] = evaluate(x[i]);
                if (f[i] < pbestF[i]) {
                    pbestF[i] = f[i];
                    pbest[i] = Arrays.copyOf(x[i], dimension);
                }
                if (f[i] < gbestF) {
                    gbestF = f[i];
                    gbest = Arrays.copyOf(x[i], dimension);
                }
            }

            if (gbestF < minErr) break;

            for (int i = 0; i < populationSize; i++) {
                for (int d = 0; d < dimension; d++) {
                    double[] lbest = findNeighborhoodBest(i);
                    v[i][d] = velocityWeight(i, maxIter) * v[i][d] +
                            c1 * rand.nextDouble() * (pbest[i][d]-x[i][d]) +
                            c2 * rand.nextDouble() * (lbest[d]-x[i][d]);
                    v[i][d] = fromRange(v[i][d], vMin, vMax);
                    x[i][d] = x[i][d] + v[i][d];
                }
            }
        }

        return gbest;
    }

    protected double[] findNeighborhoodBest(int pos) {
        return gbest;
    }

    private double velocityWeight(int t, int maxIter) {
        return weightMax - (weightMax - weightMin) * 2.0 * t / maxIter;
    }

    private double fromRange(double v, double min, double max) {
        if (v < min) return min;
        if (v > max) return max;
        return v;
    }

    private double evaluate(double[] array) {
        return ffann.calculateError(array);
    }

    private void initialize() {
        dimension = ffann.getWeightsCount();
        x = new double[populationSize][dimension];
        v = new double[populationSize][dimension];
        pbest = new double[populationSize][dimension];
        f = new double[populationSize];
        pbestF = new double[populationSize];
        Arrays.fill(pbestF, Double.POSITIVE_INFINITY);
        gbestF = Double.POSITIVE_INFINITY;

        for (int i = 0; i < populationSize; i++) {
            x[i] = initArray(dimension, xMin, xMax);
            v[i] = initArray(dimension, vMin, vMax);
        }
    }

    private double[] initArray(int dimension, double min, double max) {
        return IntStream.range(0, dimension)
                .mapToDouble(idx -> rand.nextDouble() * (max - min) - min)
                .toArray();
    }

}
