package hr.fer.zemris.optjava.dz5.part1;

import hr.fer.zemris.optjava.dz5.Crossover;
import hr.fer.zemris.optjava.dz5.Mutation;
import hr.fer.zemris.optjava.dz5.Selection;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GeneticAlgorithm {

    private static final int MIN_POPULATION = 5;
    private static final int MAX_POPULATION = 30;
    private static final int SELECTION_K = 2;
    private static final double MUT_PROBABILITY = 0.01;
    private static final double MAX_SEL_PRESS = 0.05;
    private static final double COMP_FACTOR_INIT = 0.00;
    private static final boolean RANDOM_SECOND_PARENT = true;

    private static final Random rand = new Random();
    private static final int MAX_ITERATIONS = 200000;
    private static final int MAX_TRY_PER_POP = 1000;
    private static int n;

    public static void main(String[] args) {
        n = Integer.parseInt(args[0]);

        Crossover<Chromosome> crossover = new PointCrossover();
        Mutation<Chromosome> mutation = new BitMutation(1.0 / n);
        Selection<Chromosome> tournSel = new TournamentSelection(SELECTION_K);
        Selection<Chromosome> randSel = new RandomSelection();

        double compFactor = COMP_FACTOR_INIT;
        List<Chromosome> population = initialPopulation();
        Collections.sort(population);
        Chromosome best = population.get(0);
        
        for (int iter = 0; iter < MAX_ITERATIONS; iter++) {
            HashSet<Chromosome> newPopulation = new HashSet<>();
            HashSet<Chromosome> pool = new HashSet<>();

            int tries = 0;
            while (newPopulation.size() < MAX_POPULATION && tries < MAX_TRY_PER_POP) {
                tries++;
                Chromosome parent1 = tournSel.select(population);
                Chromosome parent2 = RANDOM_SECOND_PARENT ? randSel.select(population) : tournSel.select(population);
                Chromosome child = crossover.crossover(parent1, parent2);
                mutation.mutate(child);

                double fitnessLimit = calcFitLimit(parent1, parent2, compFactor);
                if (child.getFitness() >= fitnessLimit) {
                    newPopulation.add(child);
                } else {
                    pool.add(child);
                }
            }

            population.clear();
            population.addAll(newPopulation);
            if (population.size() < MIN_POPULATION) {
                population.addAll(pool.stream().limit(MIN_POPULATION).collect(Collectors.toList()));
            }
            Collections.sort(population);

            best = best.compareTo(population.get(0)) < 0 ? best : population.get(0);
            printChromosome(best);
            if (best.getFitness() == 1) break;

            //compFactor = Math.min(1, compFactor * 1.01);
        }

        System.out.println("\nBest solution:");
        printChromosome(best);

    }

    private static void printChromosome(Chromosome best) {
        System.out.println("Solution: " + bitToStr(best.getBits()));
        System.out.println("k/n = " + best.getFitness());
    }

    private static String bitToStr(BitSet bits) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bits.size(); i++) {
            sb.append(bits.get(i) ? "1" : "0");
        }
        return sb.toString();
    }

    private static double calcFitLimit(Chromosome parent1, Chromosome parent2, double compFactor) {
        double fitMin = Math.min(parent1.getFitness(), parent2.getFitness());
        double fitMax = Math.max(parent1.getFitness(), parent2.getFitness());
        return fitMin + compFactor * (fitMax - fitMin);
    }

    private static List<Chromosome> initialPopulation() {
        return IntStream.range(0, MAX_POPULATION)
                .mapToObj(idx -> {
                    BitSet bits = new BitSet(n);
                    for (int i = 0; i < n; i++) {
                        if (rand.nextDouble() < 0.5) bits.flip(i);
                    }
                    return new Chromosome(bits);
                })
                .collect(Collectors.toList());
    }

}
