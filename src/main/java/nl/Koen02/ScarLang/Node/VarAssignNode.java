package nl.Koen02.ScarLang.Node;

import nl.Koen02.ScarLang.Token;

public class VarAssignNode extends Node {
    public Token varNameToken;
    public Node valueNode;

    public VarAssignNode(Token varNameToken, Node valueNode) {
        this.varNameToken = varNameToken;
        this.valueNode = valueNode;

        posStart = varNameToken.posStart;
        posEnd = valueNode.posEnd;
    }
}
