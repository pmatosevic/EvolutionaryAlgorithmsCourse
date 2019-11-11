package hr.fer.zemris.trisat.algorithm;

import hr.fer.zemris.trisat.BitVector;
import hr.fer.zemris.trisat.SATFormula;
import hr.fer.zemris.trisat.SolvingAlgorithm;

import java.util.Random;

public class MultistartLocalSearchAlgorithm implements SolvingAlgorithm {

    private static final int MAX_TRIES = 100;
    private static final int MAX_FLIPS = 10000;

    private Random rand = new Random();

    @Override
    public BitVector solve(SATFormula satFormula) {
        for (int iter = 0; iter < MAX_TRIES; iter++) {
            BitVector assignment = BasicAlgorithms.solveIteration(satFormula, MAX_FLIPS, rand);
            if (assignment != null) return assignment;
        }
        return null;
    }

}
