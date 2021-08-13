package nl.Koen02.ScarLang.Node;

import nl.Koen02.ScarLang.Context;
import nl.Koen02.ScarLang.RunTimeResult;
import nl.Koen02.ScarLang.Token;
import nl.Koen02.ScarLang.Type.FloatType;
import nl.Koen02.ScarLang.Type.IntegerType;

public class FloatNode extends Node {
    public Token tok;

    public FloatNode(Token tok) {
        this.tok = tok;
        posStart = tok.posStart;
        posEnd = tok.posEnd;
    }

    public RunTimeResult visit(Context context) {
        return new RunTimeResult().success(new FloatType(Float.parseFloat(tok.value)).setContext(context).setPos(posStart, posEnd));
    }

    public String get() {
        return tok.get();
    }
}
