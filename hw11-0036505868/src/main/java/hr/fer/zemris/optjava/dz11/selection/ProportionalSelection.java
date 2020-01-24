package hr.fer.zemris.optjava.dz11.selection;

import hr.fer.zemris.generic.ga.GASolution;
import hr.fer.zemris.optjava.dz11.Chromosome;
import hr.fer.zemris.optjava.rng.IRNG;
import hr.fer.zemris.optjava.rng.RNG;

import java.util.List;

public class ProportionalSelection implements Selection {

    @Override
    public Chromosome select(List<Chromosome> population) {
        IRNG rand = RNG.getRNG();
        double fitnessSum = population.stream().mapToDouble(c -> c.fitness).sum();
        double selection = rand.nextDouble() * fitnessSum;
        for (Chromosome solution : population) {
            if (selection < solution.fitness) return solution;
            selection -= solution.fitness;
        }
        return population.get(population.size() - 1);
    }
}
