package nl.Koen02.ScarLang.Node;

import nl.Koen02.ScarLang.Token;

public class NumberNode extends Node {
    public Token tok;

    public NumberNode(Token tok) {
        this.tok = tok;
        posStart = tok.posStart;
        posEnd = tok.posEnd;
    }

    public String get() {
        return tok.get();
    }
}
