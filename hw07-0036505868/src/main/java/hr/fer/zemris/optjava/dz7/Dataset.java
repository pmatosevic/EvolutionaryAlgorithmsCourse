package hr.fer.zemris.optjava.dz7;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Dataset {

    private List<SingleExample> examples;

    public Dataset(List<SingleExample> examples) {
        this.examples = Collections.unmodifiableList(examples);
    }

    public int examplesCount() {
        return examples.size();
    }

    public SingleExample getExample(int index) {
        return examples.get(index);
    }

    public List<SingleExample> getExamples() {
        return examples;
    }

    public static class SingleExample {
        public final double[] input;
        public final double[] output;

        public SingleExample(double[] input, double[] output) {
            this.input = input;
            this.output = output;
        }
    }

    public static Dataset load(Path file) throws IOException {
        List<SingleExample> examples = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(file)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.replaceAll("\\(", "").replaceAll("\\)", "").split(":");
                SingleExample example = new SingleExample(
                        commaSeparatedToArray(parts[0]),
                        commaSeparatedToArray(parts[1])
                );
                examples.add(example);
            }
        }
        return new Dataset(examples);
    }

    private static double[] commaSeparatedToArray(String str) {
        return Arrays.stream(str.trim().split(","))
                .mapToDouble(Double::parseDouble)
                .toArray();
    }
}
