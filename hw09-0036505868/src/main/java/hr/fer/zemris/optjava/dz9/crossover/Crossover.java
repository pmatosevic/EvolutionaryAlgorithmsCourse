package hr.fer.zemris.optjava.dz9.crossover;

import hr.fer.zemris.optjava.dz9.Chromosome;

public interface Crossover {

    Chromosome crossover(Chromosome a, Chromosome b);

}
