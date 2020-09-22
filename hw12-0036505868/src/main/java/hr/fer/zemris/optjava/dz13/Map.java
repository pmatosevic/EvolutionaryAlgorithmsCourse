package hr.fer.zemris.optjava.dz13;

public class Map {

    private boolean[][] map;

    public Map(boolean[][] map) {
        this.map = map;
    }


    public boolean get(int i, int j) {
        return map[i][j];
    }

    public void set(int i, int j, boolean b) {
        map[i][j] = b;
    }

    public int getRows() {
        return map.length;
    }

    public int getCols() {
        return map[0].length;
    }

    public Map duplicate() {
        boolean[][] cloned = new boolean[getRows()][getCols()];
        for (int i = 0; i < getRows(); i++) {
            for (int j = 0; j < getCols(); j++) {
                cloned[i][j] = map[i][j];
            }
        }
        return new Map(cloned);
    }


}
