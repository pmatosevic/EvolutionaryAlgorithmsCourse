package hr.fer.zemris.optjava.dz13.nodes;

import hr.fer.zemris.optjava.dz13.algorithm.Ant;

public class LeftTurnNode extends TerminalNode {
    @Override
    public Node clone() {
        Node cloned = new LeftTurnNode();
        cloned.updateDataCloned(this);
        return cloned;
    }

    @Override
    public void execute(Ant ant) {
        ant.turnLeft();
    }

    @Override
    public String toString() {
        return "Left";
    }
}
