package nl.Koen02.ScarLang.Node;

import nl.Koen02.ScarLang.Token;

public class ForNode extends Node {
    public Token varNameTok;
    public Node startValueNode, endValueNode, stepValueNode, bodyNode;

    public ForNode(Token varNameTok, Node startValueNode, Node endValueNode, Node stepValueNode, Node bodyNode) {
        this.varNameTok = varNameTok;
        this.startValueNode = startValueNode;
        this.endValueNode = endValueNode;
        this.stepValueNode = stepValueNode;
        this.bodyNode = bodyNode;

        posStart = varNameTok.posStart;
        posEnd = bodyNode.posEnd;
    }
}
