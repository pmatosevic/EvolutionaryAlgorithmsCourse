package hr.fer.zemris.optjava.dz4.part1.crossover;

import hr.fer.zemris.optjava.dz4.part1.Chromosome;

public interface Crossover {

    Chromosome crossover(Chromosome first, Chromosome second);

}
