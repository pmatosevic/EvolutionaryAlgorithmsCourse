package hr.fer.zemris.optjava.dz5.part1;

import hr.fer.zemris.optjava.dz5.Selection;
import hr.fer.zemris.optjava.dz5.part1.Chromosome;

import java.util.List;
import java.util.Random;

public class TournamentSelection implements Selection<Chromosome> {

    private final int k;
    private Random rand = new Random();

    public TournamentSelection(int k) {
        this.k = k;
    }

    @Override
    public Chromosome select(List<Chromosome> population) {
        return population.get(rand.nextInt(Math.min(population.size(), k)));
    }
}
