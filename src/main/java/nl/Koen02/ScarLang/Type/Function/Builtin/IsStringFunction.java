package nl.Koen02.ScarLang.Type.Function.Builtin;

import nl.Koen02.ScarLang.Context;
import nl.Koen02.ScarLang.RunTimeResult;
import nl.Koen02.ScarLang.Type.Function.BaseFunction;
import nl.Koen02.ScarLang.Type.IntegerType;
import nl.Koen02.ScarLang.Type.StringType;
import nl.Koen02.ScarLang.Type.Type;

import java.util.ArrayList;
import java.util.Collections;

public final class IsStringFunction extends BaseFunction {
    private static IsStringFunction INSTANCE;
    private final ArrayList<String> argNames = new ArrayList<>();

    private IsStringFunction(String name) {
        super(name);
        Collections.addAll(argNames, "value");
    }

    public RunTimeResult execute(ArrayList<Type> args) throws Exception {
        Context execContext = genNewContext();
        checkAndPopulate(argNames, args, execContext);

        return new RunTimeResult().success(execContext.symbolTable.get("value") instanceof StringType ? IntegerType.one : IntegerType.zero);
    }

    public BaseFunction getCopy() {
        IsStringFunction copy = new IsStringFunction(name);
        copy.setContext(context);
        copy.setPos(posStart, posEnd);
        return copy;
    }

    public static IsStringFunction getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new IsStringFunction("isString");
        }
        return INSTANCE;
    }
}
