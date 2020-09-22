package hr.fer.zemris.optjava.dz13;

public class AntState {

    int row;
    int col;
    int orientation;

    public AntState(int row, int col, int orientation) {
        this.row = row;
        this.col = col;
        this.orientation = orientation;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public int getOrientation() {
        return orientation;
    }
}
