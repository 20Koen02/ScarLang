package nl.Koen02.ScarLang.Node;

import nl.Koen02.ScarLang.Token;

import java.util.ArrayList;

public class FuncDefNode extends Node {
    public Token varNameTok;
    public ArrayList<Token> argNameToks;
    public Node bodyNode;

    public FuncDefNode(Token varNameTok, ArrayList<Token> argNameToks, Node bodyNode) {
        this.varNameTok = varNameTok;
        this.argNameToks = argNameToks;
        this.bodyNode = bodyNode;

        if (varNameTok != null) {
            posStart = varNameTok.posStart;
        } else if (argNameToks.size() > 0) {
            posStart = argNameToks.get(0).posStart;
        } else {
            posStart = bodyNode.posStart;
        }
        posEnd = bodyNode.posEnd;
    }
}
