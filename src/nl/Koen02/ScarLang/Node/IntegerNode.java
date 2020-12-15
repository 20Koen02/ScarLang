package nl.Koen02.ScarLang.Node;

import nl.Koen02.ScarLang.Token;

public class IntegerNode extends Node {
    public Token tok;

    public IntegerNode(Token tok) {
        this.tok = tok;
        posStart = tok.posStart;
        posEnd = tok.posEnd;
    }

    public String get() {
        return tok.get();
    }
}
