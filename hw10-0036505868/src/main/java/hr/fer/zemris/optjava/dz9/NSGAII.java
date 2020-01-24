package hr.fer.zemris.optjava.dz9;

import hr.fer.zemris.optjava.dz9.crossover.BasicCrossover;
import hr.fer.zemris.optjava.dz9.crossover.BlxAlphaCrossover;
import hr.fer.zemris.optjava.dz9.crossover.Crossover;
import hr.fer.zemris.optjava.dz9.mutation.BasicMutation;
import hr.fer.zemris.optjava.dz9.mutation.Mutation;
import hr.fer.zemris.optjava.dz9.problem.MOOPProblem;

import javax.swing.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static hr.fer.zemris.optjava.dz9.SolutionSpace.DECISION_SPACE;
import static hr.fer.zemris.optjava.dz9.SolutionSpace.OBJECTIVE_SPACE;

public class NSGAII {

    private static final double DIST_INF = 1e9;
    private Random rand = new Random();
    private MOOPProblem problem;
    private SolutionSpace space;
    private int populationSize;
    private int tournamentSize = 4;
    private double sigma;
    private Crossover crossover = new BlxAlphaCrossover();
    private Mutation mutation = new BasicMutation();
    private double epsilon;

    public NSGAII(MOOPProblem problem, int populationSize, double sigma, SolutionSpace space) {
        this.problem = problem;
        this.populationSize = populationSize;
        this.sigma = sigma;
        this.space = space;
        this.epsilon = 1/populationSize;
    }

    public List<Chromosome> run(int maxIter) {
        List<Chromosome> population = IntStream.range(0, populationSize)
                .mapToObj(idx -> initChromosome())
                .collect(Collectors.toList());
        evaluate(population);
        List<List<Chromosome>> initialFronts = NondominatedSort.sort(population);
        calculateDistancesForFronts(initialFronts);

        for (int iter = 0; iter < maxIter; iter++) {
            List<Chromosome> pool = new ArrayList<>();
            while (pool.size() < populationSize) {
                Chromosome parent1 = selectCts(population);
                Chromosome parent2 = selectCts(population);
                Chromosome child = crossover.crossover(parent1, parent2);
                mutation.mutate(child);
                postMutationProcess(child);
                pool.add(child);
            }
            pool.addAll(population);
            evaluate(pool);

            List<List<Chromosome>> fronts = NondominatedSort.sort(pool);
            List<Chromosome> lastFront = null;
            List<Chromosome> newPopulation = new ArrayList<>();
            for (int i = 0; i < fronts.size(); i++) {
                if (fronts.get(i).size() <= populationSize - newPopulation.size()) {
                    newPopulation.addAll(fronts.get(i));
                } else {
                    if (newPopulation.size() < populationSize) lastFront = fronts.get(i);
                    break;
                }
            }
            if (lastFront != null) {
                List<Chromosome> trimmedFront = lastFront.stream()
                        .sorted(Chromosome.DIST_COMPARATOR)
                        .limit(populationSize - newPopulation.size())
                        .collect(Collectors.toList());
                calculateDistances(trimmedFront);
                newPopulation.addAll(trimmedFront);
            }

            population = newPopulation;
        }

        calculateFitness(NondominatedSort.sort(population));
        return population;
    }

    private void calculateDistancesForFronts(List<List<Chromosome>> initialFronts) {
        initialFronts.forEach(this::calculateDistances);
    }

    private void calculateDistances(List<Chromosome> chromosomes) {
        chromosomes.forEach(c -> c.distance = 0);

        for (int index = 0; index < problem.getNumberOfObjectives(); index++) {
            double norm = problem.objMax(index) - problem.objMin(index);
            final int objIndex = index;
            List<Chromosome> sorted = chromosomes.stream()
                    .sorted(Comparator.comparingDouble(c -> c.objectives[objIndex]))
                    .collect(Collectors.toList());

            sorted.get(0).distance += DIST_INF;
            sorted.get(sorted.size() - 1).distance += DIST_INF;

            for (int i = 1; i < sorted.size() - 1; i++) {
                double diff = sorted.get(i+1).objectives[index] - sorted.get(i-1).objectives[index];
                sorted.get(i).distance += diff / norm;
            }
        }
    }

    private void postMutationProcess(Chromosome c) {
        for (int i = 0; i < c.solution.length; i++) {
            if (c.solution[i] < problem.xMin(i)) c.solution[i] = problem.xMin(i);
            if (c.solution[i] > problem.xMax(i)) c.solution[i] = problem.xMax(i);
        }
    }

    private void calculateFitness(List<List<Chromosome>> fronts) {
        double frontFitness = populationSize;
        for (List<Chromosome> front : fronts) {
            for (Chromosome chromosome : front) {
                double nc = 0;
                for (Chromosome other : front) {
                    nc += fitShare(chromosome, other);
                }
                chromosome.fitness = frontFitness / nc;
            }

            double minFitness = front.stream().mapToDouble(c -> c.fitness).min().getAsDouble();
            frontFitness = minFitness - epsilon;
        }
    }

    private double fitShare(Chromosome a, Chromosome b) {
        if (a == b) return 1;

        int n = a.solution.length;
        double sum = 0;
        for (int i = 0; i < n; i++) {
            double currDiff = (space == DECISION_SPACE) ? a.solution[i] - b.solution[i] : a.objectives[i] - b.objectives[i];
            double maxDiff = (space == OBJECTIVE_SPACE) ? problem.xMax(i) - problem.xMin(i) : problem.objMax(i) - problem.objMin(i);
            sum += Math.pow(currDiff / maxDiff, 2);
        }
        double d = Math.sqrt(sum);

        if (d < sigma) {
            return 1 - Math.pow(d /sigma, 2);
        } else {
            return 0;
        }
    }


    private Chromosome initChromosome() {
        double[] solution = new double[problem.getNumberOfObjectives()];
        for (int i = 0; i < solution.length; i++) {
            solution[i] = rand.nextDouble() * (problem.xMax(i) - problem.xMin(i)) + problem.xMin(i);
        }
        return new Chromosome(solution);
    }

    private void evaluate(List<Chromosome> population) {
        for (Chromosome c : population) {
            c.objectives = problem.evaluateSolution(c.solution);
        }
    }

    private Chromosome selectRoulette(List<Chromosome> population) {
        double fitnessSum = population.stream().mapToDouble(c -> c.fitness).sum();
        double selection = rand.nextDouble() * fitnessSum;
        for (Chromosome c : population) {
            if (selection < c.fitness) return c;
            selection -= c.fitness;
        }
        return population.get(population.size() - 1);
    }

    private Chromosome selectCts(List<Chromosome> population) {
        Chromosome selected = population.get(rand.nextInt(populationSize));
        for (int i = 0; i < tournamentSize; i++) {
            Chromosome potential = population.get(rand.nextInt(populationSize));
            if (Chromosome.CTS_COMPARATOR.compare(potential, selected) <= 0) {
                selected = potential;
            }
        }
        return selected;
    }

}
