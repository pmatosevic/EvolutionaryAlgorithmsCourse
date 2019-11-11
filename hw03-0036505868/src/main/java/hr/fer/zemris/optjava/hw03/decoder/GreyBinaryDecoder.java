package hr.fer.zemris.optjava.hw03.decoder;

import hr.fer.zemris.optjava.hw03.solution.BitvectorSolution;

public class GreyBinaryDecoder extends BitvectorDecoder {

    public GreyBinaryDecoder(double[] mins, double[] maxs, int[] bits, int n) {
        super(mins, maxs, bits, n);
    }

    public GreyBinaryDecoder(double min, double max, int bit, int n) {
        super(min, max, bit, n);
    }

    @Override
    public void decode(BitvectorSolution solution, double[] values) {
        int start = 0;
        boolean[] vector = solution.getBits();

        for (int i = 0; i < n; i++) {
            double delta = (maxs[i] - mins[i]) / (Math.pow(2, bits[i]) - 1);
            long grayBits = 0;

            int pos = start + bits[i] - 1;
            for (int j = 0; j < bits[i]; j++, pos--) {
                if (vector[pos]) grayBits |= 1<<j;
            }

            long currBits = grayToNatural(grayBits);
            values[i] = mins[i] + currBits * delta;
            start += bits[i];
        }
    }

    private long grayToNatural(long num) {
        long mask = num >> 1;
        while (mask != 0)
        {
            num = num ^ mask;
            mask = mask >> 1;
        }
        return num;
    }

}
