package nl.Koen02.ScarLang.Node;

import nl.Koen02.ScarLang.Token;

public class NumberNode extends Node {
    Token tok;
    public NumberNode(Token tok) {
        this.tok = tok;
    }

    public String get() {
        return tok.get();
    }
}
