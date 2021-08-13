package nl.Koen02.ScarLang.Node;

import nl.Koen02.ScarLang.Token;

public class VarAccessNode extends Node {
    public Token varNameToken;
    public VarAccessNode(Token varNameToken) {
        this.varNameToken = varNameToken;

        posStart = varNameToken.posStart;
        posEnd = varNameToken.posEnd;
    }
}
