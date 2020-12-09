package nl.Koen02.ScarLang.Node;

public class WhileNode extends Node {
    public Node conditionNode, bodyNode;

    public WhileNode(Node conditionNode, Node bodyNode) {
        this.conditionNode = conditionNode;
        this.bodyNode = bodyNode;

        posStart = conditionNode.posStart;
        posEnd = bodyNode.posEnd;
    }
}
