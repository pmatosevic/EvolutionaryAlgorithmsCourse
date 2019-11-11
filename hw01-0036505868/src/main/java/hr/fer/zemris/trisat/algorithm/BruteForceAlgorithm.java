package hr.fer.zemris.trisat.algorithm;

import hr.fer.zemris.trisat.BitVector;
import hr.fer.zemris.trisat.MutableBitVector;
import hr.fer.zemris.trisat.SATFormula;
import hr.fer.zemris.trisat.SolvingAlgorithm;

import java.util.ArrayList;
import java.util.List;

public class BruteForceAlgorithm implements SolvingAlgorithm {

    private List<BitVector> solutions = new ArrayList<>();

    @Override
    public BitVector solve(SATFormula satFormula) {
        System.err.println("All solutions:");
        recursiveCheck(satFormula, new MutableBitVector(satFormula.getNumberOfVariables()), 0);
        return solutions.size() > 0 ? solutions.get(0) : null;
    }

    private void recursiveCheck(SATFormula satFormula, MutableBitVector bits, int pos) {
        if (pos == bits.getSize()) {
            if (satFormula.isSatisfied(bits)) {
                solutions.add(bits.copy());
                System.err.println(bits.toString());
            }
            return;
        }

        bits.set(pos, false);
        recursiveCheck(satFormula, bits, pos+1);

        bits.set(pos, true);
        recursiveCheck(satFormula, bits, pos+1);
    }
}
