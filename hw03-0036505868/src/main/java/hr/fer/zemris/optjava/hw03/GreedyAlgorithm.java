package hr.fer.zemris.optjava.hw03;

import hr.fer.zemris.optjava.hw03.decoder.IDecoder;
import hr.fer.zemris.optjava.hw03.function.IFunction;
import hr.fer.zemris.optjava.hw03.neighborhood.INeighborhood;
import hr.fer.zemris.optjava.hw03.solution.SingleObjectiveSolution;

public class GreedyAlgorithm<T extends SingleObjectiveSolution> implements IOptAlgorithm<T> {

    private static final int MAX_ITERATIONS = 1000000;

    private IDecoder<T> decoder;
    private INeighborhood<T> neighborhood;
    private T startWith;
    private IFunction function;
    private boolean minimize;

    public GreedyAlgorithm(IDecoder<T> decoder, INeighborhood<T> neighborhood, T startWith, IFunction function, boolean minimize) {
        this.decoder = decoder;
        this.neighborhood = neighborhood;
        this.startWith = startWith;
        this.function = function;
        this.minimize = minimize;
    }

    @Override
    public T run() {
        T bestSolution = startWith;
        evaluateSolution(startWith);

        for (int iter = 0; iter < MAX_ITERATIONS; iter++) {
            T neighbor = neighborhood.randomNeighbor(bestSolution);
            evaluateSolution(neighbor);

            double difference = bestSolution.fitness - neighbor.fitness;
            if (difference <= 0) {
                bestSolution = neighbor;
            }

            System.err.println(iter + ": " + bestSolution.fitness);
        }

        return bestSolution;
    }

    private void evaluateSolution(T solution) {
        solution.value = function.valueAt(decoder.decode(solution));
        solution.fitness = minimize ? (-solution.value) : solution.value;
    }
}
