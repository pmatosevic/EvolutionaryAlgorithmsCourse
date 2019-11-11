package hr.fer.zemris.optjava.dz5.part1;

import hr.fer.zemris.optjava.dz5.Selection;
import hr.fer.zemris.optjava.dz5.part1.Chromosome;

import java.util.List;
import java.util.Random;

public class RandomSelection implements Selection<Chromosome> {

    private Random rand = new Random();

    @Override
    public Chromosome select(List<Chromosome> population) {
        return population.get(rand.nextInt(population.size()));
    }
}
