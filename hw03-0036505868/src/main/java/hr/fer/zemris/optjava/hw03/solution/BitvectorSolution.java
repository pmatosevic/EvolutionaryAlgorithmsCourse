package hr.fer.zemris.optjava.hw03.solution;

import java.util.Arrays;
import java.util.Random;

public class BitvectorSolution extends SingleObjectiveSolution {

    private boolean[] bits;

    private BitvectorSolution() {
    }

    public BitvectorSolution(int size) {
        this.bits = new boolean[size];
    }

    public boolean[] getBits() {
        return bits;
    }

    public void setBits(boolean[] bits) {
        this.bits = bits;
    }

    public BitvectorSolution newLikeThis() {
        BitvectorSolution clone = new BitvectorSolution(bits.length);
        clone.fitness = fitness;
        clone.value = value;
        return clone;
    }

    public BitvectorSolution duplicate() {
        BitvectorSolution clone = new BitvectorSolution();
        clone.bits = Arrays.copyOf(bits, bits.length);
        clone.fitness = fitness;
        clone.value = value;
        return clone;
    }

    public void randomize(Random random) {
        for (int i = 0; i < bits.length; i++) {
            bits[i] = random.nextBoolean();
        }
    }

}
