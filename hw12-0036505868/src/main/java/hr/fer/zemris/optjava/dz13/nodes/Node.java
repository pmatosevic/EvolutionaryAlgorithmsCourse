package hr.fer.zemris.optjava.dz13.nodes;

import hr.fer.zemris.optjava.dz13.algorithm.Ant;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Node {

    List<Node> children = new ArrayList<>(0);
    int index = -1;
    int nodesCount = 1;
    int depth = 0;

    public Node() {
    }

    public Node child(int index) {
        return children.get(index);
    }

    public void setChild(int index, Node child) {
        children.set(index, child);
    }

    public int childNum() {
        return children.size();
    }

    public List<Node> getChildren() {
        return children;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getNodesCount() {
        return nodesCount;
    }

    public void setNodesCount(int nodesCount) {
        this.nodesCount = nodesCount;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public List<Node> cloneChildren() {
        return children.stream().map(Node::clone).collect(Collectors.toList());
    }

    public abstract int getRequiredChildren();

    public abstract Node clone();

    public abstract void execute(Ant ant);

    public void updateDataCloned(Node old) {
        this.children = old.cloneChildren();
        this.index = old.index;
        this.nodesCount = old.nodesCount;
        this.depth = old.depth;
    }

}
