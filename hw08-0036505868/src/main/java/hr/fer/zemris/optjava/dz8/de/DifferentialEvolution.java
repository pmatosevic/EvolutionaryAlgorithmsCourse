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
        double[][] population = initialize();
        double[] bestSolution = null;
        double bestError = Double.POSITIVE_INFINITY;
        double[][] newPopulation = new double[populationSize][];

        for (int iter = 0; iter < maxIter; iter++) {
            for (int i = 0; i < populationSize; i++) {
                Set<Integer> usedNumbers = new HashSet<>(i);
                int r0 = randomSelect(usedNumbers);
                int r1 = randomSelect(usedNumbers);
                int r2 = randomSelect(usedNumbers);

                double[] target = population[i];
                double[] mutant = new double[dimension];
                for (int d = 0; d < dimension; d++) {
                    mutant[d] = population[r0][d] + fValue * (population[r1][d] - population[r2][d]);
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

                double targetError = ann.calculateError(target);
                double trialError = ann.calculateError(trial);
                if (targetError < trialError) {
                    newPopulation[i] = target;
                } else {
                    newPopulation[i] = trial;
                    if (trialError < bestError) {
                        bestSolution = trial;
                        bestError = trialError;
                    }
                }
            }
            population = newPopulation;
            if (bestError < minErr) break;
        }

        return bestSolution;
    }

    private double[][] initialize() {
        double[][] population = new double[populationSize][dimension];
        for (int i = 0; i < populationSize; i++) {
            for (int j = 0; j < dimension; j++) {
                population[i][j] = rand.nextDouble()*2 - 1;
            }
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
    }

}
