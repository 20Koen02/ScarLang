package nl.Koen02.ScarLang.Type.Function;

import nl.Koen02.ScarLang.Context;
import nl.Koen02.ScarLang.Interpreter;
import nl.Koen02.ScarLang.Node.Node;
import nl.Koen02.ScarLang.Type.Function.Stdlib.PrintFunction;
import nl.Koen02.ScarLang.Type.Type;

import java.util.ArrayList;

public class FunctionType extends BaseFunction {
    public Node bodyNode;
    public ArrayList<String> argNames;

    public FunctionType(String name, Node bodyNode, ArrayList<String> argNames) {
        super(name);
        this.bodyNode = bodyNode;
        this.argNames = argNames;
    }

    public Type execute(ArrayList<Type> args) throws Exception {
        Interpreter interpreter = new Interpreter();
        Context execContext = genNewContext();
        checkAndPopulate(argNames, args, execContext);
        return interpreter.visit(bodyNode, execContext);
    }

    public BaseFunction getCopy() {
        FunctionType copy = new FunctionType(name, bodyNode, argNames);
        copy.setContext(context);
        copy.setPos(posStart, posEnd);
        return copy;
    }

}
