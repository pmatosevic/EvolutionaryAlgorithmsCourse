package hr.fer.zemris.optjava.dz9;

import hr.fer.zemris.optjava.dz9.crossover.BasicCrossover;
import hr.fer.zemris.optjava.dz9.crossover.BlxAlphaCrossover;
import hr.fer.zemris.optjava.dz9.crossover.Crossover;
import hr.fer.zemris.optjava.dz9.mutation.BasicMutation;
import hr.fer.zemris.optjava.dz9.mutation.Mutation;
import hr.fer.zemris.optjava.dz9.problem.MOOPProblem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static hr.fer.zemris.optjava.dz9.SolutionSpace.DECISION_SPACE;
import static hr.fer.zemris.optjava.dz9.SolutionSpace.OBJECTIVE_SPACE;

public class NSGA {

    private Random rand = new Random();
    private MOOPProblem problem;
    private SolutionSpace space;
    private int populationSize;
    private double sigma;
    private Crossover crossover = new BlxAlphaCrossover();
    private Mutation mutation = new BasicMutation();
    private double epsilon;

    public NSGA(MOOPProblem problem, int populationSize, double sigma, SolutionSpace space) {
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

        for (int iter = 0; iter < maxIter; iter++) {
            List<List<Chromosome>> fronts = NondominatedSort.sort(population);

            calculateFitness(fronts);

            List<Chromosome> newPopulation = new ArrayList<>();
            while (newPopulation.size() < populationSize) {
                Chromosome parent1 = select(population);
                Chromosome parent2 = select(population);
                Chromosome child = crossover.crossover(parent1, parent2);
                mutation.mutate(child);
                postMutationProcess(child);
                newPopulation.add(child);
            }
            population = newPopulation;
            evaluate(population);
        }

        calculateFitness(NondominatedSort.sort(population));
        return population;
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

    private Chromosome select(List<Chromosome> population) {
        double fitnessSum = population.stream().mapToDouble(c -> c.fitness).sum();
        double selection = rand.nextDouble() * fitnessSum;
        for (Chromosome c : population) {
            if (selection < c.fitness) return c;
            selection -= c.fitness;
        }
        return population.get(population.size() - 1);
    }

}
