package nl.Koen02.ScarLang.Node;

import nl.Koen02.ScarLang.Context;
import nl.Koen02.ScarLang.RunTimeResult;
import nl.Koen02.ScarLang.Token;
import nl.Koen02.ScarLang.Type.ArrayType;
import nl.Koen02.ScarLang.Type.IntegerType;
import nl.Koen02.ScarLang.Type.Type;

import java.util.ArrayList;

public class ForNode extends Node {
    public Token varNameTok;
    public Node startValueNode, endValueNode, stepValueNode, bodyNode;
    public boolean shouldReturnNull;


    public ForNode(Token varNameTok, Node startValueNode, Node endValueNode, Node stepValueNode, Node bodyNode, boolean shouldReturnNull) {
        this.varNameTok = varNameTok;
        this.startValueNode = startValueNode;
        this.endValueNode = endValueNode;
        this.stepValueNode = stepValueNode;
        this.bodyNode = bodyNode;
        this.shouldReturnNull = shouldReturnNull;

        posStart = varNameTok.posStart;
        posEnd = bodyNode.posEnd;
    }

    public RunTimeResult visit(Context context) throws Exception {
        RunTimeResult res = new RunTimeResult();

        ArrayList<Type> elements = new ArrayList<>();
        Type startValue = res.register(startValueNode.visit(context));
        if (res.shouldReturn()) return res;
        Type endValue = res.register(endValueNode.visit(context));
        if (res.shouldReturn()) return res;
        Type stepValue = res.register(stepValueNode.visit(context));

        while (stepValue.isPositive() ? startValue.getComparisonLt(endValue).isTrue() : startValue.getComparisonGt(endValue).isTrue()) {
            context.symbolTable.set(varNameTok.value, startValue.getCopy());
            startValue = startValue.addedTo(stepValue);

            Type value = res.register(bodyNode.visit(context));
            if (res.shouldReturn() && !res.loopShouldContinue && !res.loopShouldBreak) return res;
            if (res.loopShouldContinue) continue;
            if (res.loopShouldBreak) break;
            elements.add(value);
        }

        return res.success(shouldReturnNull ? IntegerType.zero : new ArrayType(elements).setContext(context).setPos(posStart, posEnd));
    }
}
