package hr.fer.zemris.optjava.dz9.crossover;

import hr.fer.zemris.optjava.dz9.Chromosome;

import java.util.Random;

public class BlxAlphaCrossover implements Crossover {

    private double alpha = 1.0;
    private Random rand = new Random();

    @Override
    public Chromosome crossover(Chromosome a, Chromosome b) {
        double[] values = new double[a.solution.length];
        for (int i = 0; i < values.length; i++) {
            double min = Math.min(a.solution[i], b.solution[i]);
            double max = Math.max(a.solution[i], b.solution[i]);
            double diff = max - min;
            double minSel = min - diff * alpha;
            double maxSel = max + diff * alpha;
            values[i] = rand.nextDouble() * (maxSel - minSel) - minSel;
        }
        return new Chromosome(values);
    }
}
