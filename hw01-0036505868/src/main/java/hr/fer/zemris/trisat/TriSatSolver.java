package hr.fer.zemris.trisat;

import hr.fer.zemris.trisat.algorithm.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class TriSatSolver {

    private static final List<SolvingAlgorithm> algorithms = List.of(
            new BruteForceAlgorithm(),
            new HillClimbingAlgorithm(),
            new HillClimbingWithStatsAlgorithm(),
            new MultistartLocalSearchAlgorithm(),
            new RandomWalkSATAlgorithm(),
            new IteratedLocalSearchAlgorithm()
    );

    public static void main(String[] args) throws IOException {
        int algoIndex = Integer.parseInt(args[0]) - 1;
        String filename = args[1];
        TriSatParser parser = new TriSatParser(Files.newBufferedReader(Paths.get(filename)));

        SolvingAlgorithm algorithm = algorithms.get(algoIndex);
        BitVector assignment = algorithm.solve(parser.getSATFormula());

        if (assignment == null) {
            System.out.println("Algorithm finished without any found satisfiable assignment.");
        } else {
            System.out.println("Found satisfiable assignment: " + assignment);
        }
    }
}
