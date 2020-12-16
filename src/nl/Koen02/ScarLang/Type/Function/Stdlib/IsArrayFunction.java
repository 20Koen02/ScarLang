package nl.Koen02.ScarLang.Type.Function.Stdlib;

import nl.Koen02.ScarLang.Context;
import nl.Koen02.ScarLang.RunTimeResult;
import nl.Koen02.ScarLang.Type.ArrayType;
import nl.Koen02.ScarLang.Type.Function.BaseFunction;
import nl.Koen02.ScarLang.Type.IntegerType;
import nl.Koen02.ScarLang.Type.StringType;
import nl.Koen02.ScarLang.Type.Type;

import java.util.ArrayList;
import java.util.Collections;

public final class IsArrayFunction extends BaseFunction {
    private static IsArrayFunction INSTANCE;
    private final ArrayList<String> argNames = new ArrayList<>();

    private IsArrayFunction(String name) {
        super(name);
        Collections.addAll(argNames, "value");
    }

    public RunTimeResult execute(ArrayList<Type> args) throws Exception {
        Context execContext = genNewContext();
        checkAndPopulate(argNames, args, execContext);

        return new RunTimeResult().success(execContext.symbolTable.get("value") instanceof ArrayType ? IntegerType.one : IntegerType.zero);
    }

    public BaseFunction getCopy() {
        IsArrayFunction copy = new IsArrayFunction(name);
        copy.setContext(context);
        copy.setPos(posStart, posEnd);
        return copy;
    }

    public static IsArrayFunction getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new IsArrayFunction("isArray");
        }
        return INSTANCE;
    }
}
