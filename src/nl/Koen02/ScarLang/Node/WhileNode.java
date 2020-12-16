package nl.Koen02.ScarLang.Node;

public class WhileNode extends Node {
    public Node conditionNode, bodyNode;
    public boolean shouldReturnNull;


    public WhileNode(Node conditionNode, Node bodyNode, boolean shouldReturnNull) {
        this.conditionNode = conditionNode;
        this.bodyNode = bodyNode;
        this.shouldReturnNull = shouldReturnNull;

        posStart = conditionNode.posStart;
        posEnd = bodyNode.posEnd;
    }
}
