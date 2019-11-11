package hr.fer.zemris.trisat.algorithm;

import hr.fer.zemris.trisat.BitVector;
import hr.fer.zemris.trisat.BitVectorNGenerator;
import hr.fer.zemris.trisat.SATFormula;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BasicAlgorithms {

    public static BitVector solveIteration(SATFormula satFormula, int maxIterations, Random rand) {
        BitVector assignment = tryToSolve(satFormula, maxIterations, rand, new BitVector(rand, satFormula.getNumberOfVariables()));
        if (satFormula.isSatisfied(assignment)) return assignment;
        return null;
    }

    public static BitVector tryToSolve(SATFormula satFormula, int maxIterations, Random rand, BitVector initialAssignment) {
        BitVector assignment = initialAssignment;
        int currentFitness = satFormula.countSatisfiedClauses(assignment);
        if (currentFitness == satFormula.getNumberOfClauses()) return assignment;

        for (int iter = 0; iter < maxIterations; iter++) {
            List<BitVector> bestNeighbors = new ArrayList<>();
            int newBestFitness = -1;

            for (BitVector neighbor : new BitVectorNGenerator(assignment)) {
                int fitness = satFormula.countSatisfiedClauses(neighbor);
                if (fitness == satFormula.getNumberOfClauses()) return neighbor;

                if (fitness > newBestFitness) {
                    bestNeighbors.clear();
                    bestNeighbors.add(neighbor);
                    newBestFitness = fitness;
                } else if (fitness == newBestFitness) {
                    bestNeighbors.add(neighbor);
                }
            }

            if (newBestFitness < currentFitness) {
                return assignment;                  // local optima
            }
            assignment = bestNeighbors.get(rand.nextInt(bestNeighbors.size()));
            currentFitness = newBestFitness;
        }

        return assignment;
    }


}
