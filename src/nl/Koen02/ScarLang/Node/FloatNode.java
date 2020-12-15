package nl.Koen02.ScarLang.Node;

import nl.Koen02.ScarLang.Token;

public class FloatNode extends Node {
    public Token tok;

    public FloatNode(Token tok) {
        this.tok = tok;
        posStart = tok.posStart;
        posEnd = tok.posEnd;
    }

    public String get() {
        return tok.get();
    }
}
