package hr.fer.zemris.optjava.dz8;

import hr.fer.zemris.optjava.dz8.ann.*;
import hr.fer.zemris.optjava.dz8.de.DifferentialEvolution;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.Stream;

public class ANNTrainer {

    private static Random rand = new Random();

    private static final double F = 1.5;
    private static final double CR = 0.1;
    private static final int SAMPLES_LIMIT = 600;

    public static void main(String[] args) throws IOException {
        Path file = Paths.get(args[0]);
        String net = args[1];
        int n = Integer.parseInt(args[2]);
        double merr = Double.parseDouble(args[3]);
        int maxiter = Integer.parseInt(args[4]);

        ANN ann;
        String[] parts = net.split("-");
        String type = parts[0];
        String components = parts[1];
        int[] layers = Stream.of(components.split("x")).mapToInt(Integer::parseInt).toArray();
        TransferFunction function = new TangensTransferFunction();
        TransferFunction[] functions = new TransferFunction[layers.length - 1];
        Arrays.fill(functions, function);

        Dataset dataset = Dataset.load(file, layers[0], SAMPLES_LIMIT);
        switch (type) {
            case "tdnn":
                ann = new TimeDelayedNN(layers, functions, dataset);
                break;
            case "elman":
                ann = new ElmanNN(layers, functions, dataset);
                break;
            default:
                throw new IllegalArgumentException("Illegal NN type.");
        }

        Algorithm solver = new DifferentialEvolution(n, F, CR, ann);
        double[] best = solver.run(maxiter, merr);
        double error = ann.calculateError(best);
        System.out.println("Smallest error: " + error);
    }

    public static double[] randomWeights(int n) {
        double[] weights = new double[n];
        for (int i = 0; i < n; i++) {
            weights[i] = 2 * rand.nextDouble() - 1.0;
        }
        return weights;
    }

}
