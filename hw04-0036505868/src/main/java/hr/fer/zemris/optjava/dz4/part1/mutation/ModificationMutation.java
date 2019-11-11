package hr.fer.zemris.optjava.dz4.part1.mutation;

import hr.fer.zemris.optjava.dz4.part1.Chromosome;

import java.util.Random;

public class ModificationMutation implements Mutation {

    private double mutationProbability;
    private double sigma;
    private Random rand = new Random();

    public ModificationMutation(double sigma, double mutationProbability) {
        this.sigma = sigma;
        this.mutationProbability = mutationProbability;
    }

    @Override
    public void mutate(Chromosome chromosome) {
        double[] values = chromosome.getValues();
        boolean mutated = false;
        for (int i = 0; i < values.length; i++) {
            if (rand.nextDouble() < mutationProbability) {
                values[i] += rand.nextGaussian() * sigma;
                mutated = true;
            }
        }
        if (!mutated) {
            int index = rand.nextInt(values.length);
            values[index] += rand.nextGaussian() * sigma;
        }
    }

}
