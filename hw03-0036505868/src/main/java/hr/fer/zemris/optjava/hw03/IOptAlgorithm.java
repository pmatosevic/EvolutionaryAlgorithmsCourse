package hr.fer.zemris.optjava.hw03;

import hr.fer.zemris.optjava.hw03.solution.SingleObjectiveSolution;

public interface IOptAlgorithm<T extends SingleObjectiveSolution> {

    T run();

}
