package hr.fer.zemris.optjava.dz2.function;

import Jama.Matrix;
import hr.fer.zemris.optjava.dz2.IHFunction;

public class Function1 implements IHFunction {

    @Override
    public int getNumberOfVariables() {
        return 2;
    }

    @Override
    public double calculateValue(Matrix point) {
        double x1 = point.get(0, 0);
        double x2 = point.get(1, 0);
        return Math.pow(x1, 2) + Math.pow(x2 - 1, 2);
    }

    @Override
    public Matrix calculateGradient(Matrix point) {
        double x1 = point.get(0, 0);
        double x2 = point.get(1, 0);
        double grad1 = 2 * x1;
        double grad2 = 2 * (x2 - 1);
        return new Matrix(new double[][] {{grad1}, {grad2}});
    }

    @Override
    public Matrix calculateHessMatrix(Matrix point) {
        double grad11 = 2;
        double grad12 = 0;
        double grad22 = 2;
        return new Matrix(new double[][] {{grad11, grad12}, {grad12, grad22}});
    }
}
