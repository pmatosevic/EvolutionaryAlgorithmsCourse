package hr.fer.zemris.optjava.dz13.algorithm;

import hr.fer.zemris.optjava.dz13.AntState;
import hr.fer.zemris.optjava.dz13.Map;

import java.util.ArrayList;
import java.util.List;

public class Ant {

    private static int[] DX = {0, 1, 0, -1};
    private static int[] DY = {1, 0, -1, 0};

    Map map;

    int currentRow = 0;
    int currentColumn = 0;
    int orientation = 0;

    int remaining;
    int food = 0;

    List<AntState> allPassedStates = new ArrayList<>();

    public Ant(int remaining, Map map) {
        this.remaining = remaining;
        this.map = map;

        addState();
    }

    public int getResult() {
        return food;
    }

    public List<AntState> getStates() {
        return allPassedStates;
    }

    public boolean chechFood() {
        int nextRow = currentRow + DX[orientation] + map.getRows();
        nextRow %= map.getRows();

        int nextColumn = currentColumn + DY[orientation] + map.getCols();
        nextColumn %= map.getCols();

        return map.get(nextRow, nextColumn);
    }

    public void move() {
        lowerRemaining();

        currentRow = currentRow + DX[orientation] + map.getRows();
        currentRow %= map.getRows();

        currentColumn = currentColumn + DY[orientation] + map.getCols();
        currentColumn %= map.getCols();

        if (map.get(currentRow, currentColumn)) {
            food++;
            map.set(currentRow, currentColumn, false);
        }

        addState();
    }

    public void turnRight() {
        lowerRemaining();
        orientation = (orientation+1) % DX.length;

        addState();
    }

    public void turnLeft() {
        lowerRemaining();
        orientation = (orientation-1 + DX.length) % DX.length;

        addState();
    }


    private void lowerRemaining() {
        if (remaining <= 0) throw new IllegalStateException("No remaining actions.");
        remaining--;
    }

    private void addState() {
        allPassedStates.add(new AntState(currentRow, currentColumn, orientation));
    }

}
