package hr.fer.zemris.optjava.dz13.nodes;

import hr.fer.zemris.optjava.dz13.algorithm.Ant;

public class IfNode extends FunctionNode {

    @Override
    public int getRequiredChildren() {
        return 2;
    }

    @Override
    public Node clone() {
        IfNode node = new IfNode();
        node.updateDataCloned(this);
        return node;
    }

    @Override
    public void execute(Ant ant) {
        if (ant.chechFood()) {
            child(0).execute(ant);
        } else {
            child(1).execute(ant);
        }
    }

    @Override
    public String toString() {
        return "IfFoodAhead(" + child(0).toString() + ", " + child(1).toString() + ")";
    }
}
