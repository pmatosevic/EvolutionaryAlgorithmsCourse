package hr.fer.zemris.optjava.dz5.part2;

import hr.fer.zemris.optjava.dz5.Crossover;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class OrderedCrossover implements Crossover<Chromosome> {

    private Random rand = new Random();

    @Override
    public Chromosome crossover(Chromosome first, Chromosome second) {
        int n = first.getPermutation().length;
        int pos1 = rand.nextInt(n-1);
        int pos2 = rand.nextInt(n);
        int start = Math.min(pos1, pos2);
        int end = Math.max(pos1, pos2);

        Set<Integer> usedNum = new HashSet<>();
        for (int i = start; i < end; i++) usedNum.add(first.getPermutation()[i]);

        int secPos = 0;
        int[] child = new int[n];
        for (int i = 0; i < n; i++) {
            if (i >= start && i < end) {
                child[i] = first.getPermutation()[i];
            } else {
                while (usedNum.contains(second.getPermutation()[secPos])) secPos++;
                child[i] = second.getPermutation()[secPos];
                secPos++;
            }
        }

        return new Chromosome(child, 0);
    }
}
