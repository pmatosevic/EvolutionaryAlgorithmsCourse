package hr.fer.zemris.optjava.dz9.mutation;

import hr.fer.zemris.optjava.dz9.Chromosome;

import java.util.Random;

public class BasicMutation implements Mutation {

    private double probability = 0.5;
    private double alpha = 0.05;
    private Random rand = new Random();

    @Override
    public void mutate(Chromosome c) {
        int n = c.solution.length;
        for (int i = 0; i<n; i++) {
            if (rand.nextDouble() < probability) {
                c.solution[i] += rand.nextGaussian() * alpha;
            }
        }
    }
}
