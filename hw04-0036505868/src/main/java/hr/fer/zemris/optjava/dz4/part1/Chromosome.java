package hr.fer.zemris.optjava.dz4.part1;

import java.util.Arrays;
import java.util.Random;

public class Chromosome implements Comparable<Chromosome> {

    private double fitness = 0;
    private double[] values;

    public Chromosome(double[] values) {
        this.values = values;
    }

    public Chromosome(int n, Random rand, double min, double max) {
        this.values = new double[n];
        for (int i = 0; i < n; i++) {
            values[i] = rand.nextDouble() * (max - min) - min;
        }
    }

    public Chromosome clone() {
        Chromosome copy = new Chromosome(Arrays.copyOf(values, values.length));
        copy.fitness = this.fitness;
        return copy;
    }

    public double[] getValues() {
        return values;
    }

    public void setValues(double[] values) {
        this.values = values;
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    @Override
    public int compareTo(Chromosome o) {
        if (this.fitness < o.fitness) return 1;
        if (this.fitness > o.fitness) return -1;
        return 0;
    }

}
