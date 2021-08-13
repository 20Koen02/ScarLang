package nl.Koen02.ScarLang.Node;

import nl.Koen02.ScarLang.Context;
import nl.Koen02.ScarLang.Error.RunTimeError;
import nl.Koen02.ScarLang.RunTimeResult;
import nl.Koen02.ScarLang.Token;
import nl.Koen02.ScarLang.Type.Type;

public class VarAccessNode extends Node {
    public Token varNameToken;
    public VarAccessNode(Token varNameToken) {
        this.varNameToken = varNameToken;

        posStart = varNameToken.posStart;
        posEnd = varNameToken.posEnd;
    }

    public RunTimeResult visit(Context context) throws Exception {
        RunTimeResult res = new RunTimeResult();
        String varName = varNameToken.value;
        Type value = context.symbolTable.get(varName);

        if (value == null) throw new RunTimeError(posStart, posEnd, String.format("'%s' is not defined", varName), context);

        value = value.getCopy().setPos(posStart, posEnd).setContext(context);
        return res.success(value);
    }
}
