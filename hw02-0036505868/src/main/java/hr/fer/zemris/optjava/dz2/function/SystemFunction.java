package hr.fer.zemris.optjava.dz2.function;

import Jama.Matrix;
import hr.fer.zemris.optjava.dz2.IHFunction;

public class SystemFunction implements IHFunction {

    private Matrix coefficients;
    private Matrix result;

    public SystemFunction(Matrix coefficients, Matrix result) {
        this.coefficients = coefficients;
        this.result = result;
    }

    public Matrix getCoefficients() {
        return coefficients;
    }

    public Matrix getResult() {
        return result;
    }

    @Override
    public int getNumberOfVariables() {
        return coefficients.getColumnDimension();
    }

    @Override
    public double calculateValue(Matrix point) {
        Matrix diff = differenceMatrix(point);

        double value = 0;
        for (int i = 0; i < diff.getColumnDimension(); i++) {
            value += Math.pow(diff.get(i, 0), 2);
        }

        return value;
    }

    @Override
    public Matrix calculateGradient(Matrix point) {
        Matrix diff = differenceMatrix(point);
        Matrix gradient = new Matrix(getNumberOfVariables(), 1);

        for (int i = 0; i < gradient.getRowDimension(); i++) {
            double derivation = 0;
            for (int j = 0; j < gradient.getRowDimension(); j++) {
                derivation += coefficients.get(j, i) * diff.get(j, 0);
            }
            gradient.set(i, 0, 2*derivation);
        }

        return gradient;
    }

    private Matrix differenceMatrix(Matrix point) {
        Matrix pointResult = coefficients.times(point);
        return pointResult.minus(result);
    }

    @Override
    public Matrix calculateHessMatrix(Matrix point) {
        Matrix hess = new Matrix(getNumberOfVariables(), getNumberOfVariables());

        for (int i = 0; i < hess.getRowDimension(); i++) {
            double derivation = 0;
            for (int j = 0; j < coefficients.getRowDimension(); j++) {
                derivation += Math.pow(coefficients.get(j, i), 2);
            }
            hess.set(i, i, derivation);
        }

        return hess;
    }

}
