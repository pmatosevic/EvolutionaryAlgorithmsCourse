package hr.fer.zemris.optjava.dz6;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TSPAlgorithm {

    private final int antsNum;
    private final int k;
    private final int maxIter;
    private final int n;

    private City[] cities;
    private double[][] distances;
    private List<Integer>[] closestCities;
    private double[][] heuristicTrail;

    private double ro = 0.02;
    private double alpha = 2;
    private double beta = 3;
    private double a = 10;
    private double maxTrail;

    private double[][] trails;
    private TSPAnt bestSoFar;

    public TSPAlgorithm(List<City> citiesList, int k, int antsNum, int maxIter) {
        this.antsNum = antsNum;
        this.maxIter = maxIter;
        this.k = k;
        this.cities = citiesList.toArray(new City[0]);
        this.n = this.cities.length;
        this.distances = new double[n][n];
        this.heuristicTrail = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                double dx = cities[i].getX() - cities[j].getX();
                double dy = cities[i].getY() - cities[j].getY();
                distances[i][j] = Math.sqrt(dx*dx + dy*dy);
                heuristicTrail[i][j] = Math.pow(1/distances[i][j], beta);
            }
        }

        closestCities = new List[n];
        for (int i = 0; i < n; i++) {
            int currIdx = i;
            closestCities[i] = IntStream.range(0, n)
                    .mapToObj(Integer::valueOf)
                    .sorted(Comparator.comparingDouble(idx -> distance(currIdx, idx)))
                    .limit(k)
                    .collect(Collectors.toList());
        }

        trails = new double[n][n];
        initTrails();
    }

    private void initTrails() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                trails[i][j] = maxTrail;
            }
        }
    }

    public void run() {
        int stagnationCnt = 0;
        int maxStagnationCnt = maxIter / 10;
        for (int iter = 0; iter < maxIter; iter++) {
            List<TSPAnt> ants = new ArrayList<>();
            for (int antIndex = 0; antIndex < antsNum; antIndex++) {
                TSPAnt ant = new TSPAnt(this);
                ant.doWalk();
                ants.add(ant);
            }
            Collections.sort(ants);
            TSPAnt iterationBest = ants.get(0);
            if (bestSoFar == null || iterationBest.compareTo(bestSoFar) <= 0) {
                bestSoFar = iterationBest;
            } else {
                stagnationCnt++;
            }

            if (stagnationCnt == maxStagnationCnt) {
                initTrails();
                continue;
            }

            TSPAnt bestForUpdate = (iter < maxIter/2) ? iterationBest : bestSoFar;

            maxTrail = 1 / (ro * bestForUpdate.getTotalDistance());
            evaporateTrails(maxTrail / a);
            updateTrails(bestForUpdate);
        }
    }

    public TSPAnt getBestSoFar() {
        return bestSoFar;
    }

    private void updateTrails(TSPAnt ant) {
        double delta = 1.0 / ant.getTotalDistance();
        for (int idx = 0; idx < ant.getCityOrder().size() - 1; idx++) {
            int current = ant.getCityOrder().get(idx);
            int next = ant.getCityOrder().get(idx + 1);
            trails[current][next] = Math.min(maxTrail, trails[current][next] + delta);
            trails[next][current] = trails[current][next];
        }
    }

    private void evaporateTrails(double minTrail) {
        for (int i=0; i<n; i++) {
            for (int j=i+1; j<n; j++) {
                trails[i][j] = Math.max(trails[i][j] * (1-ro), minTrail);
                trails[j][i] = trails[i][j];
            }
        }
    }

    public double distance(int i, int j) {
        return distances[i][j];
    }

    public int getN() {
        return n;
    }

    public List<Integer>[] getClosestCities() {
        return closestCities;
    }

    public double getHeuristicTrail(int i, int j) {
        int min = Math.min(i, j);
        int max = Math.max(i, j);
        return heuristicTrail[min][max];
    }

    public double getAlpha() {
        return alpha;
    }

    public double getBeta() {
        return beta;
    }

    public double getTrail(int i, int j) {
        int min = Math.min(i, j);
        int max = Math.max(i, j);
        return trails[min][max];
    }

}
