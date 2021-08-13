package nl.Koen02.ScarLang.Node;

import nl.Koen02.ScarLang.Context;
import nl.Koen02.ScarLang.RunTimeResult;
import nl.Koen02.ScarLang.Token;
import nl.Koen02.ScarLang.Type.Function.FunctionType;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class FuncDefNode extends Node {
    public Token varNameTok;
    public ArrayList<Token> argNameToks;
    public Node bodyNode;
    public boolean shouldAutoReturn;

    public FuncDefNode(Token varNameTok, ArrayList<Token> argNameToks, Node bodyNode, boolean shouldAutoReturn) {
        this.varNameTok = varNameTok;
        this.argNameToks = argNameToks;
        this.bodyNode = bodyNode;
        this.shouldAutoReturn = shouldAutoReturn;

        if (varNameTok != null) {
            posStart = varNameTok.posStart;
        } else if (argNameToks.size() > 0) {
            posStart = argNameToks.get(0).posStart;
        } else {
            posStart = bodyNode.posStart;
        }
        posEnd = bodyNode.posEnd;
    }

    public RunTimeResult visit(Context context) throws Exception {
        RunTimeResult res = new RunTimeResult();

        String funcName = varNameTok != null ? varNameTok.value : null;
        ArrayList<String> argNames = (ArrayList<String>) argNameToks.stream().map(argName -> argName.value).collect(Collectors.toList());
        FunctionType func_value = (FunctionType) new FunctionType(funcName, bodyNode, argNames, shouldAutoReturn).setContext(context).setPos(posStart, posEnd);

        if (varNameTok != null) context.symbolTable.set(funcName, func_value);
        return res.success(func_value);
    }
}
