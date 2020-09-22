package hr.fer.zemris.optjava.dz13.algorithm;

import hr.fer.zemris.optjava.dz13.Map;
import hr.fer.zemris.optjava.dz13.nodes.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GPAlgorithm {

    private Random rand = new Random();

    private int populationSize;
    private int maxActions;
    private Map mainMap;

    private static final int MAX_DEPTH = 5;
    private static final int TOURNAMENT_K = 4;
    private static final double MUTATION_PROB = 0.2;
    private static final double CROSSOVER_PROB = 0.75;
    private static final double CROSSOVER_FUNCTIONAL_PROB = 0.75;
    private static final double PLAGIARISM_FINE = 0.95;
    private static final int ELITISM = 1;

    public GPAlgorithm(int populationSize, int maxActions, Map mainMap) {
        this.populationSize = populationSize;
        this.maxActions = maxActions;
        this.mainMap = mainMap;
    }

    public Chromosome run(int maxIter, double minFitness) {
        List<Chromosome> population = initialize();
        population.forEach(this::evaluate);
        population.sort(null);

        for (int iter = 0; iter < maxIter; iter++) {
            //System.err.println("Iter: " + iter + " -> " + population.get(0).fitness);

            List<Chromosome> newPopulation = new ArrayList<>();
            newPopulation.addAll(population.subList(0, ELITISM));
            while (newPopulation.size() < populationSize) {
                double randType = rand.nextDouble();
                if (randType < MUTATION_PROB) {
                    Chromosome parent = select(population);
                    Chromosome mutated = mutate(parent);

                    newPopulation.add(mutated);
                    evaluateWithPlagiarism(mutated, parent);
                } else if (randType < MUTATION_PROB + CROSSOVER_PROB) {
                    Chromosome parent1 = select(population);
                    Chromosome parent2 = select(population);
                    Chromosome crossed = crossover(parent1, parent2);

                    newPopulation.add(crossed);
                    evaluateWithPlagiarism(crossed, parent1);
                } else {
                    Chromosome selected = select(population).duplicate();
                    evaluate(selected);
                    newPopulation.add(selected);
                }
            }

            population = newPopulation;
            population.sort(null);
            if (population.get(0).fitness >= minFitness) break;
        }

        return population.get(0);
    }

    public List<Chromosome> initialize() {
        List<Chromosome> population = new ArrayList<>();
        for (int i = 0; i < populationSize; i++) {
            Node root = GPUtils.generateSubTree(MAX_DEPTH, rand.nextBoolean());
            GPUtils.annotate(root);

            population.add(new Chromosome(0, 0, root));
        }
        return population;
    }


    public Chromosome mutate(Chromosome parent) {
        Node newRoot = parent.root.clone();
        Chromosome child = new Chromosome(0, 0, newRoot);
        int selectedIndex = rand.nextInt(newRoot.getNodesCount());

        if (selectedIndex == 0) {
            child.root = GPUtils.generateSubTree(MAX_DEPTH, rand.nextBoolean());
            GPUtils.annotate(child.root);
            return child;
        }

        Node indexedParent = GPUtils.findParentOfIndexedNode(newRoot, selectedIndex);
        Node newSubTree = GPUtils.generateSubTree(MAX_DEPTH - (indexedParent.getDepth() + 1), rand.nextBoolean());

        GPUtils.insertSubTreeInsteadOfIndexedNode(indexedParent, selectedIndex, newSubTree);

        GPUtils.annotate(newRoot);
        return child;
    }

    public Chromosome crossover(Chromosome parent1, Chromosome parent2) {
        Node newRoot = parent1.root.clone();
        Node parent2Root = parent2.root.clone();
        Chromosome child = new Chromosome(0, 0, newRoot);

        if (newRoot.getNodesCount() <= 1) return child;                                     // Only terminal
        int selectedIndex1 = rand.nextInt(newRoot.getNodesCount() - 1) + 1;         // Do not select root

        Node indexedParent = GPUtils.findParentOfIndexedNode(newRoot, selectedIndex1);
        int availableDepth = MAX_DEPTH - (indexedParent.getDepth() + 1);

        Node secondSubTree;
        while (true) {
            int selectedIndex2 = rand.nextInt(parent2Root.getNodesCount());
            secondSubTree = GPUtils.findIndexedNode(parent2Root, selectedIndex2);
            int currentDepth = GPUtils.maxDepth(secondSubTree) - secondSubTree.getDepth() + 1;
            if (currentDepth <= availableDepth) break;
        }
        secondSubTree = secondSubTree;

        GPUtils.insertSubTreeInsteadOfIndexedNode(indexedParent, selectedIndex1, secondSubTree);

        GPUtils.annotate(newRoot);
        return child;
    }

    public Chromosome select(List<Chromosome> population) {
        Chromosome best = null;
        for (int i = 0; i < TOURNAMENT_K; i++) {
            Chromosome possible = population.get(rand.nextInt(populationSize));
            if (best == null || possible.fitness >= best.fitness) best = possible;
        }
        return best;
    }

    private void evaluateWithPlagiarism(Chromosome child, Chromosome parent) {
        evaluate(child);
        if (parent.food == child.food) {
            child.fitness = PLAGIARISM_FINE * child.fitness;
        }
    }

    private void evaluate(Chromosome c) {
        Ant ant = new Ant(maxActions, mainMap.duplicate());
        try {
            while (true) {
                c.root.execute(ant);
            }
        } catch (IllegalStateException ignored) {}

        c.food = ant.getResult();
        c.fitness = c.food;
    }





}
