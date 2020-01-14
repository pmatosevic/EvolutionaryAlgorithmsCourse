package hr.fer.zemris.optjava.dz8.de;

import hr.fer.zemris.optjava.dz8.Algorithm;
import hr.fer.zemris.optjava.dz8.ann.ANN;

import java.util.*;

public class DifferentialEvolution implements Algorithm {

    private int populationSize;
    private int dimension;
    private double fValue;
    private double crValue;
    private ANN ann;
    private Random rand = new Random();

    public DifferentialEvolution(int populationSize, double fValue, double crValue, ANN ann) {
        this.populationSize = populationSize;
        this.fValue = fValue;
        this.crValue = crValue;
        this.ann = ann;
        dimension = ann.getWeightsCount();
    }

    @Override
    public double[] run(int maxIter, double minErr) {
        VectorSolution[] population = initialize();
        VectorSolution bestSolution = new VectorSolution(null, Double.POSITIVE_INFINITY);
        VectorSolution[] newPopulation = new VectorSolution[populationSize];

        for (int iter = 0; iter < maxIter; iter++) {
            for (int i = 0; i < populationSize; i++) {
                Set<Integer> usedNumbers = new HashSet<>(i);
                int r0 = randomSelect(usedNumbers);
                int r1 = randomSelect(usedNumbers);
                int r2 = randomSelect(usedNumbers);
                int r3 = randomSelect(usedNumbers);
                int r4 = randomSelect(usedNumbers);

                double[] target = population[i].weights;
                double[] mutant = new double[dimension];
                for (int d = 0; d < dimension; d++) {
                    mutant[d] = population[r0].weights[d]
                            + fValue * (population[r1].weights[d] - population[r2].weights[d]);
                            //+ fValue * (population[r3].weights[d] - population[r4].weights[d]);
                }

                int randCopy = rand.nextInt(dimension);
                double[] trial = new double[dimension];
                for (int d = 0; d < dimension; d++) {
                    if (rand.nextDouble() < crValue || d == randCopy) {
                        trial[d] = mutant[d];
                    } else {
                        trial[d] = target[d];
                    }
                }

                VectorSolution targetVec = population[i];
                VectorSolution trialVec = new VectorSolution(trial, ann.calculateError(trial));
                if (targetVec.error < trialVec.error) {
                    newPopulation[i] = targetVec;
                } else {
                    newPopulation[i] = trialVec;
                    if (trialVec.error < bestSolution.error) {
                        bestSolution = trialVec;
                    }
                }
            }
            population = newPopulation;
            if (bestSolution.error < minErr) break;
        }

        return bestSolution.weights;
    }

    private VectorSolution[] initialize() {
        VectorSolution[] population = new VectorSolution[populationSize];
        for (int i = 0; i < populationSize; i++) {
            double[] weights = new double[dimension];
            for (int j = 0; j < dimension; j++) {
                weights[j] = rand.nextDouble()*2 - 1;
            }
            double error = ann.calculateError(weights);
            population[i] = new VectorSolution(weights, error);
        }
        return population;
    }

    private int randomSelect(Set<Integer> usedNumbers) {
        int num;
        do {
            num = rand.nextInt(populationSize);
        } while (usedNumbers.contains(num));
        usedNumbers.add(num);
        return num;
    }

    private static class VectorSolution {
        private double[] weights;
        private double error;

        private VectorSolution(double[] weights, double error) {
            this.weights = weights;
            this.error = error;
        }
    }

}
