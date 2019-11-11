package hr.fer.zemris.optjava.dz4.part1.selection;

import hr.fer.zemris.optjava.dz4.part1.Chromosome;

import java.util.*;

public class TournamentSelection implements Selection {

    private int k;
    private Random rand = new Random();

    public TournamentSelection(int k) {
        this.k = k;
    }

    @Override
    public Chromosome select(List<Chromosome> chromosomes) {
        List<Chromosome> tournament = new ArrayList<>();
        for (int i = 0; i < k; i++) tournament.add(chromosomes.get(rand.nextInt(chromosomes.size())));

        return tournament.stream().max(Comparator.comparing(Chromosome::getFitness)).get();
    }

}
