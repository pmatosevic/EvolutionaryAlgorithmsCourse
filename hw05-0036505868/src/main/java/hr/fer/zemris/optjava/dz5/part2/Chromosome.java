package hr.fer.zemris.optjava.dz5.part2;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Chromosome implements Comparable<Chromosome> {

    private int[] permutation;
    private long fitness;

    public Chromosome(int[] permutation, long fitness) {
        this.permutation = permutation;
        this.fitness = fitness;
    }

    public Chromosome(int n) {
        List<Integer> nums = IntStream.range(0, n).mapToObj(i -> Integer.valueOf(i)).collect(Collectors.toList());
        Collections.shuffle(nums);
        permutation = nums.stream().mapToInt(i -> i).toArray();
    }

    public Chromosome clone() {
        return new Chromosome(Arrays.copyOf(permutation, permutation.length), fitness);
    }

    public int[] getPermutation() {
        return permutation;
    }

    public long getFitness() {
        return fitness;
    }

    public void evaluate(FactoriesData data) {
        fitness = -data.permutationCost(permutation);
    }

    @Override
    public int compareTo(Chromosome o) {
        return Long.compare(o.getFitness(), this.getFitness());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Chromosome that = (Chromosome) o;
        return Arrays.equals(permutation, that.permutation);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(permutation);
    }
}
