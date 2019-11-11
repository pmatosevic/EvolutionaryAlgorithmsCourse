package hr.fer.zemris.optjava.dz5.part1;

import java.util.BitSet;
import java.util.Objects;

public class Chromosome implements Comparable<Chromosome> {

    private BitSet bits;

    public Chromosome(BitSet bits) {
        this.bits = bits;
    }

    public Chromosome(int n) {
        this.bits = new BitSet(n);
    }

    public double getFitness() {
        double ratio = (double) bits.cardinality() / bits.size();
        if (ratio < 0.8) return ratio;
        if (ratio > 0.9) return (2.0*bits.cardinality() / bits.size()) - 1;
        else return 0.8;
    }

    public BitSet getBits() {
        return bits;
    }

    public Chromosome clone() {
        return new Chromosome((BitSet) bits.clone());
    }

    @Override
    public int compareTo(Chromosome o) {
        return Double.compare(o.getFitness(), this.getFitness());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Chromosome that = (Chromosome) o;
        return Objects.equals(bits, that.bits);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bits);
    }
}
