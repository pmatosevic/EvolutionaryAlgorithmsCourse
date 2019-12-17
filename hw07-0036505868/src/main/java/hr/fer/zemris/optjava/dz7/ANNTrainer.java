package hr.fer.zemris.optjava.dz7;

import hr.fer.zemris.optjava.dz7.ann.FFANN;
import hr.fer.zemris.optjava.dz7.ann.SigmoidTransferFunction;
import hr.fer.zemris.optjava.dz7.ann.TransferFunction;
import hr.fer.zemris.optjava.dz7.clonalg.ClonAlg;
import hr.fer.zemris.optjava.dz7.pso.GlobalPSO;
import hr.fer.zemris.optjava.dz7.pso.LocalPSO;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Random;

public class ANNTrainer {

    private static Random rand = new Random();
    private static final int[] LAYERS = {4, 5, 3, 3};
    private static final TransferFunction[] TRANSFER_FUNCTIONS =
            {new SigmoidTransferFunction(), new SigmoidTransferFunction(), new SigmoidTransferFunction()};

    private static final double C1 = 2.0;
    private static final double C2 = 2.0;
    private static final double BETA = 2;
    private static final double MUTATION_CONSTANT = 1.0;

    public static void main(String[] args) throws IOException {
        Path file = Paths.get(args[0]);
        String algorithm = args[1];
        int n = Integer.parseInt(args[2]);
        double merr = Double.parseDouble(args[3]);
        int maxiter = Integer.parseInt(args[4]);

        Dataset dataset = Dataset.load(file);
        FFANN ffann = new FFANN(LAYERS, TRANSFER_FUNCTIONS, dataset);

        Algorithm solver;
        if (algorithm.startsWith("pso-a")) {
            solver = new GlobalPSO(n, ffann, C1, C2);
        } else if (algorithm.startsWith("pso-b-")) {
            int neighbors = Integer.parseInt(algorithm.substring(algorithm.lastIndexOf('-')));
            solver = new LocalPSO(n, ffann, C1, C2, neighbors);
        } else if (algorithm.startsWith("clonalg")) {
            solver = new ClonAlg(n, BETA,n/10, MUTATION_CONSTANT, ffann);
        } else {
            throw new IllegalArgumentException("Algorithm not supported.");
        }

        double[] weights = solver.run(maxiter, merr);
        int correct = 0;
        for (Dataset.SingleExample example : dataset.getExamples()) {
            double[] actual = ffann.calcClassification(example.input, weights);
            double[] expected = example.output;
            if (Arrays.equals(actual, expected)) {
                correct++;
            }
            System.out.printf("Input: %s Expected: %s Output: %s%n",
                    Arrays.toString(example.input), Arrays.toString(example.output), Arrays.toString(actual));
        }

        System.out.println("Correct classification: " + correct + "/" + dataset.examplesCount());
    }

    public static double[] randomWeights(int n) {
        double[] weights = new double[n];
        for (int i = 0; i < n; i++) {
            weights[i] = 2 * rand.nextDouble() - 1.0;
        }
        return weights;
    }

}
