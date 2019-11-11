package hr.fer.zemris.optjava.hw03.temperature;

public class GeometricTempSchedule implements ITempSchedule {

    private double alpha;
    private double tInitial;
    private int innerLimit;
    private int outerLimit;

    private double tCurrent;


    public GeometricTempSchedule(double alpha, double tInitial, int innerLimit, int outerLimit) {
        this.alpha = alpha;
        this.tInitial = tInitial;
        this.tCurrent = tInitial;
        this.innerLimit = innerLimit;
        this.outerLimit = outerLimit;
    }

    @Override
    public double getNextTemperature() {
        double tempCopy = tCurrent;
        tCurrent = tCurrent * alpha;
        return tempCopy;
    }

    @Override
    public int getInnerLoopCounter() {
        return innerLimit;
    }

    @Override
    public int getOuterLoopCounter() {
        return outerLimit;
    }
}
