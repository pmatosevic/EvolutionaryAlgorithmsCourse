package hr.fer.zemris.trisat;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.Stream;

public class BitVector {

    protected boolean bits[];

    public BitVector(Random rand, int numberOfBits) {
        bits = new boolean[numberOfBits];
        for (int i = 0; i < numberOfBits; i++) {
            bits[i] = rand.nextBoolean();
        }
    }

    public BitVector(boolean... bits) {
        this.bits = Arrays.copyOf(bits, bits.length);
    }

    public BitVector(int n) {
        bits = new boolean[n];
    }

    public boolean get(int index) {
        return bits[index];
    }

    public int getSize() {
        return bits.length;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bits.length; i++) {
            sb.append(bits[i] ? "1" : "0");
        }
        return sb.toString();
    }

    public MutableBitVector copy() {
        return new MutableBitVector(bits);
    }
}
