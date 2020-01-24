package hr.fer.zemris.optjava.dz9.problem;

public class SecondProblem implements MOOPProblem {

    private static final int NUM_OBJECTIVES = 2;
    private static final double[] MIN_X = {0.1, 0};
    private static final double[] MAX_X = {1, 5};
    private static final double[] MIN_OBJ = {0.1, 1};
    private static final double[] MAX_OBJ = {1, 60};

    @Override
    public int getNumberOfObjectives() {
        return NUM_OBJECTIVES;
    }

    @Override
    public double[] evaluateSolution(double[] solution) {
        if (solution.length != 2) throw new IllegalArgumentException("Wrong number of objectives.");
        for (int i = 0; i < 2; i++) {
            if (solution[i] < MIN_X[i] || solution[i] > MAX_X[i]) return null;
        }

        double[] result = new double[NUM_OBJECTIVES];
        result[0] = solution[0];
        result[1] = (1 + solution[1])/solution[0];
        return result;
    }

    @Override
    public double xMin(int index) {
        return MIN_X[index];
    }

    @Override
    public double xMax(int index) {
        return MAX_X[index];
    }

    @Override
    public double objMin(int index) {
        return MIN_OBJ[index];
    }

    @Override
    public double objMax(int index) {
        return MAX_OBJ[index];
    }
}
