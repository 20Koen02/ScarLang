package nl.Koen02.ScarLang.Type.Function;

import nl.Koen02.ScarLang.Context;
import nl.Koen02.ScarLang.Interpreter;
import nl.Koen02.ScarLang.Node.Node;
import nl.Koen02.ScarLang.Type.Function.Stdlib.PrintFunction;
import nl.Koen02.ScarLang.Type.IntegerType;
import nl.Koen02.ScarLang.Type.Type;

import java.util.ArrayList;

public class FunctionType extends BaseFunction {
    public Node bodyNode;
    public ArrayList<String> argNames;
    public boolean shouldReturnNull;

    public FunctionType(String name, Node bodyNode, ArrayList<String> argNames, boolean shouldReturnNull) {
        super(name);
        this.bodyNode = bodyNode;
        this.argNames = argNames;
        this.shouldReturnNull = shouldReturnNull;
    }

    public Type execute(ArrayList<Type> args) throws Exception {
        Interpreter interpreter = new Interpreter();
        Context execContext = genNewContext();
        checkAndPopulate(argNames, args, execContext);
        Type res = interpreter.visit(bodyNode, execContext);
        return shouldReturnNull ? IntegerType.zero : res;
    }

    public BaseFunction getCopy() {
        FunctionType copy = new FunctionType(name, bodyNode, argNames, shouldReturnNull);
        copy.setContext(context);
        copy.setPos(posStart, posEnd);
        return copy;
    }

}
