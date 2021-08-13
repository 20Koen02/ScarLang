package nl.Koen02.ScarLang.Node;

import nl.Koen02.ScarLang.Context;
import nl.Koen02.ScarLang.RunTimeResult;
import nl.Koen02.ScarLang.Token;
import nl.Koen02.ScarLang.Type.Type;

public class VarAssignNode extends Node {
    public Token varNameToken;
    public Node valueNode;

    public VarAssignNode(Token varNameToken, Node valueNode) {
        this.varNameToken = varNameToken;
        this.valueNode = valueNode;

        posStart = varNameToken.posStart;
        posEnd = valueNode.posEnd;
    }

    public RunTimeResult visit(Context context) throws Exception {
        RunTimeResult res = new RunTimeResult();
        String varName = varNameToken.value;
        Type value = res.register(valueNode.visit(context));
        if (res.shouldReturn()) return res;

        context.symbolTable.set(varName, value);
        return res.success(value);
    }
}
