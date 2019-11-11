package hr.fer.zemris.optjava.hw03;

import hr.fer.zemris.optjava.hw03.decoder.GreyBinaryDecoder;
import hr.fer.zemris.optjava.hw03.decoder.IDecoder;
import hr.fer.zemris.optjava.hw03.decoder.PassThroughDecoder;
import hr.fer.zemris.optjava.hw03.function.IFunction;
import hr.fer.zemris.optjava.hw03.function.TransferFunction;
import hr.fer.zemris.optjava.hw03.neighborhood.BitvectorNeighborhood;
import hr.fer.zemris.optjava.hw03.neighborhood.DoubleArrayNormNeighborHood;
import hr.fer.zemris.optjava.hw03.neighborhood.DoubleArrayUnifNeighborHood;
import hr.fer.zemris.optjava.hw03.solution.BitvectorSolution;
import hr.fer.zemris.optjava.hw03.solution.DoubleArraySolution;
import hr.fer.zemris.optjava.hw03.temperature.GeometricTempSchedule;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class RegresijaSustava {

    private static final int NUM_PARAMS = 6;

    private static final double[] INITIAL_MIN = {-5, -5, -5, -5, -5, -5};
    private static final double[] INITIAL_MAX = {5, 5, 5, 5, 5, 5};
    private static final double[] DELTAS = {0.2, 0.2, 0.2, 0.2, 0.2, 0.2};

    private static final double BITVECTOR_MIN = -10;
    private static final double BITVECTOR_MAX = 10;
    private static final double BIT_MUTATION_PROBABILITY = 0.1;

    private static final double TEMP_INITIAL = 300;
    private static final double TEMP_ALPHA = 0.985;
    private static final int TEMP_INNER_LIMIT = 1500;
    private static final int TEMP_OUTER_LIMIT = 2000;

    private static Random rand = new Random();

    public static void main(String[] args) throws IOException {
        Path file = Paths.get(args[0]).toRealPath();
        String option = args[1];

        TransferFunction function = loadFunction(file);
        double[] arraySolution;
        double fitness;

        if (option.equals("decimal")) {
            DoubleArraySolution solution = doubleArraySolve(function);
            arraySolution = solution.getValues();
            fitness = solution.fitness;
        } else if (option.startsWith("binary:")) {
            int bitPerVariable = Integer.parseInt(option.split(":")[1]);

            IDecoder<BitvectorSolution> decoder = new GreyBinaryDecoder(BITVECTOR_MIN, BITVECTOR_MAX, bitPerVariable, NUM_PARAMS);

            BitvectorSolution solution = bitvectorSolve(function, bitPerVariable, decoder);
            arraySolution = decoder.decode(solution);
            fitness = solution.fitness;
        } else if (option.equals("greedy")) {
            DoubleArraySolution solution = greedySolve(function);
            arraySolution = solution.getValues();
            fitness = solution.fitness;
        } else {
            throw new IllegalArgumentException("Illegal option.");
        }

        System.out.printf("Solution: a=%f, b=%f, c=%f, d=%f, e=%f, f=%f%n",
                arraySolution[0], arraySolution[1], arraySolution[2],
                arraySolution[3], arraySolution[4], arraySolution[5]
        );
        System.out.println("Fitness: " + fitness);
    }


    private static DoubleArraySolution greedySolve(TransferFunction function) {
        DoubleArraySolution startWith = new DoubleArraySolution(NUM_PARAMS);
        startWith.randomize(rand, INITIAL_MIN, INITIAL_MAX);

        IOptAlgorithm<DoubleArraySolution> algo = new GreedyAlgorithm<>(
                new PassThroughDecoder(),
                new DoubleArrayUnifNeighborHood(DELTAS),
                startWith,
                function,
                true
        );
        return algo.run();
    }

    private static DoubleArraySolution doubleArraySolve(TransferFunction function) {
        DoubleArraySolution startWith = new DoubleArraySolution(NUM_PARAMS);
        startWith.randomize(rand, INITIAL_MIN, INITIAL_MAX);

        IOptAlgorithm<DoubleArraySolution> algo = new SimulatedAnnealing<>(
                new PassThroughDecoder(),
                new DoubleArrayNormNeighborHood(DELTAS),
                startWith,
                function,
                true,
                new GeometricTempSchedule(TEMP_ALPHA, TEMP_INITIAL, TEMP_INNER_LIMIT, TEMP_OUTER_LIMIT)
        );
        return algo.run();
    }

    private static BitvectorSolution bitvectorSolve(IFunction function, int bitPerVariable, IDecoder<BitvectorSolution> decoder) {
        BitvectorSolution startWith = new BitvectorSolution(NUM_PARAMS * bitPerVariable);
        startWith.randomize(rand);

        IOptAlgorithm<BitvectorSolution> algo = new SimulatedAnnealing<>(
                decoder,
                new BitvectorNeighborhood(BIT_MUTATION_PROBABILITY),
                startWith,
                function,
                true,
                new GeometricTempSchedule(TEMP_ALPHA, TEMP_INITIAL, TEMP_INNER_LIMIT, TEMP_OUTER_LIMIT)
        );
        return algo.run();
    }



    private static TransferFunction loadFunction(Path path) throws IOException {
        List<List<Double>> numbers = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            while (true) {
                String line = reader.readLine();
                if (line == null) break;
                if (line.startsWith("#")) continue;

                String[] parts = line
                        .replace("[", "")
                        .replace("]", "")
                        .trim().split("\\s*,\\s*");
                List<Double> lineNums = Arrays.stream(parts).map(Double::parseDouble).collect(Collectors.toList());
                numbers.add(lineNums);
            }
        }

        int samples = numbers.size();
        double[][] input = new double[numbers.size()][];
        double[] output = new double[numbers.size()];
        for (int i = 0; i < numbers.size(); i++) {
            List<Double> numLine = numbers.get(i);
            input[i] = new double[TransferFunction.NUM_X];
            for (int j = 0; j < input[i].length; j++) input[i][j] = numLine.get(j);
            output[i] = numLine.get(numLine.size() - 1);
        }

        return new TransferFunction(input, output);
    }

}
