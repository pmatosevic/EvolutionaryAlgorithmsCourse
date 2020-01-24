package hr.fer.zemris.optjava.dz11.part2;

import hr.fer.zemris.optjava.dz11.Chromosome;
import hr.fer.zemris.optjava.dz11.eval.Evaluator;
import hr.fer.zemris.optjava.dz11.eval.IEvaluatorProvider;
import hr.fer.zemris.optjava.dz11.crossover.Crossover;
import hr.fer.zemris.optjava.dz11.mutation.Mutation;
import hr.fer.zemris.optjava.dz11.selection.Selection;
import hr.fer.zemris.optjava.rng.EVOThread;

import java.util.concurrent.BlockingQueue;

public class WorkerThread extends EVOThread {

    private Selection selection;
    private Mutation mutation;
    private Crossover crossover;
    private IEvaluatorProvider evaluatorProvider;

    private BlockingQueue<WorkerJob> inQueue;
    private BlockingQueue<Chromosome[]> outQueue;

    public WorkerThread(Selection selection, Mutation mutation, Crossover crossover, IEvaluatorProvider evaluatorProvider,
                        BlockingQueue<WorkerJob> inQueue, BlockingQueue<Chromosome[]> outQueue) {
        this.selection = selection;
        this.mutation = mutation;
        this.crossover = crossover;
        this.evaluatorProvider = evaluatorProvider;
        this.inQueue = inQueue;
        this.outQueue = outQueue;
    }

    @Override
    public void run() {
        Evaluator evaluator = evaluatorProvider.getEvaluator();

        while (true) {
            WorkerJob job = null;
            boolean taken = false;
            while (!taken) {
                try {
                    job = inQueue.take();
                    taken = true;
                } catch (InterruptedException ignorable) { }
            }
            if (job == Algorithm2.RED_PILL) return;

            int n = job.getNumChildren();
            Chromosome[] result = new Chromosome[n];
            for (int i = 0; i < n; i++) {
                Chromosome parent1 = selection.select(job.getPopulation());
                Chromosome parent2 = selection.select(job.getPopulation());
                Chromosome child = crossover.crossover(parent1, parent2);
                mutation.mutate(child);
                evaluator.evaluate(child);

                result[i] = child;
            }

            outQueue.add(result);
        }
    }
}
