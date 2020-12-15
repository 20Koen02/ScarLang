package nl.Koen02.ScarLang.Type.Function.Stdlib;

import nl.Koen02.ScarLang.Context;
import nl.Koen02.ScarLang.Error.RunTimeError;
import nl.Koen02.ScarLang.Type.ArrayType;
import nl.Koen02.ScarLang.Type.Function.BaseFunction;
import nl.Koen02.ScarLang.Type.IntegerType;
import nl.Koen02.ScarLang.Type.Type;

import java.util.ArrayList;
import java.util.Collections;

public final class PopFunction extends BaseFunction {
    private static PopFunction INSTANCE;
    private final ArrayList<String> argNames = new ArrayList<>();

    private PopFunction(String name) {
        super(name);
        Collections.addAll(argNames, "array", "value");
    }

    public Type execute(ArrayList<Type> args) throws Exception {
        Context execContext = genNewContext();
        checkAndPopulate(argNames, args, execContext);

        Type array = execContext.symbolTable.get("array");
        Type value = execContext.symbolTable.get("value");

        if (!(array instanceof ArrayType))
            throw new RunTimeError(posStart, posEnd, "First argument must be of type array", execContext);

        ((ArrayType) array).subtractedBy(value);
        return IntegerType.zero;
    }

    public BaseFunction getCopy() {
        PopFunction copy = new PopFunction(name);
        copy.setContext(context);
        copy.setPos(posStart, posEnd);
        return copy;
    }

    public static PopFunction getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PopFunction("pop");
        }
        return INSTANCE;
    }
}
