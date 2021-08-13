package nl.Koen02.ScarLang.Node;

import nl.Koen02.ScarLang.Context;
import nl.Koen02.ScarLang.RunTimeResult;
import nl.Koen02.ScarLang.Token;
import nl.Koen02.ScarLang.Type.StringType;

public class StringNode extends Node {
    public Token tok;

    public StringNode(Token tok) {
        this.tok = tok;
        posStart = tok.posStart;
        posEnd = tok.posEnd;
    }

    public RunTimeResult visit(Context context) throws Exception {
        return new RunTimeResult().success(new StringType(tok.value).setContext(context).setPos(posStart, posEnd));
    }

    public String get() {
        return tok.get();
    }
}
