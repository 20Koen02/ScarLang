package nl.Koen02.ScarLang.Type.Function;


import nl.Koen02.ScarLang.Context;
import nl.Koen02.ScarLang.Error.RunTimeError;
import nl.Koen02.ScarLang.SymbolTable;
import nl.Koen02.ScarLang.Type.Type;

import java.util.ArrayList;

public class BaseFunction extends Type {
    public String name;

    public BaseFunction(String name) {
        this.name = name != null ? name : "<anonymous>";
    }

    public Context genNewContext() {
        Context newContext = new Context(name, context, posStart);
        newContext.symbolTable = new SymbolTable(newContext.parent.symbolTable);
        return newContext;
    }

    public void argsErrorCheck(ArrayList<String> argNames, ArrayList<Type> args) throws RunTimeError {
        if (args.size() > argNames.size())
            throw new RunTimeError(posStart, posEnd,
                    String.format("%s too many args passed into %s", args.size() - argNames.size(), name), context);
        if (args.size() < argNames.size())
            throw new RunTimeError(posStart, posEnd,
                    String.format("%s too few args passed into %s", argNames.size() - args.size(), name), context);
    }

    public void populateArgs(ArrayList<String> argNames, ArrayList<Type> args, Context execContext) {
        for (int i = 0; i < args.size(); i++) {
            String argName = argNames.get(i);
            Type argValue = args.get(i);
            argValue.setContext(execContext);
            execContext.symbolTable.set(argName, argValue);
        }
    }

    public void checkAndPopulate(ArrayList<String> argNames, ArrayList<Type> args, Context execContext) throws RunTimeError {
        argsErrorCheck(argNames, args);
        populateArgs(argNames, args, execContext);
    }

    public String get() {
        return String.format("<function %s>", name);
    }
}
