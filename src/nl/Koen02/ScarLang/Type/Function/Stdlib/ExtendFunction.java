package nl.Koen02.ScarLang.Type.Function.Stdlib;

import nl.Koen02.ScarLang.Context;
import nl.Koen02.ScarLang.Error.RunTimeError;
import nl.Koen02.ScarLang.RunTimeResult;
import nl.Koen02.ScarLang.Type.ArrayType;
import nl.Koen02.ScarLang.Type.Function.BaseFunction;
import nl.Koen02.ScarLang.Type.IntegerType;
import nl.Koen02.ScarLang.Type.Type;

import java.util.ArrayList;
import java.util.Collections;

public final class ExtendFunction extends BaseFunction {
    private static ExtendFunction INSTANCE;
    private final ArrayList<String> argNames = new ArrayList<>();

    private ExtendFunction(String name) {
        super(name);
        Collections.addAll(argNames, "array", "secondArray");
    }

    public RunTimeResult execute(ArrayList<Type> args) throws Exception {
        Context execContext = genNewContext();
        checkAndPopulate(argNames, args, execContext);

        Type array = execContext.symbolTable.get("array");
        Type secondArray = execContext.symbolTable.get("secondArray");

        if (!(array instanceof ArrayType))
            throw new RunTimeError(posStart, posEnd, "First argument must be of type array", execContext);
        if (!(secondArray instanceof ArrayType))
            throw new RunTimeError(posStart, posEnd, "Second argument must be of type array", execContext);

        ((ArrayType) array).multipliedBy(secondArray);
        return new RunTimeResult().success(IntegerType.zero);
    }

    public BaseFunction getCopy() {
        ExtendFunction copy = new ExtendFunction(name);
        copy.setContext(context);
        copy.setPos(posStart, posEnd);
        return copy;
    }

    public static ExtendFunction getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ExtendFunction("extend");
        }
        return INSTANCE;
    }
}
