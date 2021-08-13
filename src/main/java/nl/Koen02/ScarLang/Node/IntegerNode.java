package nl.Koen02.ScarLang.Node;

import nl.Koen02.ScarLang.Context;
import nl.Koen02.ScarLang.RunTimeResult;
import nl.Koen02.ScarLang.Token;
import nl.Koen02.ScarLang.Type.IntegerType;
import nl.Koen02.ScarLang.Type.Type;

public class IntegerNode extends Node {
    public Token tok;

    public IntegerNode(Token tok) {
        this.tok = tok;
        posStart = tok.posStart;
        posEnd = tok.posEnd;
    }

    public RunTimeResult visit(Context context) {
        return new RunTimeResult().success(new IntegerType(Integer.parseInt(tok.value)).setContext(context).setPos(posStart, posEnd));
    }

    public String get() {
        return tok.get();
    }
}
