package nl.Koen02.ScarLang.Type.Function.Builtin;

import nl.Koen02.ScarLang.Context;
import nl.Koen02.ScarLang.Error.RunTimeError;
import nl.Koen02.ScarLang.RunTimeResult;
import nl.Koen02.ScarLang.Type.ArrayType;
import nl.Koen02.ScarLang.Type.Function.BaseFunction;
import nl.Koen02.ScarLang.Type.IntegerType;
import nl.Koen02.ScarLang.Type.Type;

import java.util.ArrayList;
import java.util.Collections;

public final class AppendFunction extends BaseFunction {
    private static AppendFunction INSTANCE;
    private final ArrayList<String> argNames = new ArrayList<>();

    private AppendFunction(String name) {
        super(name);
        Collections.addAll(argNames, "array", "value");
    }

    public RunTimeResult execute(ArrayList<Type> args) throws Exception {
        Context execContext = genNewContext();
        checkAndPopulate(argNames, args, execContext);

        Type array = execContext.symbolTable.get("array");
        Type value = execContext.symbolTable.get("value");

        if (!(array instanceof ArrayType))
            throw new RunTimeError(posStart, posEnd, "First argument must be of type array", execContext);

        ((ArrayType) array).addedTo(value);
        return new RunTimeResult().success(IntegerType.zero);
    }

    public BaseFunction getCopy() {
        AppendFunction copy = new AppendFunction(name);
        copy.setContext(context);
        copy.setPos(posStart, posEnd);
        return copy;
    }

    public static AppendFunction getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AppendFunction("append");
        }
        return INSTANCE;
    }
}
