package hr.fer.zemris.optjava.dz6;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TSPAnt implements Comparable<TSPAnt> {

    private static Random rand = new Random();

    private final TSPAlgorithm tsp;
    private List<Integer> cityOrder = new ArrayList<>();
    private double totalDistance;

    public TSPAnt(TSPAlgorithm tsp) {
        this.tsp = tsp;
    }

    public List<Integer> getCityOrder() {
        return cityOrder;
    }

    public double getTotalDistance() {
        return totalDistance;
    }

    public void doWalk() {
        Set<Integer> visited = new HashSet<>();

        int current = rand.nextInt(tsp.getN());
        cityOrder.add(current);
        visited.add(current);

        for (int i=0; i<tsp.getN()-1; i++) {
            List<Integer> possibleNext = tsp.getClosestCities()[current];

            double probSum = 0;
            for (int next : possibleNext) {
                probSum += Math.pow(tsp.getTrail(current, next), tsp.getAlpha()) * tsp.getHeuristicTrail(current, next);
            }

            double randVal = rand.nextDouble() * probSum;
            int selected = -1;
            for (int j = 0; j < possibleNext.size(); j++) {
                int next = possibleNext.get(j);
                if (visited.contains(next)) continue;
                double currVal = Math.pow(tsp.getTrail(current, next), tsp.getAlpha()) * tsp.getHeuristicTrail(current, next);
                if (randVal <= currVal) {
                    selected = next;
                    break;
                }
                randVal -= currVal;
            }

            if (selected == -1) {
                List<Integer> notVisited = IntStream.range(0, tsp.getN())
                        .mapToObj(Integer::valueOf)
                        .filter(idx -> !visited.contains(idx))
                        .collect(Collectors.toList());
                selected = notVisited.get(rand.nextInt(notVisited.size()));
            }

            totalDistance += tsp.distance(current, selected);
            current = selected;
            cityOrder.add(current);
            visited.add(selected);
        }

        totalDistance += tsp.distance(cityOrder.get(0), current);
        cityOrder.add(cityOrder.get(0));
    }

    @Override
    public int compareTo(TSPAnt o) {
        return Double.compare(this.totalDistance, o.totalDistance);
    }
}
