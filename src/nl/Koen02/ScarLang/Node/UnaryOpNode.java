package nl.Koen02.ScarLang.Node;

import nl.Koen02.ScarLang.Token;

public class UnaryOpNode extends Node {
    public Token opTok;
    public Node node;

    public UnaryOpNode(Token opTok, Node node) {
        this.opTok = opTok;
        this.node = node;

        posStart = opTok.posStart;
        posEnd = node.posEnd;
    }

    public String get() {
        return String.format("(%s, %s)", opTok.get(), node.get());
    }
}
