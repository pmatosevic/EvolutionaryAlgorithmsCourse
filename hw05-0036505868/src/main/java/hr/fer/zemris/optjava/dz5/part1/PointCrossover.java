package hr.fer.zemris.optjava.dz5.part1;

import hr.fer.zemris.optjava.dz5.Crossover;
import hr.fer.zemris.optjava.dz5.part1.Chromosome;

public class PointCrossover implements Crossover<Chromosome> {
    @Override
    public Chromosome crossover(Chromosome first, Chromosome second) {
        int n = first.getBits().size();
        int point = (int) (Math.random() * n);

        Chromosome child = new Chromosome(n);
        for (int i = 0; i < n; i++) {
            child.getBits().set(i, (i<point) ? second.getBits().get(i) : first.getBits().get(i));
        }
        return child;
    }
}
