package hr.fer.zemris.optjava.dz13.nodes;

import hr.fer.zemris.optjava.dz13.algorithm.Ant;

public class MoveNode extends TerminalNode {
    @Override
    public Node clone() {
        Node cloned = new MoveNode();
        cloned.updateDataCloned(this);
        return cloned;
    }

    @Override
    public void execute(Ant ant) {
        ant.move();
    }

    @Override
    public String toString() {
        return "Move";
    }
}
