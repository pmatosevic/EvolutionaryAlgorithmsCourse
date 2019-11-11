package hr.fer.zemris.optjava.hw03.solution;

import java.util.Arrays;
import java.util.Random;

public class DoubleArraySolution extends SingleObjectiveSolution {

    private double[] values;

    private DoubleArraySolution() {
    }

    public DoubleArraySolution(int size) {
        this.values = new double[size];
    }

    public double[] getValues() {
        return values;
    }

    public void setValues(double[] values) {
        this.values = values;
    }

    public DoubleArraySolution newLikeThis() {
        DoubleArraySolution clone = new DoubleArraySolution(values.length);
        clone.value = value;
        clone.fitness = fitness;
        return clone;
    }

    public DoubleArraySolution duplicate() {
        DoubleArraySolution clone = new DoubleArraySolution();
        clone.values = Arrays.copyOf(values, values.length);
        clone.value = value;
        clone.fitness = fitness;
        return clone;
    }

    public void randomize(Random random, double[] min, double[] max) {
        for (int i = 0; i < values.length; i++) {
            double randNum = random.nextDouble();
            double factor = max[i] - min[i];
            values[i] = min[i] + randNum * factor;
        }
    }

}
