package hr.fer.zemris.optjava.dz8.ann;

public class TangensTransferFunction implements TransferFunction {
    @Override
    public double calculate(double x) {
        return (1 - Math.exp(-x))/(1 + Math.exp(-x));
    }
}
