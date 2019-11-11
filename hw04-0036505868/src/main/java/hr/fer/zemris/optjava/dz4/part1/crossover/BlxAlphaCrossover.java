package hr.fer.zemris.optjava.dz4.part1.crossover;

import hr.fer.zemris.optjava.dz4.part1.Chromosome;

import java.util.Random;

public class BlxAlphaCrossover implements Crossover {

    private Random rand = new Random();
    private final double alpha;

    public BlxAlphaCrossover(double alpha) {
        this.alpha = alpha;
    }

    @Override
    public Chromosome crossover(Chromosome first, Chromosome second) {
        double[] values = new double[first.getValues().length];
        for (int i = 0; i < values.length; i++) {
            double min = Math.min(first.getValues()[i], second.getValues()[i]);
            double max = Math.max(first.getValues()[i], second.getValues()[i]);
            double diff = max - min;
            double minSel = min - diff * alpha;
            double maxSel = max + diff * alpha;
            values[i] = rand.nextDouble() * (maxSel - minSel) - minSel;
        }
        return new Chromosome(values);
    }
}
