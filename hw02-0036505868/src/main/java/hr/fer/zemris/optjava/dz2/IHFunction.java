package hr.fer.zemris.optjava.dz2;

import Jama.Matrix;

public interface IHFunction extends IFunction {

    Matrix calculateHessMatrix(Matrix point);

}
