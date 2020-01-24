package hr.fer.zemris.optjava.dz11.part2;

import hr.fer.zemris.optjava.dz11.Chromosome;

import java.util.List;

public class WorkerJob {

    private List<Chromosome> population;
    private int numChildren;

    public WorkerJob(List<Chromosome> population, int numChildren) {
        this.population = population;
        this.numChildren = numChildren;
    }

    public List<Chromosome> getPopulation() {
        return population;
    }

    public int getNumChildren() {
        return numChildren;
    }
}
