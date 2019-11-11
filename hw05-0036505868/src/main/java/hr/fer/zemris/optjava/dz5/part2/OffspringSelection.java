package hr.fer.zemris.optjava.dz5.part2;

import hr.fer.zemris.optjava.dz5.Crossover;
import hr.fer.zemris.optjava.dz5.Mutation;
import hr.fer.zemris.optjava.dz5.Selection;

import java.util.*;
import java.util.stream.Collectors;

public class OffspringSelection {

    private double compFactor;
    private final double maxSelPress;
    private final double succRatio;
    private final double maxIterations;
    private final Mutation<Chromosome> mutation;
    private final Crossover<Chromosome> crossover;
    private final Selection<Chromosome> selection;
    private final FactoriesData data;

    public OffspringSelection(double compFactor, double maxSelPress, double succRatio, double maxIterations,
                              Mutation<Chromosome> mutation, Crossover<Chromosome> crossover, Selection<Chromosome> selection,
                              FactoriesData data) {
        this.compFactor = compFactor;
        this.maxSelPress = maxSelPress;
        this.succRatio = succRatio;
        this.maxIterations = maxIterations;
        this.mutation = mutation;
        this.crossover = crossover;
        this.selection = selection;
        this.data = data;
    }

    public List<Chromosome> run(List<Chromosome> population) {
        int n = population.size();
        double actSelPress = 1;
        int iter = 0;
        while (iter < maxIterations && actSelPress < maxSelPress) {
            Set<Chromosome> newPopulation = new HashSet<>();
            List<Chromosome> pool = new ArrayList<>();

            while (newPopulation.size() < n * succRatio
                    && newPopulation.size() + pool.size() < n * maxSelPress) {
                Chromosome parent1 = selection.select(population);
                Chromosome parent2 = selection.select(population);
                Chromosome child = crossover.crossover(parent1, parent2);
                mutation.mutate(child);
                child.evaluate(data);

                double fitLimit = calcFitLimit(parent1, parent2, compFactor);
                if (child.getFitness() < fitLimit) {
                    pool.add(child);
                } else {
                    newPopulation.add(child);
                }
            }

            actSelPress = (newPopulation.size() + pool.size()) / (double)n;
            population.clear();
            population.addAll(newPopulation);
            population.addAll(pool.stream().limit(n - newPopulation.size()).collect(Collectors.toList()));
            Collections.sort(population);

            iter++;
            compFactor = Math.min(1, compFactor * 1.1);
        }

        return population;
    }

    private static double calcFitLimit(Chromosome parent1, Chromosome parent2, double compFactor) {
        double fitMin = Math.min(parent1.getFitness(), parent2.getFitness());
        double fitMax = Math.max(parent1.getFitness(), parent2.getFitness());
        return fitMin + compFactor * (fitMax - fitMin);
    }

}
