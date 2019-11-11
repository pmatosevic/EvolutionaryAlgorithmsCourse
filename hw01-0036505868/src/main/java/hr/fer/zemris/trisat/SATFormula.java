package hr.fer.zemris.trisat;

import java.util.Arrays;
import java.util.stream.Collectors;

public class SATFormula {

    private int numberOfVariables;
    private Clause[] clauses;

    public SATFormula(int numberOfVariables, Clause[] clauses) {
        this.numberOfVariables = numberOfVariables;
        this.clauses = clauses;
    }

    public int getNumberOfVariables() {
        return numberOfVariables;
    }

    public int getNumberOfClauses() {
        return clauses.length;
    }

    public Clause getClause(int index) {
        return clauses[index];
    }

    public boolean isSatisfied(BitVector assignment) {
        for (Clause clause : clauses) {
            if (!clause.isSatisfied(assignment)) {
                return false;
            }
        }
        return true;
    }

    public int countSatisfiedClauses(BitVector assignment) {
        int count = 0;
        for (Clause clause : clauses) {
            if (clause.isSatisfied(assignment)) count++;
        }
        return count;
    }

    @Override
    public String toString() {
       return Arrays.stream(clauses)
               .map(clause -> "(" + clause.toString() + ")")
               .collect(Collectors.joining("*"));
    }

}
