package hr.fer.zemris.optjava.dz7.pso;

import hr.fer.zemris.optjava.dz7.ann.FFANN;

public class LocalPSO extends GlobalPSO {

    private int d;

    public LocalPSO(int populationSize, FFANN ffann, double c1, double c2, int d) {
        super(populationSize, ffann, c1, c2);
        this.d = d;
    }

    @Override
    protected double[] findNeighborhoodBest(int pos) {
        double bestF = pbestF[pos];
        double[] best = pbest[pos];
        for (int i = 1; i <= d; i++) {
            int currPosRight = (pos+i) % populationSize;
            int currPosLeft = (pos - i + populationSize) % populationSize;
            if (pbestF[currPosRight] < bestF) {
                bestF = pbestF[currPosRight];
                best = pbest[currPosRight];
            }
            if (pbestF[currPosLeft] < bestF) {
                bestF = pbestF[currPosLeft];
                best = pbest[currPosLeft];
            }
        }
        return best;
    }
}
