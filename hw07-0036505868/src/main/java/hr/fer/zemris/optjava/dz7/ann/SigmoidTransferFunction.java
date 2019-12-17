package hr.fer.zemris.optjava.dz7.ann;

public class SigmoidTransferFunction implements TransferFunction {

    public double calculate(double x) {
        return 1.0 / (1.0 + Math.exp(-x));
    }

}
