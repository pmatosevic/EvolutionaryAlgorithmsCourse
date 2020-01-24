package hr.fer.zemris.optjava.dz9;

import java.util.Comparator;

public class Chromosome {

    public double[] solution;
    public double[] objectives;
    public double fitness;

    public double distance;
    public int rank;


    public Chromosome(double[] solution) {
        this.solution = solution;
    }

    public static Comparator<Chromosome> CTS_COMPARATOR = (c1, c2) -> {
        if (c1.rank < c2.rank) return -1;
        if (c1.rank == c2.rank && c1.distance > c2.distance) return -1;
        if (c1.rank == c2.rank && c1.distance == c2.distance) return 0;
        return 1;
    };

    public static Comparator<Chromosome> DIST_COMPARATOR = (c1, c2) -> Double.compare(c2.distance, c1.distance);

}
