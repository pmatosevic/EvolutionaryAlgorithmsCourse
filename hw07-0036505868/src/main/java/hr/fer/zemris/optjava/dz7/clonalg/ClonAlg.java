package hr.fer.zemris.optjava.dz7.clonalg;

import hr.fer.zemris.optjava.dz7.ANNTrainer;
import hr.fer.zemris.optjava.dz7.Algorithm;
import hr.fer.zemris.optjava.dz7.ann.FFANN;

import java.util.*;
import java.util.stream.Collectors;

public class ClonAlg implements Algorithm {

    private Random rand = new Random();

    private int populationSize;
    private double beta;
    private int d;
    private double mutationConstant;
    private FFANN ffann;

    public ClonAlg(int populationSize, double beta, int d, double mutationConstant, FFANN ffann) {
        this.populationSize = populationSize;
        this.beta = beta;
        this.d = d;
        this.mutationConstant = mutationConstant;
        this.ffann = ffann;
    }

    public double[] run(int maxIter, double minError) {
        List<Antibody> population = initializePopulation(populationSize);
        for (int i = 0; i < maxIter; i++) {
            evaluate(population);
            population.sort(ANTIBODY_COMPARATOR);
            if (population.get(0).getError() <= minError) break;

            List<Antibody> clones = clonePopulation(population);
            hyperMutate(clones);

            evaluate(clones);
            clones.sort(ANTIBODY_COMPARATOR);
            List<Antibody> newPopulation = clones.stream().limit(populationSize - d).collect(Collectors.toList());
            newPopulation.addAll(initializePopulation(d));

            population = newPopulation;
        }
        evaluate(population);
        population.sort(ANTIBODY_COMPARATOR);
        return population.get(0).getWeights();
    }

    private List<Antibody> clonePopulation(List<Antibody> population) {
        List<Antibody> clones = new ArrayList<>();
        for (int i = 0; i < population.size(); i++) {
            int count = (int) (beta * population.size() / (i+1));
            for (int k = 0; k < count; k++) clones.add(population.get(i).clone());
        }
        return clones;
    }

    private void evaluate(List<Antibody> population) {
        population.forEach(ab -> ab.setError(ffann.calculateError(ab.getWeights())));
    }

    private List<Antibody> initializePopulation(int size) {
        List<Antibody> population = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            population.add(new Antibody(ANNTrainer.randomWeights(ffann.getWeightsCount())));
        }
        return population;
    }

    public void hyperMutate(List<Antibody> antibodies) {
        DoubleSummaryStatistics stat = antibodies.stream().mapToDouble(Antibody::getError).summaryStatistics();

        for (Antibody antibody : antibodies) {
            double[] weights = antibody.getWeights();
//            double normalized = 1.0 / (antibody.getScore() - stat.getMin());
//            double intensity = Math.exp(-ro * normalized);
            double intensity = (antibody.getError() - stat.getMin()) * ffann.getWeightsCount() * mutationConstant;
            for (int i = 0; i < weights.length; i++) {
                weights[i] += rand.nextGaussian() * intensity;
            }
        }
    }

    private static Comparator<Antibody> ANTIBODY_COMPARATOR = Comparator.comparingDouble(Antibody::getError);

}
