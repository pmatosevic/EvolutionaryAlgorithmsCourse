package hr.fer.zemris.optjava.dz11.crossover;

import hr.fer.zemris.optjava.dz11.Chromosome;
import hr.fer.zemris.optjava.rng.IRNG;
import hr.fer.zemris.optjava.rng.RNG;

public class TwoFoldCrossover implements Crossover {
    @Override
    public Chromosome crossover(Chromosome parent1, Chromosome parent2) {
        int n = (parent1.getData().length - 1) / 5;
        int[] data = new int[5*n+1];
        Chromosome child = new Chromosome(data);

        IRNG rand = RNG.getRNG();
        data[0] = rand.nextBoolean() ? parent1.getData()[0] : parent2.getData()[2];

        int location = rand.nextInt(1, n);
        for (int i = 0; i < n; i++) {
            Chromosome par = i < location ? parent1 : parent2;
            for (int j = 1; j <= 5; j++) {
                data[5*i + j] = par.getData()[5*i + j];
            }
        }

        return child;
    }
}
