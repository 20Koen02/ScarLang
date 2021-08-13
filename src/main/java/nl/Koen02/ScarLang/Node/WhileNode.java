package nl.Koen02.ScarLang.Node;

import nl.Koen02.ScarLang.Context;
import nl.Koen02.ScarLang.RunTimeResult;
import nl.Koen02.ScarLang.Type.ArrayType;
import nl.Koen02.ScarLang.Type.IntegerType;
import nl.Koen02.ScarLang.Type.Type;

import java.util.ArrayList;

public class WhileNode extends Node {
    public Node conditionNode, bodyNode;
    public boolean shouldReturnNull;


    public WhileNode(Node conditionNode, Node bodyNode, boolean shouldReturnNull) {
        this.conditionNode = conditionNode;
        this.bodyNode = bodyNode;
        this.shouldReturnNull = shouldReturnNull;

        posStart = conditionNode.posStart;
        posEnd = bodyNode.posEnd;
    }

    public RunTimeResult visit(Context context) throws Exception {
        RunTimeResult res = new RunTimeResult();

        ArrayList<Type> elements = new ArrayList<>();
        while (true) {
            Type condition = res.register(conditionNode.visit(context));
            if (res.shouldReturn()) return res;
            if (!condition.isTrue()) break;

            Type value = res.register(bodyNode.visit(context));
            if (res.shouldReturn() && !res.loopShouldContinue && !res.loopShouldBreak) return res;
            if (res.loopShouldContinue) continue;
            if (res.loopShouldBreak) break;
            elements.add(value);
        }
        return res.success(shouldReturnNull ? IntegerType.zero : new ArrayType(elements).setContext(context).setPos(posStart, posEnd));
    }
}
