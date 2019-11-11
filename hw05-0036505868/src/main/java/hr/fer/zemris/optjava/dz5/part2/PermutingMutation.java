package hr.fer.zemris.optjava.dz5.part2;

import hr.fer.zemris.optjava.dz5.Mutation;

import java.util.Random;

public class PermutingMutation implements Mutation<Chromosome> {

    private Random rand = new Random();

    @Override
    public void mutate(Chromosome c) {
        int[] array = c.getPermutation();

        int ind1 = rand.nextInt(array.length);
        int ind2 = rand.nextInt(array.length);

        int tmp = array[ind1];
        array[ind1] = array[ind2];
        array[ind2] = tmp;
    }
}
