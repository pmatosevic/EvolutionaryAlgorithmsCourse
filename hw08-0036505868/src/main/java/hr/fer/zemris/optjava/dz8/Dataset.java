package hr.fer.zemris.optjava.dz8;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.DoubleUnaryOperator;

public class Dataset {

    private List<Example> examples;

    public Dataset(List<Example> examples) {
        this.examples = Collections.unmodifiableList(examples);
    }

    public int examplesCount() {
        return examples.size();
    }

    public Example getExample(int index) {
        return examples.get(index);
    }

    public List<Example> getExamples() {
        return examples;
    }


    public static List<Double> loadFile(Path file) throws IOException {
        List<Double> numbers = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(file)) {
            String line;
            while ((line = reader.readLine()) != null) {
                numbers.add(Double.parseDouble(line.trim()));
            }
        }
        return numbers;
    }


    public static Dataset load(Path file, int group, int limit) throws IOException {
        List<Double> numbers = loadFile(file);
        DoubleSummaryStatistics dss = numbers.stream().mapToDouble(x -> x).summaryStatistics();
        double min = dss.getMin();
        double max = dss.getMax();
        double delta = max - min;
        DoubleUnaryOperator transform = x -> -1 + 2 * (x-min) / delta;

        List<Example> examples = new ArrayList<>();
        if (limit == -1) limit = numbers.size();
        for (int pos = 0; pos + group < limit; pos++) {
            double[] input = new double[group];
            for (int k = 0; k < group; k++) {
                input[k] = transform.applyAsDouble(numbers.get(pos + k));
            }
            double[] output = new double[] {transform.applyAsDouble(numbers.get(pos + group))};
            examples.add(new Example(input, output));
        }
        return new Dataset(examples);
    }

    public static Dataset load(Path file, int limit) throws IOException {
        return load(file, 1, limit);
    }

    private static double[] commaSeparatedToArray(String str) {
        return Arrays.stream(str.trim().split(","))
                .mapToDouble(Double::parseDouble)
                .toArray();
    }
}
