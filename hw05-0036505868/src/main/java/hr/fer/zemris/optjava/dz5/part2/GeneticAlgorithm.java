package hr.fer.zemris.optjava.dz5.part2;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GeneticAlgorithm {

    private static final double MAX_SEL_PRESS = 10000;
    private static final int MAX_ITERTIONS = 20000;
    private static final double SUCC_RATIO = 0.5;
    private static final int TOURN_K = 3;
    private static final double COMP_FACTOR = 0.2;
    private static int populationSize;
    private static int initialSubPopulations;
    private static FactoriesData data;

    public static void main(String[] args) throws IOException {
        Path file = Paths.get(args[0]);
        populationSize = Integer.parseInt(args[1]);
        initialSubPopulations = Integer.parseInt(args[2]);
        data = FactoriesData.loadFromFile(file);

        OffspringSelection offspringSelection = new OffspringSelection(COMP_FACTOR, MAX_SEL_PRESS, SUCC_RATIO, MAX_ITERTIONS,
                new PermutingMutation(), new OrderedCrossover(), new TournamentSelection(TOURN_K), data);

        List<Chromosome> population = initializePopulation(populationSize);
        population.forEach(c -> c.evaluate(data));
        int subPopsNum = initialSubPopulations;
        while (subPopsNum >= 1) {
            List<List<Chromosome>> finishedSubPopulations = new ArrayList<>();
            int end = 0;
            for (int i = 0; i < subPopsNum; i++) {
                int begin = end;
                end = Math.min(populationSize, (i+1) * populationSize / subPopsNum);
                List<Chromosome> sub = new ArrayList<>(population.subList(begin, end));
                List<Chromosome> result = offspringSelection.run(sub);
                finishedSubPopulations.add(result);
            }
            population.clear();
            for (List<Chromosome> finishedSubPopulation : finishedSubPopulations) {
                population.addAll(finishedSubPopulation);
            }

            System.err.println("Iteration with " + subPopsNum + " subpopulations done.");
            subPopsNum--;
        }

        Collections.sort(population);
        Chromosome best = population.get(0);
        System.out.println(Arrays.stream(best.getPermutation()).mapToObj(i->String.valueOf(i)).collect(Collectors.joining(", ")));
        System.out.println("Best fitness: " + best.getFitness());
    }

    private static List<Chromosome> initializePopulation(int populationSize) {
        return IntStream.range(0, populationSize)
                .mapToObj(idx -> new Chromosome(data.getN()))
                .collect(Collectors.toList());
    }

}
