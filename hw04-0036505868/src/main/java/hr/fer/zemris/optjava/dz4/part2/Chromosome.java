package hr.fer.zemris.optjava.dz4.part2;

import java.util.*;
import java.util.stream.Collectors;

public class Chromosome implements Comparable<Chromosome> {

    private double fitness;

    private int packetSize;
    private List<Packet> packaging;

    public Chromosome(int packetSize, List<Packet> packaging) {
        this.packetSize = packetSize;
        this.packaging = packaging;
    }

    public Chromosome(int packetSize, List<Item> items, Random rand) {
        this.packetSize = packetSize;
        packaging = new ArrayList<>();
        List<Item> shuffled = new ArrayList<>(items);
        Collections.shuffle(items, rand);

        for (int i = 0; i < items.size(); i++) {
            addItem(items.get(i));
        }
    }

    public void addItem(Item item) {
        boolean added = false;
        for (Packet p : packaging) {
            if (p.getSize() + item.getSize() < packetSize) {
                p.addItem(item);
                added = true;
                break;
            }
        }

        if (!added) {
            Packet p = new Packet();
            p.addItem(item);
            packaging.add(p);
        }
    }

    public void removeEmpty() {
        packaging.removeIf(p -> p.getItems().isEmpty());
    }

    public Chromosome clone() {
        Chromosome copy = new Chromosome(
                packetSize,
                packaging.stream().map(Packet::clone).collect(Collectors.toList())
        );
        copy.fitness = fitness;
        return copy;
    }

    public int getPacketNum() {
        return packaging.size();
    }

    public int getPacketSize() {
        return packetSize;
    }

    public List<Packet> getPackaging() {
        return packaging;
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    @Override
    public int compareTo(Chromosome o) {
        if (this.fitness < o.fitness) return 1;
        if (this.fitness > o.fitness) return -1;
        return 0;
    }
}
