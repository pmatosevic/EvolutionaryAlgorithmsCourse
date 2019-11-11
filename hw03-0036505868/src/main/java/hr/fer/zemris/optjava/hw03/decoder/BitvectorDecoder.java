package hr.fer.zemris.optjava.hw03.decoder;

import hr.fer.zemris.optjava.hw03.solution.BitvectorSolution;

import java.util.Arrays;

public abstract class BitvectorDecoder implements IDecoder<BitvectorSolution> {

    protected double[] mins;
    protected double[] maxs;
    protected int[] bits;
    protected int n;
    protected int totalBits;

    public BitvectorDecoder(double[] mins, double[] maxs, int[] bits, int n) {
        this.mins = mins;
        this.maxs = maxs;
        this.bits = bits;
        this.n = n;
        this.totalBits = Arrays.stream(bits).sum();
    }

    public BitvectorDecoder(double min, double max, int bit, int n) {
        this.mins = new double[n];
        this.maxs = new double[n];
        this.bits = new int[n];
        this.n = n;
        this.totalBits = n * bit;

        for (int i = 0; i < n; i++) {
            mins[i] = min;
            maxs[i] = max;
            bits[i] = bit;
        }
    }


    public int getTotalBits() {
        return totalBits;
    }

    public int getDimension() {
        return n;
    }

    @Override
    public double[] decode(BitvectorSolution solution) {
        double[] values = new double[n];
        decode(solution, values);
        return values;
    }

    @Override
    public abstract void decode(BitvectorSolution solution, double[] values);

}
