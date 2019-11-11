package hr.fer.zemris.optjava.dz2;

import Jama.Matrix;

public interface IFunction {

    int getNumberOfVariables();

    double calculateValue(Matrix point);

    Matrix calculateGradient(Matrix point);

}
