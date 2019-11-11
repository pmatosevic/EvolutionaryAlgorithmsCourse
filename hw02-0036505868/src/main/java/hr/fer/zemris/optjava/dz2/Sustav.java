package hr.fer.zemris.optjava.dz2;

import Jama.Matrix;
import hr.fer.zemris.optjava.dz2.function.SystemFunction;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Sustav {

    public static void main(String[] args) throws IOException {
        String option = args[0];
        int maxIterations = Integer.parseInt(args[1]);
        Path file = Paths.get(args[2]);

        SystemFunction function = readSystem(file);
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
            System.out.println("x" + (i+1) + ": " + solution.get(i, 0));
        }

        Matrix expected = function.getResult();
        Matrix actual = function.getCoefficients().times(solution);
        Matrix difference = actual.minus(expected);
        System.out.println();
        System.out.println("Errors:");
        difference.print(8, 8);
    }

    private static SystemFunction readSystem(Path path) throws IOException {
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

        int n = numbers.size();
        Matrix coefficients = new Matrix(n, n);
        Matrix result = new Matrix(n, 1);
        for (int i = 0; i < numbers.size(); i++) {
            List<Double> numLine = numbers.get(i);
            for (int j = 0; j < n; j++) coefficients.set(i, j, numLine.get(j));
            result.set(i, 0, numLine.get(n));
        }

        return new SystemFunction(coefficients, result);
    }

}
