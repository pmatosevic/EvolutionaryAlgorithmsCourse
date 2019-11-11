package hr.fer.zemris.trisat;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BitVectorNGenerator implements Iterable<MutableBitVector> {

    private BitVector initialAssignment;

    public BitVectorNGenerator(BitVector assignment) {
        this.initialAssignment = assignment;
    }

    @Override
    public Iterator<MutableBitVector> iterator() {
        return new Iterator<MutableBitVector>() {

            int pos = 0;

            @Override
            public boolean hasNext() {
                return pos < initialAssignment.getSize();
            }

            @Override
            public MutableBitVector next() {
                MutableBitVector assignment = initialAssignment.copy();
                assignment.set(pos, !initialAssignment.get(pos));
                pos++;
                return assignment;
            }

        };
    }

    public MutableBitVector[] createNeighborhood() {
        List<MutableBitVector> result = new ArrayList<>();
        for (MutableBitVector mutableBitVector : this) {
            result.add(mutableBitVector);
        }
        return result.toArray(new MutableBitVector[0]);
    }

}
