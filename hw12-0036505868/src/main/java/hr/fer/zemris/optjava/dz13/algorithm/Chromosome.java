package hr.fer.zemris.optjava.dz13.algorithm;

import hr.fer.zemris.optjava.dz13.nodes.Node;

public class Chromosome implements Comparable<Chromosome> {

    double fitness;
    int food;
    Node root;

    public Chromosome() {
    }

    public Chromosome(double fitness, int food, Node root) {
        this.fitness = fitness;
        this.food = food;
        this.root = root;
    }

    public double getFitness() {
        return fitness;
    }

    public int getFood() {
        return food;
    }

    public Node getRoot() {
        return root;
    }

    public Chromosome duplicate() {
        return new Chromosome(fitness, food, root.clone());
    }

    @Override
    public int compareTo(Chromosome o) {
        return Double.compare(o.fitness, this.fitness);
    }
}
