package hr.fer.zemris.optjava.hw03.neighborhood;

import hr.fer.zemris.optjava.hw03.solution.BitvectorSolution;

import java.util.Random;

public class BitvectorNeighborhood implements INeighborhood<BitvectorSolution> {

    private double mutationProbability;
    Random rand = new Random();

    public BitvectorNeighborhood(double mutationProbability) {
        this.mutationProbability = mutationProbability;
    }

    @Override
    public BitvectorSolution randomNeighbor(BitvectorSolution solution) {
        int size = solution.getBits().length;
        BitvectorSolution neighbor = solution.duplicate();
        for (int i = 0; i < size; i++) {
            if (rand.nextDouble() < mutationProbability) {
                neighbor.getBits()[i] = !neighbor.getBits()[i];
            }
        }
        return neighbor;
    }

}
