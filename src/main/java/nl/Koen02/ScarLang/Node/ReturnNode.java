package nl.Koen02.ScarLang.Node;

import nl.Koen02.ScarLang.Context;
import nl.Koen02.ScarLang.Position;
import nl.Koen02.ScarLang.RunTimeResult;
import nl.Koen02.ScarLang.Type.IntegerType;
import nl.Koen02.ScarLang.Type.Type;

public class ReturnNode extends Node {
    public Node nodeToReturn;

    public ReturnNode(Node nodeToReturn, Position posStart, Position posEnd) {
        this.nodeToReturn = nodeToReturn;
        this.posStart = posStart;
        this.posEnd = posEnd;
    }

    public RunTimeResult visit(Context context) throws Exception {
        RunTimeResult res = new RunTimeResult();

        Type value;
        if (nodeToReturn != null) {
            value = res.register(nodeToReturn.visit(context));
        } else {
            value = IntegerType.zero;
        }
        return res.successReturn(value);
    }
}
