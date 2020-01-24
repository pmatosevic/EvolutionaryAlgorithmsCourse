package hr.fer.zemris.optjava.dz11.selection;

import hr.fer.zemris.generic.ga.GASolution;
import hr.fer.zemris.optjava.dz11.Chromosome;

import java.util.List;

public interface Selection {

    Chromosome select(List<Chromosome> population);

}
