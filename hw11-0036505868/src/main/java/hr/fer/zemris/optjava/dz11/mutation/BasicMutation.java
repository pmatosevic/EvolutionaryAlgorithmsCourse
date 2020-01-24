package hr.fer.zemris.optjava.dz11.mutation;

import hr.fer.zemris.optjava.dz11.Chromosome;
import hr.fer.zemris.optjava.rng.IRNG;
import hr.fer.zemris.optjava.rng.RNG;

public class BasicMutation implements Mutation {

    private static final int NUM_COLORS = 256;
    private double sigmaColor = 12;
    private double mutationProbability = 0.01;
    private double sigmaSize = 10;

    private int width;
    private int height;

    public BasicMutation(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public void mutate(Chromosome c) {
        IRNG rand = RNG.getRNG();
        int[] data = c.getData();

        data[0] += data[0] + rand.nextGaussian() * sigmaColor;
        data[0] %= NUM_COLORS;
        int n = (data.length - 1)/5;
        for (int i = 0; i < n; i++) {
            boolean changed = false;
            if (rand.nextDouble() < mutationProbability) {
                data[5*i + 1] += rand.nextGaussian() * sigmaSize;
                data[5*i + 2] += rand.nextGaussian() * sigmaSize;
                changed = true;
            }
            if (rand.nextDouble() < mutationProbability) {
                data[5*i + 3] += rand.nextGaussian() * sigmaSize;
                data[5*i + 4] += rand.nextGaussian() * sigmaSize;
                changed = true;
            }
            if (changed) fixSquare(data, 5*i+1);
            if (data[5*i+3] == 0 && data[5*i+4] == 0) {             // gives pressure so that all squares are used and slightly moves them to top left corner
                int rand1 = rand.nextInt(0, (int) sigmaSize);
                int rand2 = rand.nextInt(0, (int) sigmaSize);
                data[5*i + 1] -= rand1;
                data[5*i + 3] += rand1;
                data[5*i + 2] -= rand2;
                data[5*i + 4] += rand2;
                fixSquare(data, 5*i+1);
            }
            if (rand.nextDouble() < mutationProbability) {
                data[5*i + 5] += rand.nextGaussian() * sigmaColor;
                data[5*i + 5] %= 256;
            }
        }
    }

    private void fixSquare(int[] data, int index) {
        data[index] = limit(data[index], 0, width);
        data[index+1] = limit(data[index+1], 0, height);
        data[index+2] = limit(data[index+2], 0, width - data[index]);
        data[index+3] = limit(data[index+3], 0, height - data[index+1]);
    }

    private int limit(int num, int min, int max) {
        if (num < min) return min;
        if (num > max) return max;
        return num;
    }

}
