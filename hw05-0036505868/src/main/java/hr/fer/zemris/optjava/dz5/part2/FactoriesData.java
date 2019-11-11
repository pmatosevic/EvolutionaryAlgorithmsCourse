package hr.fer.zemris.optjava.dz5.part2;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Scanner;

public class FactoriesData {

    private int n;
    private int[][] dist;
    private int[][] transport;

    public FactoriesData(int n, int[][] dist, int[][] transport) {
        this.n = n;
        this.dist = dist;
        this.transport = transport;
    }

    public int getN() {
        return n;
    }

    public int[][] getDist() {
        return dist;
    }

    public int[][] getTransport() {
        return transport;
    }

    public long permutationCost(int[] permutation) {
        long sum = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                sum += transport[i][j] * dist[permutation[i]][permutation[j]];
            }
        }
        return sum;
    }

    public static FactoriesData loadFromFile(Path file) throws IOException {
        try (Scanner sc = new Scanner(file, StandardCharsets.UTF_8)) {
            int n = sc.nextInt();
            int[][] dist = new int[n][n];
            int[][] transport = new int[n][n];

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    dist[i][j] = sc.nextInt();
                }
            }

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    transport[i][j] = sc.nextInt();
                }
            }

            return new FactoriesData(n, dist, transport);
        }
    }
}
