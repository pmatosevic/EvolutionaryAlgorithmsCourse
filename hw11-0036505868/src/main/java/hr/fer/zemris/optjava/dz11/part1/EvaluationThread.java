package hr.fer.zemris.optjava.dz11.part1;

import hr.fer.zemris.optjava.dz11.Chromosome;
import hr.fer.zemris.optjava.dz11.eval.Evaluator;
import hr.fer.zemris.optjava.dz11.eval.IEvaluatorProvider;
import hr.fer.zemris.optjava.rng.EVOThread;

import java.util.concurrent.BlockingQueue;

public class EvaluationThread extends EVOThread {

    BlockingQueue<Chromosome> inQueue;
    BlockingQueue<Chromosome> outQueue;
    IEvaluatorProvider evaluatorProvider;

    public EvaluationThread(BlockingQueue<Chromosome> inQueue, BlockingQueue<Chromosome> outQueue,
                            IEvaluatorProvider evaluatorProvider) {
        this.inQueue = inQueue;
        this.outQueue = outQueue;
        this.evaluatorProvider = evaluatorProvider;
    }

    @Override
    public void run() {
        Evaluator evaluator = evaluatorProvider.getEvaluator();
        while (true) {
            Chromosome c = null;
            boolean taken = false;
            while (!taken) {
                try {
                    c = inQueue.take();
                    taken = true;
                } catch (InterruptedException ignorable) { }
            }
            if (c == Algorithm1.RED_PILL) return;

            evaluator.evaluate(c);
            outQueue.add(c);
        }
    }
}
