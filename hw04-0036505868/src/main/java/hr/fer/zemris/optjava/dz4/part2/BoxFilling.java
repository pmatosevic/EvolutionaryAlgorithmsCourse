package hr.fer.zemris.optjava.dz4.part2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BoxFilling {

    private static Random rand = new Random();

    public static void main(String[] args) throws IOException {
        String file = args[0];
        int populationSize = Integer.parseInt(args[1]);
        int n = Integer.parseInt(args[2]);
        int m = Integer.parseInt(args[3]);
        boolean p = Boolean.parseBoolean(args[4]);
        int maxIterations = Integer.parseInt(args[5]);
        int widthLimit = Integer.parseInt(args[6]);
        double mutationProbability = Double.parseDouble(args[7]);
        int packetSize = 20;

        List<Integer> boxSizes = loadBoxSizes(Paths.get(file));
        List<Item> allItems = new ArrayList<>();
        for (int i = 0; i < boxSizes.size(); i++) {
            allItems.add(new Item(i, boxSizes.get(i)));
        }

        List<Chromosome> population = initializePopulation(populationSize, packetSize, allItems);
        evaluateChromosomes(population);
        Collections.sort(population);
        for (int i = 0; i < maxIterations; i++) {
            Chromosome parent1 = tournamentSelection(population, n);
            Chromosome parent2 = tournamentSelection(population, n);
            Chromosome child = crossover(parent1, parent2, allItems);
            mutate(child, mutationProbability);
            evaluateChromosome(child);
            torunamentDelete(population, m, child, p);
            Collections.sort(population);

            System.err.println("Iteration " + i + " fitness: " + population.get(0).getFitness());

            if (population.get(0).getPackaging().size() <= widthLimit) break;
        }

        System.out.println("Min boxes: " + population.get(0).getPacketNum());
        printSolution(population.get(0));
    }

    private static void printSolution(Chromosome chromosome) {
        System.out.println("Solution:");
        for (int i = 0; i < chromosome.getPackaging().size(); i++) {
            System.out.printf("Box %02d: %s%n", i,
                    chromosome.getPackaging().get(i).getItems().stream()
                            .map(b -> String.valueOf(b.getSize())).collect(Collectors.joining(", ")));
        }
    }

    private static Chromosome crossover(Chromosome a, Chromosome b, List<Item> allItems) {
        int minBoxes = Math.min(a.getPacketNum(), b.getPacketNum());
        int cutPoint = rand.nextInt(minBoxes - 1) + 1;

        List<Packet> packets1 = a.getPackaging().subList(0, cutPoint);
        List<Packet> packets2 = b.getPackaging().subList(cutPoint, b.getPacketNum());
        List<Packet> newPackets = new ArrayList<>();
        packets1.forEach(p -> newPackets.add(p.clone()));
        packets2.forEach(p -> newPackets.add(p.clone()));

        Set<Item> copiedItems = new HashSet<>();
        for (Packet p : newPackets) {
            Iterator<Item> it = p.getItems().iterator();
            while (it.hasNext()) {
                if (!copiedItems.add(it.next())) it.remove();
            }
        }
        Set<Item> remaining = new HashSet<>(allItems);
        remaining.removeAll(copiedItems);

        Chromosome c = new Chromosome(a.getPacketSize(), newPackets);
        remaining.stream().sorted().forEach(item -> c.addItem(item));
        c.removeEmpty();
        return c;
    }

    private static void mutate(Chromosome c, double mutationProbability) {
        List<Item> removed = new ArrayList<>();
        for (Packet p : c.getPackaging()) {
            if (rand.nextDouble() > mutationProbability) continue;
            Item item = p.getItems().get(rand.nextInt(p.getItems().size()));
            p.removeItem(item);
            removed.add(item);
        }
        removed.sort(null);
        for (Item item : removed) {
            c.addItem(item);
        }
        c.removeEmpty();
    }

    private static Chromosome tournamentSelection(List<Chromosome> population, int n) {
        List<Chromosome> tournament = new ArrayList<>();
        for (int i = 0; i < n; i++) tournament.add(population.get(rand.nextInt(population.size())));
        return tournament.stream().max(Comparator.comparing(Chromosome::getFitness)).get();
    }

    private static void torunamentDelete(List<Chromosome> population, int m, Chromosome child, boolean p) {
        List<Chromosome> tournament = new ArrayList<>();
        for (int i = 0; i < m; i++) tournament.add(population.get(rand.nextInt(population.size())));
        Chromosome worst = tournament.stream().min(Comparator.comparing(Chromosome::getFitness)).get();
        if (!p || child.getFitness() >= worst.getFitness()) {
            population.remove(worst);
            population.add(child);
        }
    }

    private static List<Chromosome> initializePopulation(int populationSize, int packetSize, List<Item> items) {
        return IntStream.range(0, populationSize)
                .mapToObj(i -> new Chromosome(packetSize, items, rand))
                .collect(Collectors.toList());
    }

    private static void evaluateChromosomes(List<Chromosome> population) {
        population.forEach(c -> evaluateChromosome(c));
    }

    private static void evaluateChromosome(Chromosome c) {
        c.setFitness(-c.getPackaging().size());
    }


    private static List<Integer> loadBoxSizes(Path file) throws IOException {
        String input = Files.readString(file);
        String[] parts = input.replace("[", "").replace("]", "").split(",");
        return Arrays.stream(parts).map(s -> Integer.parseInt(s.trim())).sorted().collect(Collectors.toList());
    }

}
