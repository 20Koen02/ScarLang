package nl.Koen02.ScarLang.Type.Function.Stdlib;

import nl.Koen02.ScarLang.Context;
import nl.Koen02.ScarLang.Type.Function.BaseFunction;
import nl.Koen02.ScarLang.Type.Function.FunctionType;
import nl.Koen02.ScarLang.Type.IntegerType;
import nl.Koen02.ScarLang.Type.Type;

import java.util.ArrayList;
import java.util.Collections;

public final class PrintFunction extends BaseFunction {
    private static PrintFunction INSTANCE;
    private final ArrayList<String> argNames = new ArrayList<>();

    private PrintFunction(String name) {
        super(name);
        Collections.addAll(argNames, "value");
    }

    public Type execute(ArrayList<Type> args) throws Exception {
        Context execContext = genNewContext();
        checkAndPopulate(argNames, args, execContext);

        System.out.println(execContext.symbolTable.get("value").get());
        return IntegerType.zero;
    }

    public BaseFunction getCopy() {
        PrintFunction copy = new PrintFunction(name);
        copy.setContext(context);
        copy.setPos(posStart, posEnd);
        return copy;
    }

    public static PrintFunction getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PrintFunction("print");
        }
        return INSTANCE;
    }
}
