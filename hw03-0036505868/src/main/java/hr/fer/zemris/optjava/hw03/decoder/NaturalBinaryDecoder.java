package hr.fer.zemris.optjava.hw03.decoder;

import hr.fer.zemris.optjava.hw03.solution.BitvectorSolution;

public class NaturalBinaryDecoder extends BitvectorDecoder {

    public NaturalBinaryDecoder(double[] mins, double[] maxs, int[] bits, int n) {
        super(mins, maxs, bits, n);
    }

    public NaturalBinaryDecoder(double min, double max, int bit, int n) {
        super(min, max, bit, n);
    }

    @Override
    public void decode(BitvectorSolution solution, double[] values) {
        int start = 0;
        boolean[] vector = solution.getBits();

        for (int i = 0; i < n; i++) {
            double delta = (maxs[i] - mins[i]) / (Math.pow(2, bits[i]) - 1);
            long currBits = 0;

            int pos = start + bits[i] - 1;
            for (int j = 0; j < bits[i]; j++, pos--) {
                if (vector[pos]) currBits |= 1<<j;
            }

            values[i] = mins[i] + currBits * delta;
            start += bits[i];
        }
    }

}
