package nl.Koen02.ScarLang.Type.Function.Stdlib;

import nl.Koen02.ScarLang.Context;
import nl.Koen02.ScarLang.Error.Error;
import nl.Koen02.ScarLang.Error.RunTimeError;
import nl.Koen02.ScarLang.Run;
import nl.Koen02.ScarLang.RunTimeResult;
import nl.Koen02.ScarLang.Type.ArrayType;
import nl.Koen02.ScarLang.Type.Function.BaseFunction;
import nl.Koen02.ScarLang.Type.IntegerType;
import nl.Koen02.ScarLang.Type.StringType;
import nl.Koen02.ScarLang.Type.Type;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import static nl.Koen02.ScarLang.Run.run;

public final class RunFunction extends BaseFunction {
    private static RunFunction INSTANCE;
    private final ArrayList<String> argNames = new ArrayList<>();

    private RunFunction(String name) {
        super(name);
        Collections.addAll(argNames, "file");
    }

    public RunTimeResult execute(ArrayList<Type> args) throws Exception {
        Context execContext = genNewContext();
        checkAndPopulate(argNames, args, execContext);

        Type file = execContext.symbolTable.get("file");

        if (!(file instanceof StringType))
            throw new RunTimeError(posStart, posEnd, "First argument must be of type string", execContext);

        String fileName = ((StringType) file).value;

        String script;
        try {
            script = new Scanner(new File(fileName)).useDelimiter("\\Z").next();
            script = script.replaceAll("\r\n", "\n").replaceAll("\r", "\n");
        } catch (Exception e) {
            throw new RunTimeError(posStart, posEnd, String.format("Failed to load script '%s'", fileName), context);
        }

        try {
            run(fileName, script);
        } catch (Error e) {
            System.out.println(e.getError());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new RunTimeResult().success(IntegerType.zero);
    }

    public BaseFunction getCopy() {
        RunFunction copy = new RunFunction(name);
        copy.setContext(context);
        copy.setPos(posStart, posEnd);
        return copy;
    }

    public static RunFunction getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RunFunction("run");
        }
        return INSTANCE;
    }
}
