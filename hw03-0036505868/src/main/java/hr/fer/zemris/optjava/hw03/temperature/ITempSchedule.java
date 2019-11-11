package hr.fer.zemris.optjava.hw03.temperature;

public interface ITempSchedule {

    double getNextTemperature();

    int getInnerLoopCounter();

    int getOuterLoopCounter();

}
