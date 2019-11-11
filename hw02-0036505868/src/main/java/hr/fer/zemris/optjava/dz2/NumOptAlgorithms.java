package hr.fer.zemris.optjava.dz2;

import Jama.Matrix;

import java.util.function.Consumer;

public class NumOptAlgorithms {

    private static final double EPS = 1e-8;
    private static final int OPT_LAMBDA_MAX_ITERS = 10000;

    public static Matrix gradientDescent(IFunction function, int maxIterations, Matrix initialPoint, Consumer<Matrix> pointConsumer) {
        Matrix point = initialPoint.copy();

        for (int i = 0; i < maxIterations; i++) {
            if (pointConsumer != null) pointConsumer.accept(point);

            Matrix gradient = function.calculateGradient(point);
            if (isOptimum(gradient)) return point;

            Matrix direction = gradient.times(-1);
            double lambda = optimalLambda(function, point, direction);

            point.plusEquals(direction.times(lambda));
        }

        return point;
    }

    public static Matrix newtonMethod(IHFunction function, int maxIterations, Matrix initialPoint, Consumer<Matrix> pointConsumer) {
        Matrix point = initialPoint.copy();

        for (int i = 0; i < maxIterations; i++) {
            if (pointConsumer != null) pointConsumer.accept(point);

            Matrix hessMatrix = function.calculateHessMatrix(point);
            Matrix gradient = function.calculateGradient(point);
            if (isOptimum(gradient)) return point;

            Matrix direction = hessMatrix.inverse().times(gradient).times(-1);
            double lambda = optimalLambda(function, point, direction);

            point.plusEquals(direction.times(lambda));
        }

        return point;
    }

    private static boolean isOptimum(Matrix gradient) {
        for (int i = 0; i < gradient.getRowDimension(); i++) {
            if (Math.abs(gradient.get(i, 0)) >= EPS) return false;
        }
        return true;
    }

    private static double optimalLambda(IFunction function, Matrix point, Matrix direction) {
        double minLambda = 0;
        double maxLambda = 1;
        while (thetaDerivation(function, point, direction, maxLambda) < 0) {
            maxLambda *= 2;
        }

        int iters = 0;
        while (true) {
            double lambda = (minLambda + maxLambda) / 2;
            double derivation = thetaDerivation(function, point, direction, lambda);

            if (Math.abs(derivation) < EPS || iters > OPT_LAMBDA_MAX_ITERS) {
                return lambda;
            }

            if (derivation > 0) {
                maxLambda = lambda;
            } else {
                minLambda = lambda;
            }

            iters++;
        }
    }

    private static double thetaDerivation(IFunction function, Matrix point, Matrix direction, double lambda) {
        Matrix translatedPoint = point.plus(direction.times(lambda));
        Matrix gradTransposed = function.calculateGradient(translatedPoint).transpose();
        return gradTransposed.times(direction).get(0, 0);
    }


    public static Matrix randomInitialize(int numberOfVariables) {
        return Matrix.random(numberOfVariables, 1);
    }

}
