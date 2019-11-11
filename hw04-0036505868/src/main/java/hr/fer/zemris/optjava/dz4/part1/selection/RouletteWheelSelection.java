package hr.fer.zemris.optjava.dz4.part1.selection;

import hr.fer.zemris.optjava.dz4.part1.Chromosome;

import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Random;

public class RouletteWheelSelection implements Selection {

    private Random rand = new Random();

    public RouletteWheelSelection() {
    }

    @Override
    public Chromosome select(List<Chromosome> chromosomes) {
        DoubleSummaryStatistics st = chromosomes.stream().mapToDouble(Chromosome::getFitness).summaryStatistics();
        double worstFitness = st.getMin();
        double fitnessSum = st.getSum() - worstFitness * chromosomes.size();

        double randNum = rand.nextDouble() * fitnessSum;
        for (int i = 0; i < chromosomes.size(); i++) {
            double currFitness = chromosomes.get(i).getFitness() - worstFitness;
            if (currFitness < randNum) return chromosomes.get(i);
            randNum -= currFitness;
        }
        return chromosomes.get(chromosomes.size() - 1);
    }

}
