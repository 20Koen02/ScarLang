package nl.Koen02.ScarLang.Type.Function.Builtin;

import nl.Koen02.ScarLang.Context;
import nl.Koen02.ScarLang.RunTimeResult;
import nl.Koen02.ScarLang.Type.FloatType;
import nl.Koen02.ScarLang.Type.Function.BaseFunction;
import nl.Koen02.ScarLang.Type.IntegerType;
import nl.Koen02.ScarLang.Type.Type;

import java.util.ArrayList;
import java.util.Collections;

public final class IsFloatFunction extends BaseFunction {
    private static IsFloatFunction INSTANCE;
    private final ArrayList<String> argNames = new ArrayList<>();

    private IsFloatFunction(String name) {
        super(name);
        Collections.addAll(argNames, "value");
    }

    public RunTimeResult execute(ArrayList<Type> args) throws Exception {
        Context execContext = genNewContext();
        checkAndPopulate(argNames, args, execContext);

        return new RunTimeResult().success(execContext.symbolTable.get("value") instanceof FloatType ? IntegerType.one : IntegerType.zero);
    }

    public BaseFunction getCopy() {
        IsFloatFunction copy = new IsFloatFunction(name);
        copy.setContext(context);
        copy.setPos(posStart, posEnd);
        return copy;
    }

    public static IsFloatFunction getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new IsFloatFunction("isFloat");
        }
        return INSTANCE;
    }
}
