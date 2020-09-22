package hr.fer.zemris.optjava.dz13.nodes;

public abstract class TerminalNode extends Node {
    @Override
    public int getRequiredChildren() {
        return 0;
    }
}
