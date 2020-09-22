package hr.fer.zemris.optjava.dz13.nodes;

import hr.fer.zemris.optjava.dz13.algorithm.Ant;

public class Prog2Node extends FunctionNode {
    @Override
    public int getRequiredChildren() {
        return 2;
    }

    @Override
    public Node clone() {
        Prog2Node node = new Prog2Node();
        node.updateDataCloned(this);
        return node;
    }

    @Override
    public void execute(Ant ant) {
        child(0).execute(ant);
        child(1).execute(ant);
    }

    @Override
    public String toString() {
        return "Pr2(" + child(0).toString() + ", " + child(1).toString() + ")";
    }
}
