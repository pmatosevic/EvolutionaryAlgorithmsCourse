package hr.fer.zemris.optjava.dz2;

import Jama.Matrix;
import hr.fer.zemris.optjava.dz2.function.TransferFunction;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Prijenosna {

    public static void main(String[] args) throws IOException {
        String option = args[0];
        int maxIterations = Integer.parseInt(args[1]);
        Path file = Paths.get(args[2]);

        TransferFunction function = loadFunction(file);
        Matrix initialPoint = NumOptAlgorithms.randomInitialize(function.getNumberOfVariables());

        Matrix solution;
        if (option.equals("grad")) {
            solution = NumOptAlgorithms.gradientDescent(function, maxIterations, initialPoint, null);
        }
        else if (option.equals("newton")) {
            solution = NumOptAlgorithms.newtonMethod(function, maxIterations, initialPoint, null);
        }
        else {
            throw new IllegalArgumentException("Invalid algorithm.");
        }

        System.out.println("Result of numeric optimisation:");
        for (int i = 0; i < solution.getRowDimension(); i++) {
            char c = (char) ('a' + i);
            System.out.println(c + ": " + solution.get(i, 0));
        }

        System.out.println();
        System.out.println("Error per sample:");
        double[] error = function.calculateDifference(solution);
        for (double e : error) {
            System.out.println(e);
        }
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
