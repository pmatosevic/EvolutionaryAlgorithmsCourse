package hr.fer.zemris.optjava.dz11.crossover;

import hr.fer.zemris.optjava.dz11.Chromosome;

public interface Crossover {

    Chromosome crossover(Chromosome parent1, Chromosome parent2);

}
