package hr.fer.zemris.trisat.algorithm;

import hr.fer.zemris.trisat.BitVector;
import hr.fer.zemris.trisat.BitVectorNGenerator;
import hr.fer.zemris.trisat.SATFormula;
import hr.fer.zemris.trisat.SolvingAlgorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HillClimbingAlgorithm implements SolvingAlgorithm {

    private static final int MAX_ITERATIONS = 100000;

    protected Random rand = new Random();

    @Override
    public BitVector solve(SATFormula satFormula) {
        return BasicAlgorithms.solveIteration(satFormula, MAX_ITERATIONS, rand);
    }

}
