package hr.fer.zemris.trisat.algorithm;

import hr.fer.zemris.trisat.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomWalkSATAlgorithm implements SolvingAlgorithm {

    private static final int MAX_TRIES = 100;
    private static final int MAX_FLIPS = 10000;
    private static final double RANDOM_FLIP_PROBABILITY = 0.2;

    private Random rand = new Random();

    @Override
    public BitVector solve(SATFormula satFormula) {
        for (int iter = 0; iter < MAX_TRIES; iter++) {
            BitVector assignment = trySolve(satFormula, MAX_FLIPS);
            if (assignment != null) return assignment;
        }
        return null;
    }

    private BitVector trySolve(SATFormula satFormula, int maxFlips) {
        MutableBitVector assignment = new BitVector(rand, satFormula.getNumberOfVariables()).copy();
        for (int iter = 0; iter < maxFlips; iter++) {
            if (satFormula.isSatisfied(assignment)) return assignment;

            List<Integer> unsatisfiedClauseIndexes = new ArrayList<>();
            for (int i = 0; i < satFormula.getNumberOfClauses(); i++) {
                Clause clause = satFormula.getClause(i);
                if (!clause.isSatisfied(assignment)) unsatisfiedClauseIndexes.add(i);
            }

            Clause clause = satFormula.getClause(unsatisfiedClauseIndexes.get(rand.nextInt(unsatisfiedClauseIndexes.size())));
            if (rand.nextDouble() < RANDOM_FLIP_PROBABILITY) {
                int literalIndex = clause.getLiteral(rand.nextInt(clause.getSize())) - 1;
                assignment.set(literalIndex, !assignment.get(literalIndex));
            } else {
                flipBitForBestResult(assignment, clause, satFormula);
            }
        }

        return null;
    }

    private void flipBitForBestResult(MutableBitVector assignment, Clause clause, SATFormula satFormula) {
        int bestSatisfied = -1;
        int bestLiteral = 0;

        for (int i = 0; i < clause.getSize(); i++) {
            int literal = clause.getLiteral(i) - 1;
            assignment.set(literal, !assignment.get(literal));

            int satisfied = satFormula.countSatisfiedClauses(assignment);
            if (satisfied > bestSatisfied) {
                bestLiteral = literal;
                bestSatisfied = satisfied;
            }

            assignment.set(literal, !assignment.get(literal));
        }

        assignment.set(bestLiteral, !assignment.get(bestLiteral));
    }


}
