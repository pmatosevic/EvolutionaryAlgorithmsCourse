package hr.fer.zemris.trisat;

import java.util.Arrays;

public class MutableBitVector extends BitVector {

    public MutableBitVector(boolean... bits) {
        this.bits = Arrays.copyOf(bits, bits.length);
    }

    public MutableBitVector(int n) {
        this.bits = new boolean[n];
    }

    public void set(int index, boolean value) {
        bits[index] = value;
    }

}
