package hr.fer.zemris.optjava.dz11.part1;

import hr.fer.zemris.art.GrayScaleImage;
import hr.fer.zemris.optjava.dz11.Algorithm;
import hr.fer.zemris.optjava.dz11.Chromosome;
import hr.fer.zemris.optjava.dz11.eval.EvaluatorProviderImpl;
import hr.fer.zemris.optjava.dz11.eval.IEvaluatorProvider;
import hr.fer.zemris.optjava.dz11.crossover.Crossover;
import hr.fer.zemris.optjava.dz11.crossover.TwoFoldCrossover;
import hr.fer.zemris.optjava.dz11.mutation.BasicMutation;
import hr.fer.zemris.optjava.dz11.mutation.Mutation;
import hr.fer.zemris.optjava.dz11.selection.ProportionalSelection;
import hr.fer.zemris.optjava.dz11.selection.Selection;
import hr.fer.zemris.optjava.rng.IRNG;
import hr.fer.zemris.optjava.rng.RNG;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Algorithm1 implements Algorithm {

    public static Chromosome RED_PILL = new Chromosome(null);

    int numSquares;
    int populationSize;
    GrayScaleImage targetImage;
    IEvaluatorProvider evaluatorProvider;

    Mutation mutation;
    Crossover crossover = new TwoFoldCrossover();
    Selection selection = new ProportionalSelection();

    BlockingQueue<Chromosome> outQueue = new LinkedBlockingQueue<>();
    BlockingQueue<Chromosome> inQueue = new LinkedBlockingQueue<>();

    List<EvaluationThread> workers;

    public Algorithm1(int numSquares, int populationSize, GrayScaleImage targetImage) {
        this.numSquares = numSquares;
        this.populationSize = populationSize;
        this.targetImage = targetImage;
        this.evaluatorProvider = new EvaluatorProviderImpl(targetImage);
        this.mutation = new BasicMutation(targetImage.getWidth(), targetImage.getHeight());
    }

    public Chromosome run(int maxIter, double minFitness) {
        initializeWorkers();

        List<Chromosome> population = IntStream.range(0, populationSize)
                .mapToObj(i -> initialize()).collect(Collectors.toList());
        population = evaluate(population);
        Collections.sort(population);

        for (int iter = 0; iter < maxIter; iter++) {
            for (int i = 0; i < populationSize; i++) {
                Chromosome parent1 = selection.select(population);
                Chromosome parent2 = selection.select(population);
                Chromosome child = crossover.crossover(parent1, parent2);
                mutation.mutate(child);

                outQueue.add(child);
            }

            population = waitFromWorkers();
            Collections.sort(population);

            if (Math.abs(population.get(0).fitness) < minFitness) break;
        }

        poisonWorkers();
        return population.get(0);
    }

    private void poisonWorkers() {
        for (int i = 0; i < workers.size(); i++) {
            outQueue.add(RED_PILL);
        }
    }

    private void initializeWorkers() {
        workers = new ArrayList<>();
        int n = Runtime.getRuntime().availableProcessors();
        for (int i = 0; i < n; i++) {
            EvaluationThread thread = new EvaluationThread(outQueue, inQueue, evaluatorProvider);
            thread.start();
            workers.add(thread);
        }
    }


    private Chromosome initialize() {
        IRNG rand = RNG.getRNG();
        return new Chromosome(numSquares, rand, targetImage.getWidth(), targetImage.getHeight());
    }

    private List<Chromosome> evaluate(List<Chromosome> population) {
        for (int i = 0; i < populationSize; i++) {
            outQueue.add(population.get(i));
        }

        return waitFromWorkers();
    }

    private List<Chromosome> waitFromWorkers() {
        List<Chromosome> result = new ArrayList<>();
        while (result.size() < populationSize) {
            try {
                result.add(inQueue.take());
            } catch (InterruptedException ignorable) { }
        }
        return result;
    }

}
