package hr.fer.zemris.optjava.dz11;

import hr.fer.zemris.art.GrayScaleImage;
import hr.fer.zemris.optjava.dz11.eval.Evaluator;
import hr.fer.zemris.optjava.dz11.part1.Algorithm1;
import hr.fer.zemris.optjava.rng.EVOThread;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Pokretac1 {


    public static void main(String[] args) throws IOException, InterruptedException {
        Path imgPath = Paths.get(args[0]);
        int numSquares = Integer.parseInt(args[1]);
        int populationSize = Integer.parseInt(args[2]);
        int maxIter = Integer.parseInt(args[3]);
        double minFitness = Double.parseDouble(args[4]);
        Path solutionPath = Paths.get(args[5]);
        Path solutionImage = Paths.get(args[6]);

        GrayScaleImage target = GrayScaleImage.load(imgPath.toFile());
        Algorithm1 algorithm = new Algorithm1(numSquares, populationSize, target);

        Chromosome[] solution = new Chromosome[1];
        Thread t = new EVOThread(() -> {
            solution[0] = algorithm.run(maxIter, minFitness);
        });
        t.start();
        t.join();
        System.out.println("Best fitness: " + solution[0].fitness);

        GrayScaleImage rendered = new GrayScaleImage(target.getWidth(), target.getHeight());
        Evaluator.drawSolutionToImage(solution[0], rendered);
        rendered.save(solutionImage.toFile());

        Files.writeString(solutionPath, solution[0].solutionString());
    }


}
