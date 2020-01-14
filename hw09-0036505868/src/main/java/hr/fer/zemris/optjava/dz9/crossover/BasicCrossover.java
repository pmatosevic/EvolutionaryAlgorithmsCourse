package hr.fer.zemris.optjava.dz9.crossover;

import hr.fer.zemris.optjava.dz9.Chromosome;

import java.util.Random;

public class BasicCrossover implements Crossover {

    private Random rand = new Random();

    @Override
    public Chromosome crossover(Chromosome a, Chromosome b) {
        double[] array = new double[a.solution.length];
        for (int i = 0; i < array.length; i++) {
            array[i] = (rand.nextBoolean()) ? a.solution[i] : b.solution[i];
        }
        return new Chromosome(array);
    }
}
