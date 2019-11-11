package hr.fer.zemris.optjava.dz5.part1;

import hr.fer.zemris.optjava.dz5.Mutation;

import java.util.Random;

public class BitMutation implements Mutation<Chromosome> {

    private final double prob;
    private Random rand = new Random();

    public BitMutation(double prob) {
        this.prob = prob;
    }

    @Override
    public void mutate(Chromosome c) {
        for (int i = 0; i < c.getBits().size(); i++) {
            if (rand.nextDouble() < prob) {
                c.getBits().flip(i);
            }
        }
    }

}
