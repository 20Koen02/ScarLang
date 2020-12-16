package nl.Koen02.ScarLang.Type.Function.Stdlib;

import nl.Koen02.ScarLang.Context;
import nl.Koen02.ScarLang.Error.RunTimeError;
import nl.Koen02.ScarLang.RunTimeResult;
import nl.Koen02.ScarLang.Type.Function.BaseFunction;
import nl.Koen02.ScarLang.Type.IntegerType;
import nl.Koen02.ScarLang.Type.StringType;
import nl.Koen02.ScarLang.Type.Type;

import java.util.ArrayList;
import java.util.Scanner;

public final class ClearFunction extends BaseFunction {
    private static ClearFunction INSTANCE;
    private final ArrayList<String> argNames = new ArrayList<>();

    private ClearFunction(String name) {
        super(name);
    }

    public RunTimeResult execute(ArrayList<Type> args) throws Exception {
        Context execContext = genNewContext();
        checkAndPopulate(argNames, args, execContext);
        try {
            Runtime.getRuntime().exec(System.getProperty("os.name").contains("Windows") ? "cls" : "clear");
        } catch (Exception e) {
            throw new RunTimeError(posStart, posEnd, "Screen clearing is not supported", context);
        }
        return new RunTimeResult().success(IntegerType.zero);
    }

    public BaseFunction getCopy() {
        ClearFunction copy = new ClearFunction(name);
        copy.setContext(context);
        copy.setPos(posStart, posEnd);
        return copy;
    }

    public static ClearFunction getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ClearFunction("clear");
        }
        return INSTANCE;
    }
}
