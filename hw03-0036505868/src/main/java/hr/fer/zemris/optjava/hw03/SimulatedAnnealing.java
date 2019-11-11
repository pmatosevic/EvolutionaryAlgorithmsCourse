package hr.fer.zemris.optjava.hw03;

import hr.fer.zemris.optjava.hw03.temperature.ITempSchedule;
import hr.fer.zemris.optjava.hw03.decoder.IDecoder;
import hr.fer.zemris.optjava.hw03.function.IFunction;
import hr.fer.zemris.optjava.hw03.neighborhood.INeighborhood;
import hr.fer.zemris.optjava.hw03.solution.SingleObjectiveSolution;

import java.util.Random;

public class SimulatedAnnealing<T extends SingleObjectiveSolution> implements IOptAlgorithm<T> {

    private IDecoder<T> decoder;
    private INeighborhood<T> neighborhood;
    private T startWith;
    private IFunction function;
    private boolean minimize;
    private ITempSchedule tempSchedule;

    private Random rand = new Random();

    public SimulatedAnnealing(IDecoder<T> decoder, INeighborhood<T> neighborhood, T startWith, IFunction function, boolean minimize, ITempSchedule tempSchedule) {
        this.decoder = decoder;
        this.neighborhood = neighborhood;
        this.startWith = startWith;
        this.function = function;
        this.minimize = minimize;
        this.tempSchedule = tempSchedule;
    }

    @Override
    public T run() {
        T bestSolution = startWith;
        evaluateSolution(startWith);

        for (int outer = 0; outer < tempSchedule.getOuterLoopCounter(); outer++) {
            double temp = tempSchedule.getNextTemperature();

            for (int inner = 0; inner < tempSchedule.getInnerLoopCounter(); inner++) {
                T neighbor = neighborhood.randomNeighbor(bestSolution);
                evaluateSolution(neighbor);

                double difference = bestSolution.fitness - neighbor.fitness;
                if (difference <= 0 || rand.nextDouble() < Math.exp(-difference / temp)) {
                    bestSolution = neighbor;
                }

//                System.err.printf("(outer: %d, inner: %d, temp: %f) - best solution: %f%n", outer, inner, temp, bestSolution.fitness);
            }
            System.err.printf("(outer: %d, temp: %f) - best solution: %f%n", outer, temp, bestSolution.fitness);
        }

        return bestSolution;
    }

    private void evaluateSolution(T solution) {
        solution.value = function.valueAt(decoder.decode(solution));
        solution.fitness = minimize ? (-solution.value) : solution.value;
    }
}
