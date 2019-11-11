package hr.fer.zemris.optjava.dz4.part1.selection;

import hr.fer.zemris.optjava.dz4.part1.Chromosome;

import java.util.List;

public interface Selection {

    Chromosome select(List<Chromosome> chromosomes);

}
