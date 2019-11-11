package hr.fer.zemris.optjava.hw03.decoder;

import hr.fer.zemris.optjava.hw03.solution.SingleObjectiveSolution;

public interface IDecoder<T extends SingleObjectiveSolution> {

    double[] decode(T solution);

    void decode(T solution, double[] values);

}
