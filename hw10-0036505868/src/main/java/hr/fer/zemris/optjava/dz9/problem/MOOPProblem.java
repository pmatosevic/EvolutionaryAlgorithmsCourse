package hr.fer.zemris.optjava.dz9.problem;

public interface MOOPProblem {

    int getNumberOfObjectives();

    double[] evaluateSolution(double[] solution);

    double xMin(int index);

    double xMax(int index);

    double objMin(int index);

    double objMax(int index);

}