package hr.fer.zemris.optjava.dz13.nodes;

import hr.fer.zemris.optjava.dz13.algorithm.Ant;

public class RightTurnNode extends TerminalNode {
    @Override
    public Node clone() {
        Node cloned = new RightTurnNode();
        cloned.updateDataCloned(this);
        return cloned;
    }

    @Override
    public void execute(Ant ant) {
        ant.turnRight();
    }

    @Override
    public String toString() {
        return "Right";
    }
}
