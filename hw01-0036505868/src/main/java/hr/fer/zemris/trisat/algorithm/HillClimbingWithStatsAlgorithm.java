package hr.fer.zemris.trisat.algorithm;

import hr.fer.zemris.trisat.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HillClimbingWithStatsAlgorithm implements SolvingAlgorithm {

    private static final int NUMBER_OF_BEST = 2;
    private static final int MAX_ITERATIONS = 100000;

    private Random rand = new Random();

    @Override
    public BitVector solve(SATFormula satFormula) {
        BitVector assignment = new BitVector(rand, satFormula.getNumberOfVariables());
        SATFormulaStats stats = new SATFormulaStats(satFormula);

        for (int iter = 0; iter < MAX_ITERATIONS; iter++) {
            stats.setAssignment(assignment, true);

            List<BitVectorWithFitness> neighbors = new ArrayList<>();
            for (BitVector neighbor : new BitVectorNGenerator(assignment)) {
                stats.setAssignment(neighbor, false);
                if (stats.isSatisfied()) return neighbor;

                neighbors.add(new BitVectorWithFitness(
                        neighbor,
                        stats.getNumberOfSatisfied() + stats.getPercentageBonus())
                );
            }

            neighbors.sort((o1, o2) -> Double.compare(o2.fitness, o1.fitness));
            int index = rand.nextInt(NUMBER_OF_BEST);
            assignment = neighbors.get(index).bitVector;
        }

        return null;
    }


    private static class BitVectorWithFitness {
        private BitVector bitVector;
        private double fitness;

        public BitVectorWithFitness(BitVector bitVector, double fitness) {
            this.bitVector = bitVector;
            this.fitness = fitness;
        }
    }

}
