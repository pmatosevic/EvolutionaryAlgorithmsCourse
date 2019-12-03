package hr.fer.zemris.optjava.dz6;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TSPSolver {

    private static Path file;
    private static int k;
    private static int antsNum;
    private static int maxIter;

    public static void main(String[] args) throws IOException {
        file = Paths.get(args[0]);
        k = Integer.parseInt(args[1]);
        antsNum = Integer.parseInt(args[2]);
        maxIter = Integer.parseInt(args[3]);

        TSPAlgorithm tspAlgorithm = new TSPAlgorithm(load(file), k, antsNum, maxIter);
        tspAlgorithm.run();
        TSPAnt best = tspAlgorithm.getBestSoFar();
        System.out.println("Best distance: " + best.getTotalDistance());
        System.out.println("Path: " + best.getCityOrder().stream().map(idx -> String.valueOf(idx+1)).collect(Collectors.joining("->")));
    }

    public static List<City> load(Path file) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(file)) {
            List<City> cities = new ArrayList<>();
            while (true) {
                String line = reader.readLine();
                if (line == null) break;
                String[] parts = line.trim().split("\\s+");
                if (parts.length != 3) continue;
                try {
                    double x = Double.parseDouble(parts[1].trim());
                    double y = Double.parseDouble(parts[2].trim());
                    cities.add(new City(x, y));
                } catch (IllegalArgumentException ignorable) { }
            }
            return cities;
        }
    }

}
