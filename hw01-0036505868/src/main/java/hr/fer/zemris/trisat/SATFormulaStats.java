package hr.fer.zemris.trisat;

public class SATFormulaStats {

    private static final double PERCENTAGE_CONSTANT_UP = 0.01;
    private static final double PERCENTAGE_CONSTANT_DOWN = 0.1;
    private static final double PERCENTAGE_UNIT_AMOUNT = 50;

    private SATFormula formula;
    private double[] post;
    private int numSatisfiedClauses;
    private double percentageBonus;

    public SATFormulaStats(SATFormula formula) {
        this.formula = formula;
        this.post = new double[formula.getNumberOfClauses()];
    }

    public void setAssignment(BitVector assignment, boolean updatePercentages) {
        numSatisfiedClauses = 0;
        percentageBonus = 0.0;
        for (int i = 0; i < formula.getNumberOfClauses(); i++) {
            Clause clause = formula.getClause(i);

            if (clause.isSatisfied(assignment)) {
                if (updatePercentages) {
                    post[i] += (1-post[i]) * PERCENTAGE_CONSTANT_UP;
                }
                percentageBonus += PERCENTAGE_UNIT_AMOUNT * (1-post[i]);
                numSatisfiedClauses++;
            } else {
                if (updatePercentages) {
                    post[i] += (0-post[i]) * PERCENTAGE_CONSTANT_DOWN;
                }
                percentageBonus += -PERCENTAGE_UNIT_AMOUNT * (1-post[i]);
            }
        }
    }

    public int getNumberOfSatisfied() {
        return numSatisfiedClauses;
    }

    public boolean isSatisfied() {
        return numSatisfiedClauses == formula.getNumberOfClauses();
    }

    public double getPercentageBonus() {
        return percentageBonus;
    }

    public double getPercentage(int index) {
        return post[index];
    }

}