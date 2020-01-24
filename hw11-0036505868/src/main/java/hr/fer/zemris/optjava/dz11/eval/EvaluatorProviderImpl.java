package hr.fer.zemris.optjava.dz11.eval;

import hr.fer.zemris.art.GrayScaleImage;

public class EvaluatorProviderImpl implements IEvaluatorProvider {

    private final GrayScaleImage template;

    private ThreadLocal<Evaluator> threadLocal = new ThreadLocal<>();

    public EvaluatorProviderImpl(GrayScaleImage template) {
        this.template = template;
    }

    @Override
    public Evaluator getEvaluator() {
        if (threadLocal.get() == null) {
            threadLocal.set(new Evaluator(template));
        }
        return threadLocal.get();
    }
}
