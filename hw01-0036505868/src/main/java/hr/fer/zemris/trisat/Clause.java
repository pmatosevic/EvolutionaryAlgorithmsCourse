package hr.fer.zemris.trisat;

public class Clause {

    private int[] indexes;

    public Clause(int[] indexes) {
        this.indexes = indexes;
    }

    public int getSize() {
        return indexes.length;
    }

    public int getLiteral(int index) {
        return Math.abs(indexes[index]);
    }

    public boolean isSatisfied(BitVector assignment) {
        for (int index : indexes) {
            int literal = Math.abs(index);
            if (assignment.get(literal - 1) == index > 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < indexes.length; i++) {
            if (indexes[i] < 0) sb.append("!");
            sb.append("x" + Math.abs(indexes[i]));
            if (i != indexes.length - 1) sb.append("+");
        }
        return sb.toString();
    }

}
