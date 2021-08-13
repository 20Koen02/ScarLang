package nl.Koen02.ScarLang.Type.Function;

import nl.Koen02.ScarLang.Context;
import nl.Koen02.ScarLang.Node.Node;
import nl.Koen02.ScarLang.RunTimeResult;
import nl.Koen02.ScarLang.Type.IntegerType;
import nl.Koen02.ScarLang.Type.Type;

import java.util.ArrayList;

public class FunctionType extends BaseFunction {
    public Node bodyNode;
    public ArrayList<String> argNames;
    public boolean shouldAutoReturn;

    public FunctionType(String name, Node bodyNode, ArrayList<String> argNames, boolean shouldAutoReturn) {
        super(name);
        this.bodyNode = bodyNode;
        this.argNames = argNames;
        this.shouldAutoReturn = shouldAutoReturn;
    }

    public RunTimeResult execute(ArrayList<Type> args) throws Exception {
        RunTimeResult res = new RunTimeResult();

        Context execContext = genNewContext();

        checkAndPopulate(argNames, args, execContext);

        Type value = res.register(bodyNode.visit(execContext));
        if (res.shouldReturn() && res.funcReturnValue == null) return res;

        if (!shouldAutoReturn) value = null;
        if (value == null) value = res.funcReturnValue;
        if (value == null) value = IntegerType.zero;

        return res.success(value);
    }

    public BaseFunction getCopy() {
        FunctionType copy = new FunctionType(name, bodyNode, argNames, shouldAutoReturn);
        copy.setContext(context);
        copy.setPos(posStart, posEnd);
        return copy;
    }

}
