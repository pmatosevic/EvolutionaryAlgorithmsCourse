package hr.fer.zemris.optjava.dz13.nodes;

import hr.fer.zemris.optjava.dz13.algorithm.Ant;

public class Prog3Node extends FunctionNode {
    @Override
    public int getRequiredChildren() {
        return 3;
    }

    @Override
    public Node clone() {
        Prog3Node node = new Prog3Node();
        node.updateDataCloned(this);
        return node;
    }

    @Override
    public void execute(Ant ant) {
        child(0).execute(ant);
        child(1).execute(ant);
        child(2).execute(ant);
    }

    @Override
    public String toString() {
        return "Pr3(" + child(0).toString() + ", " + child(1).toString()+ ", " + child(2).toString() + ")";
    }
}
