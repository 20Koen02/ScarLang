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

public final class LengthFunction extends BaseFunction {
    private static LengthFunction INSTANCE;
    private final ArrayList<String> argNames = new ArrayList<>();

    private LengthFunction(String name) {
        super(name);
        Collections.addAll(argNames, "array");
    }

    public RunTimeResult execute(ArrayList<Type> args) throws Exception {
        Context execContext = genNewContext();
        checkAndPopulate(argNames, args, execContext);

        Type array = execContext.symbolTable.get("array");

        if (!(array instanceof ArrayType))
            throw new RunTimeError(posStart, posEnd, "First argument must be of type array", execContext);


        return new RunTimeResult().success(new IntegerType(((ArrayType) array).elements.size()));
    }

    public BaseFunction getCopy() {
        LengthFunction copy = new LengthFunction(name);
        copy.setContext(context);
        copy.setPos(posStart, posEnd);
        return copy;
    }

    public static LengthFunction getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new LengthFunction("length");
        }
        return INSTANCE;
    }
}
