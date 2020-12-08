package nl.Koen02.ScarLang.Node;

import nl.Koen02.ScarLang.Token;

public class BinOpNode extends Node {
    Node leftNode, rightNode;
    Token opTok;

    public BinOpNode(Node leftNode, Token opTok, Node rightNode) {
        this.leftNode = leftNode;
        this.opTok = opTok;
        this.rightNode = rightNode;
    }

    public String get() {
        return String.format("(%s, %s, %s)", leftNode.get(), opTok.get(), rightNode.get());
    }
}
