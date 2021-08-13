package nl.Koen02.ScarLang.Node;

import nl.Koen02.ScarLang.Context;
import nl.Koen02.ScarLang.RunTimeResult;
import nl.Koen02.ScarLang.Type.IntegerType;
import nl.Koen02.ScarLang.Type.Type;

import java.util.ArrayList;

public class IfNode extends Node {
    public ArrayList<ArrayList<Node>> cases;
    public Node elseCase;
    public boolean shouldReturnNull;

    public IfNode(ArrayList<ArrayList<Node>> cases, Node elseCase, boolean shouldReturnNull) {
        this.cases = cases;
        this.elseCase = elseCase;
        this.shouldReturnNull = shouldReturnNull;

        posStart = cases.get(0).get(0).posStart;
        posEnd = elseCase != null ? elseCase.posEnd : cases.get(cases.size() - 1).get(0).posEnd;
    }

    public RunTimeResult visit(Context context) throws Exception {
        RunTimeResult res = new RunTimeResult();

        for (ArrayList<Node> condExpr : cases) {
            Node condition = condExpr.get(0);
            Node expression = condExpr.get(1);
            Type conditionValue = res.register(condition.visit(context));
            if (res.shouldReturn()) return res;
            if (conditionValue.isTrue()) {
                Type v = res.register(expression.visit(context));
                if (res.shouldReturn()) return res;
                return res.success(shouldReturnNull ? IntegerType.zero : v);
            }
        }
        if (elseCase != null) {
            {
                Type v = res.register(elseCase.visit(context));
                if (res.shouldReturn()) return res;
                return res.success(shouldReturnNull ? IntegerType.zero : v);
            }
        }
        return res.success(IntegerType.zero);
    }
}
