package hr.fer.zemris.optjava.dz11;

import hr.fer.zemris.art.GrayScaleImage;
import hr.fer.zemris.generic.ga.GASolution;
import hr.fer.zemris.optjava.rng.IRNG;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Chromosome extends GASolution<int[]> {

    public Chromosome() {
    }

    public Chromosome(int[] data) {
        this.data = data;
    }

    public Chromosome(int numSquares, IRNG rand, int width, int height) {
        this.data = new int[5*numSquares + 1];
        data[0] = rand.nextInt(0, GrayScaleImage.NUM_COLORS);
        for (int i = 0; i < numSquares; i++) {
            data[5*i + 1] = rand.nextInt(0, width);
            data[5*i + 2] = rand.nextInt(0, height);
            data[5*i + 3] = rand.nextInt(0, width / 2);
            data[5*i + 4] = rand.nextInt(0, height / 2);
            data[5*i + 5] = rand.nextInt(0, GrayScaleImage.NUM_COLORS);
        }
    }

    @Override
    public Chromosome duplicate() {
        Chromosome chromosome = new Chromosome();
        chromosome.fitness = this.fitness;
        chromosome.data = Arrays.copyOf(this.data, this.data.length);
        return chromosome;
    }

    public String solutionString() {
        return Arrays.stream(data).mapToObj(String::valueOf).collect(Collectors.joining("\n"));
    }

}
