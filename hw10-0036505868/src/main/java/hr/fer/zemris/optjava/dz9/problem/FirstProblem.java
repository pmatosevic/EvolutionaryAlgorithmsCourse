package hr.fer.zemris.optjava.dz9.problem;

public class FirstProblem implements MOOPProblem {

    private static final int NUM_OBJECTIVES = 4;
    private static final double[] MIN_X = {-5, -5, -5, -5};
    private static final double[] MAX_X = {5, 5, 5, 5};
    private static final double[] MIN_OBJ = {0, 0, 0, 0};
    private static final double[] MAX_OBJ = {25, 25, 25, 25};


    @Override
    public int getNumberOfObjectives() {
        return NUM_OBJECTIVES;
    }

    @Override
    public double[] evaluateSolution(double[] solution) {
        if (solution.length != 4) throw new IllegalArgumentException("Wrong number of objectives.");

        double[] result = new double[NUM_OBJECTIVES];
        for (int i = 0; i < NUM_OBJECTIVES; i++) {
            if (solution[i] < MIN_X[i] || solution[i] > MAX_X[i]) return null;
            result[i] = solution[i] * solution[i];
        }

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
