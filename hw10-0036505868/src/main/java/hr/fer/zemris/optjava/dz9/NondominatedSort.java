package hr.fer.zemris.optjava.dz9;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class NondominatedSort {

    public static List<List<Chromosome>> sort(List<Chromosome> population) {
        int n = population.size();
        List<Wrapper> solutions = population.stream().map(Wrapper::new).collect(Collectors.toList());

        for (int i = 0; i < n; i++) {
            Wrapper sol1 = solutions.get(i);
            for (int j = 0; j < n; j++) {
                if (i == j) continue;

                Wrapper sol2 = solutions.get(j);
                if (dominates(sol1.chromosome, sol2.chromosome)) {
                    sol1.dominatesOn.add(sol2);
                    sol2.dominatedBy++;
                }
            }
        }

        List<List<Chromosome>> result = new ArrayList<>();
        List<Wrapper> front = solutions.stream()
                .filter(wrapper -> wrapper.dominatedBy == 0)
                .collect(Collectors.toList());
        while (!front.isEmpty()) {
            result.add(front.stream().map(wrapper -> wrapper.chromosome).collect(Collectors.toList()));
            List<Wrapper> nextFront = new ArrayList<>();
            for (Wrapper frontSolution : front) {
                for (Wrapper dominates : frontSolution.dominatesOn) {
                    dominates.dominatedBy--;
                    if (dominates.dominatedBy == 0) {
                        nextFront.add(dominates);
                    }
                }
            }
            front = nextFront;
        }

        for (int i = 0; i < result.size(); i++) {
            int rank = i;
            result.get(i).forEach(c -> c.rank = rank);
        }

        return result;
    }

    private static boolean dominates(Chromosome c1, Chromosome c2) {
        if (c1.objectives.length != c2.objectives.length) throw new IllegalArgumentException("Different objectives.");

        boolean betterThan = false;
        for (int i = 0; i < c1.objectives.length; i++) {
            if (c1.objectives[i] < c2.objectives[i]) betterThan = true;
            if (c1.objectives[i] > c2.objectives[i]) return false;
        }
        return betterThan;
    }

    private static class Wrapper {
        Chromosome chromosome;
        List<Wrapper> dominatesOn = new ArrayList<>();
        int dominatedBy = 0;

        public Wrapper(Chromosome chromosome) {
            this.chromosome = chromosome;
        }
    }

}
