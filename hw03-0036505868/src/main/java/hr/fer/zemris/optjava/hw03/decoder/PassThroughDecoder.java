package hr.fer.zemris.optjava.hw03.decoder;

import hr.fer.zemris.optjava.hw03.solution.DoubleArraySolution;

public class PassThroughDecoder implements IDecoder<DoubleArraySolution> {
    @Override
    public double[] decode(DoubleArraySolution solution) {
        return solution.getValues();
    }

    @Override
    public void decode(DoubleArraySolution solution, double[] values) {
        for (int i = 0; i < values.length; i++) {
            values[i] = solution.getValues()[i];
        }
    }
}
