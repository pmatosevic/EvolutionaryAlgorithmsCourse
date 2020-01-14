package hr.fer.zemris.optjava.dz9;

import hr.fer.zemris.optjava.dz9.problem.FirstProblem;
import hr.fer.zemris.optjava.dz9.problem.MOOPProblem;
import hr.fer.zemris.optjava.dz9.problem.SecondProblem;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

public class MOOP {

    private static final double SIGMA = 0.25;

    public static void main(String[] args) throws IOException {
        String problemType = args[0];
        MOOPProblem problem = (problemType.equals("1")) ? new FirstProblem() : new SecondProblem();
        int populationSize = Integer.parseInt(args[1]);
        SolutionSpace space = args[2].equals("decision-space") ? SolutionSpace.DECISION_SPACE : SolutionSpace.OBJECTIVE_SPACE;
        int maxIter = Integer.parseInt(args[3]);

        NSGA nsga = new NSGA(problem, populationSize, SIGMA, space);
        List<Chromosome> population = nsga.run(maxIter);
        List<List<Chromosome>> fronts = NondominatedSort.sort(population);

        for (int i = 0; i < fronts.size(); i++) {
            List<Chromosome> front = fronts.get(i);
            System.out.println("Front" + i + ": " + front.size() + " chromosomes");

            for (Chromosome c : front) {
                System.out.println("\t Solution: " + solutionStr(c) + " Fitness: " + c.fitness + " Objectives: " + objectiveStr(c));
            }

            System.out.println();
        }

        System.out.println("All points:");
        System.out.println("{" + population.stream().map(MOOP::objectiveStr).collect(Collectors.joining(",")) + "}");

        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get("izlaz-dec.txt"))) {
            for (Chromosome c : population) {
                writer.write(solutionStr(c) + "\n");
            }
        }
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get("izlaz-obj.txt"))) {
            for (Chromosome c : population) {
                if (problemType.equals("1")) {
                    writer.write(c.fitness + "\n");
                } else {
                    writer.write(objectiveStr(c) + "\n");
                }
            }
        }
    }

    private static String solutionStr(Chromosome c) {
        return "(" + DoubleStream.of(c.solution)
                .mapToObj(Double::toString)
                .collect(Collectors.joining(",")) + ")";
    }

    private static String objectiveStr(Chromosome c) {
        return "(" + DoubleStream.of(c.objectives)
                .mapToObj(Double::toString)
                .collect(Collectors.joining(",")) + ")";
    }

}
