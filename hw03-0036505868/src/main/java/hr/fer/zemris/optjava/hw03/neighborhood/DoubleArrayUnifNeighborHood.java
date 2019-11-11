package hr.fer.zemris.optjava.hw03.neighborhood;

import hr.fer.zemris.optjava.hw03.solution.DoubleArraySolution;

import java.util.Random;

public class DoubleArrayUnifNeighborHood implements INeighborhood<DoubleArraySolution> {

    private double[] deltas;
    Random rand = new Random();

    public DoubleArrayUnifNeighborHood(double[] deltas) {
        this.deltas = deltas;
    }

    @Override
    public DoubleArraySolution randomNeighbor(DoubleArraySolution solution) {
        int size = solution.getValues().length;
        DoubleArraySolution neighbor = solution.duplicate();
        for (int i = 0; i < size; i++) {
            double val = (rand.nextDouble() * 2 * deltas[i]) - deltas[i];
            neighbor.getValues()[i] += val;
        }
        return neighbor;
    }
}
