package nl.Koen02.ScarLang.Type;

import nl.Koen02.ScarLang.Context;
import nl.Koen02.ScarLang.Error.RunTimeError;
import nl.Koen02.ScarLang.Interpreter;
import nl.Koen02.ScarLang.Node.Node;
import nl.Koen02.ScarLang.SymbolTable;

import java.util.ArrayList;

public class FunctionType extends Type {
    public String name;
    public Node bodyNode;
    public ArrayList<String> argNames;

    public FunctionType(String name, Node bodyNode, ArrayList<String> argNames) {
        this.name = name != null ? name : "<anonymous>";
        this.bodyNode = bodyNode;
        this.argNames = argNames;
    }

    public Type execute(ArrayList<Type> args) throws Exception {
        Interpreter interpreter = new Interpreter();
        Context newContext = new Context(name, context, posStart);
        newContext.symbolTable = new SymbolTable(newContext.parent.symbolTable);

        if (args.size() > argNames.size()) throw new RunTimeError(posStart, posEnd,
                String.format("%s too many args passed into %s", args.size() - argNames.size(), name), context);
        if (args.size() < argNames.size()) throw new RunTimeError(posStart, posEnd,
                String.format("%s too few args passed into %s", argNames.size() - args.size(), name), context);

        for (int i = 0; i < args.size(); i++) {
            String argName = argNames.get(i);
            Type argValue = args.get(i);
            argValue.setContext(newContext);
            newContext.symbolTable.set(argName, argValue);
        }

        return interpreter.visit(bodyNode, newContext);
    }

    public FunctionType getCopy() {
        FunctionType copy = new FunctionType(name, bodyNode, argNames);
        copy.setContext(context);
        copy.setPos(posStart, posEnd);
        return copy;
    }

    public String get() {
        return String.format("<function %s>", name);
    }
}
