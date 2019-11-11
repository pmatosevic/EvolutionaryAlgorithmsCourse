package hr.fer.zemris.optjava.hw03.neighborhood;

import hr.fer.zemris.optjava.hw03.solution.SingleObjectiveSolution;

public interface INeighborhood<T extends SingleObjectiveSolution> {

    T randomNeighbor(T solution);

}
