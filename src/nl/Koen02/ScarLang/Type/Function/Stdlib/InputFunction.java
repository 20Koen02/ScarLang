package nl.Koen02.ScarLang.Type.Function.Stdlib;

import nl.Koen02.ScarLang.Context;
import nl.Koen02.ScarLang.RunTimeResult;
import nl.Koen02.ScarLang.Type.Function.BaseFunction;
import nl.Koen02.ScarLang.Type.IntegerType;
import nl.Koen02.ScarLang.Type.StringType;
import nl.Koen02.ScarLang.Type.Type;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public final class InputFunction extends BaseFunction {
    private static InputFunction INSTANCE;
    private final ArrayList<String> argNames = new ArrayList<>();

    private InputFunction(String name) {
        super(name);
    }

    public RunTimeResult execute(ArrayList<Type> args) throws Exception {
        Context execContext = genNewContext();
        checkAndPopulate(argNames, args, execContext);

        Scanner stdin = new Scanner(System.in);
        String inp = stdin.nextLine();
        return new RunTimeResult().success(new StringType(inp));
    }

    public BaseFunction getCopy() {
        InputFunction copy = new InputFunction(name);
        copy.setContext(context);
        copy.setPos(posStart, posEnd);
        return copy;
    }

    public static InputFunction getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new InputFunction("input");
        }
        return INSTANCE;
    }
}
