package hr.fer.zemris.optjava.hw03.solution;

public abstract class SingleObjectiveSolution implements Comparable<SingleObjectiveSolution> {

    public double fitness;
    public double value;

    @Override
    public int compareTo(SingleObjectiveSolution o) {
        return Double.compare(this.fitness, o.fitness);
    }

}
