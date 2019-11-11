package hr.fer.zemris.trisat.algorithm;

import hr.fer.zemris.trisat.BitVector;
import hr.fer.zemris.trisat.MutableBitVector;
import hr.fer.zemris.trisat.SATFormula;
import hr.fer.zemris.trisat.SolvingAlgorithm;

import java.util.Random;

public class IteratedLocalSearchAlgorithm implements SolvingAlgorithm {

    private static final int MAX_TRIES = 100;
    private static final int MAX_FLIPS = 10000;
    private static final double FLIP_PERCENTAGE = 0.2;

    private Random rand = new Random();

    @Override
    public BitVector solve(SATFormula satFormula) {
        BitVector assignment = new BitVector(rand, satFormula.getNumberOfVariables());

        for (int iter = 0; iter < MAX_TRIES; iter++) {
            assignment = BasicAlgorithms.tryToSolve(satFormula, MAX_FLIPS, rand, assignment);
            if (satFormula.isSatisfied(assignment)) return assignment;
            assignment = perturbation(assignment);
        }

        return null;
    }

    private BitVector perturbation(BitVector assignment) {
        MutableBitVector result = assignment.copy();
        for (int i = 0; i < assignment.getSize(); i++) {
            if (rand.nextDouble() < FLIP_PERCENTAGE) {
                result.set(i, !result.get(i));
            }
        }
        return result;
    }

}
