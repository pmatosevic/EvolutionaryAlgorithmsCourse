package hr.fer.zemris.optjava.dz13.algorithm;

import hr.fer.zemris.optjava.dz13.nodes.*;

import java.util.Random;
import java.util.function.Supplier;

public class GPUtils {

    private static Supplier[] TERMINALS = {MoveNode::new, LeftTurnNode::new, RightTurnNode::new};
    private static Supplier[] FUNCTIONS = {IfNode::new, Prog2Node::new, Prog3Node::new};

    private static Random rand = new Random();

    public static void annotate(Node root) {
        dfsAnnotate(root, new int[] {0}, 0);
    }

    private static void dfsAnnotate(Node node, int[] currentIndex, int depth) {
        node.setIndex(currentIndex[0]++);
        node.setDepth(depth);
        for (Node child : node.getChildren()) {
            dfsAnnotate(child, currentIndex, depth + 1);
        }
        node.setNodesCount(1 + node.getChildren().stream().mapToInt(Node::getNodesCount).sum());
    }

    public static Node findParentOfIndexedNode(Node root, int index) {
        if (root.childNum() == 0) return null;
        for (Node node : root.getChildren()) {
            if (node.getIndex() == index) return root;
        }

        for (Node node : root.getChildren()) {
            Node result = findParentOfIndexedNode(node, index);
            if (result != null) return result;
        }

        return null;
    }

    public static void insertSubTreeInsteadOfIndexedNode(Node parent, int index, Node newSubtree) {
        for (int i = 0; i < parent.childNum(); i++) {
            if (parent.child(i).getIndex() == index) {
                parent.setChild(i, newSubtree);
                return;
            }
        }
        throw new RuntimeException("Invalid call.");
    }

    public static int maxDepth(Node node) {
        int maxDepth = node.getDepth();
        for (Node child : node.getChildren()) maxDepth = Math.max(maxDepth, maxDepth(child));
        return maxDepth;
    }

    public static Node findIndexedNode(Node root, int index) {
        if (root.getIndex() == index) return root;
        for (Node child : root.getChildren()) {
            Node result = findIndexedNode(child, index);
            if (result != null) return result;
        }
        return null;
    }


    public static Node generateSubTree(int maxDepth, boolean full) {
        if (maxDepth <= 1) return generateTerminal();
        if (full) return generateFunction(maxDepth, true);

        return rand.nextBoolean() ? generateTerminal() : generateFunction(maxDepth, false);
    }

    private static Node generateTerminal() {
        return (Node) TERMINALS[rand.nextInt(TERMINALS.length)].get();
    }

    private static Node generateFunction(int maxDepth, boolean full) {
        FunctionNode node = (FunctionNode) FUNCTIONS[rand.nextInt(FUNCTIONS.length)].get();
        for (int i = 0; i < node.getRequiredChildren(); i++) {
            node.addChild(generateSubTree(maxDepth - 1, full));
        }
        return node;
    }

}
