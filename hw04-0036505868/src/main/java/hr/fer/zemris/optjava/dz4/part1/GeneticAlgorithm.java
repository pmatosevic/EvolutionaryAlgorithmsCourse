package hr.fer.zemris.optjava.dz4.part1;

import hr.fer.zemris.optjava.dz4.part1.crossover.BlxAlphaCrossover;
import hr.fer.zemris.optjava.dz4.part1.crossover.Crossover;
import hr.fer.zemris.optjava.dz4.part1.function.IFunction;
import hr.fer.zemris.optjava.dz4.part1.function.TransferFunction;
import hr.fer.zemris.optjava.dz4.part1.mutation.ModificationMutation;
import hr.fer.zemris.optjava.dz4.part1.mutation.Mutation;
import hr.fer.zemris.optjava.dz4.part1.selection.RouletteWheelSelection;
import hr.fer.zemris.optjava.dz4.part1.selection.Selection;
import hr.fer.zemris.optjava.dz4.part1.selection.TournamentSelection;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GeneticAlgorithm {

    private static final double MIN_VALUE = -3;
    private static final double MAX_VALUE = 3;
    private static final double ALPHA = 0.5;
    private static final double MUTATION_PROBABILITY = 0.3;

    private static Random rand = new Random();
    private static final int NUM_VARIABLES = 6;
    private static final String FILE_TRANSFER_FUNCTION = "02-zad-prijenosna.txt";

    public static void main(String[] args) throws IOException {
        int populationSize = Integer.parseInt(args[0]);
        double errorBound = Double.parseDouble(args[1]);
        int maxGeneration = Integer.parseInt(args[2]);
        Selection selection = args[3].equals("rouletteWheel") ? new RouletteWheelSelection() :
                new TournamentSelection(Integer.parseInt(args[3].split(":")[1]));
        double sigma = Double.parseDouble(args[4]);

        TransferFunction function = TransferFunction.loadFunctionFromFile(Paths.get(FILE_TRANSFER_FUNCTION));
        Mutation mutation = new ModificationMutation(sigma, MUTATION_PROBABILITY);
        Crossover crossover = new BlxAlphaCrossover(ALPHA);

        List<Chromosome> population = initialPopulation(populationSize);
        evaluatePopulation(function, population);
        Collections.sort(population);

        for (int i = 0; i < maxGeneration; i++) {
            List<Chromosome> newPopulation = new ArrayList<>();
            newPopulation.add(population.get(0).clone());
            newPopulation.add(population.get(1).clone());

            while (newPopulation.size() < populationSize) {
                Chromosome parent1 = selection.select(population);
                Chromosome parent2 = selection.select(population);
                Chromosome child = crossover.crossover(parent1, parent2);
                mutation.mutate(child);
                newPopulation.add(child);
            }

            population = newPopulation;
            evaluatePopulation(function, population);
            Collections.sort(population);

            Chromosome best = population.get(0);
            System.err.printf("Generation: %d, fitness: %f, best: %s%n", i, best.getFitness(), printChromosome(best));

            if (-best.getFitness() < errorBound) break;
        }

        Chromosome best = population.get(0);
        System.out.println("Best solution: " + printChromosome(best) + " (fitness: " + best.getFitness() + ")");
    }

    private static String printChromosome(Chromosome c) {
        double[] values = c.getValues();
        return String.format("a=%f, b=%f, c=%f, d=%f, e=%f, f=%f",
                values[0], values[1], values[2],
                values[3], values[4], values[5]
        );
    }

    private static List<Chromosome> initialPopulation(int populationSize) {
        return IntStream.range(0, populationSize)
                .mapToObj(i -> new Chromosome(NUM_VARIABLES, rand, MIN_VALUE, MAX_VALUE))
                .collect(Collectors.toList());
    }

    private static void evaluatePopulation(IFunction function, List<Chromosome> chromosomes) {
        chromosomes.forEach(c -> evaluateChromosome(c, function));
    }

    private static void evaluateChromosome(Chromosome chromosome, IFunction function) {
        chromosome.setFitness(-function.valueAt(chromosome.getValues()));
    }



}
