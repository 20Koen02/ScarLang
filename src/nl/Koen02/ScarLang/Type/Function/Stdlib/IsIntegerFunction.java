package nl.Koen02.ScarLang.Type.Function.Stdlib;

import nl.Koen02.ScarLang.Context;
import nl.Koen02.ScarLang.RunTimeResult;
import nl.Koen02.ScarLang.Type.Function.BaseFunction;
import nl.Koen02.ScarLang.Type.IntegerType;
import nl.Koen02.ScarLang.Type.Type;

import java.util.ArrayList;
import java.util.Collections;

public final class IsIntegerFunction extends BaseFunction {
    private static IsIntegerFunction INSTANCE;
    private final ArrayList<String> argNames = new ArrayList<>();

    private IsIntegerFunction(String name) {
        super(name);
        Collections.addAll(argNames, "value");
    }

    public RunTimeResult execute(ArrayList<Type> args) throws Exception {
        Context execContext = genNewContext();
        checkAndPopulate(argNames, args, execContext);

        return new RunTimeResult().success(execContext.symbolTable.get("value") instanceof IntegerType ? IntegerType.one : IntegerType.zero);
    }

    public BaseFunction getCopy() {
        IsIntegerFunction copy = new IsIntegerFunction(name);
        copy.setContext(context);
        copy.setPos(posStart, posEnd);
        return copy;
    }

    public static IsIntegerFunction getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new IsIntegerFunction("isInteger");
        }
        return INSTANCE;
    }
}
