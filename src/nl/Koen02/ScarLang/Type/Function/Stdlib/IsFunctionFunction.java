package nl.Koen02.ScarLang.Type.Function.Stdlib;

import nl.Koen02.ScarLang.Context;
import nl.Koen02.ScarLang.RunTimeResult;
import nl.Koen02.ScarLang.Type.ArrayType;
import nl.Koen02.ScarLang.Type.Function.BaseFunction;
import nl.Koen02.ScarLang.Type.Function.FunctionType;
import nl.Koen02.ScarLang.Type.IntegerType;
import nl.Koen02.ScarLang.Type.Type;

import java.util.ArrayList;
import java.util.Collections;

public final class IsFunctionFunction extends BaseFunction {
    private static IsFunctionFunction INSTANCE;
    private final ArrayList<String> argNames = new ArrayList<>();

    private IsFunctionFunction(String name) {
        super(name);
        Collections.addAll(argNames, "value");
    }

    public RunTimeResult execute(ArrayList<Type> args) throws Exception {
        Context execContext = genNewContext();
        checkAndPopulate(argNames, args, execContext);

        return new RunTimeResult().success(execContext.symbolTable.get("value") instanceof BaseFunction ? IntegerType.one : IntegerType.zero);
    }

    public BaseFunction getCopy() {
        IsFunctionFunction copy = new IsFunctionFunction(name);
        copy.setContext(context);
        copy.setPos(posStart, posEnd);
        return copy;
    }

    public static IsFunctionFunction getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new IsFunctionFunction("isFunction");
        }
        return INSTANCE;
    }
}
