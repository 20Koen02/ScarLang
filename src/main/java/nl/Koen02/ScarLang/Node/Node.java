package nl.Koen02.ScarLang.Node;

import nl.Koen02.ScarLang.Context;
import nl.Koen02.ScarLang.Error.RunTimeError;
import nl.Koen02.ScarLang.Position;
import nl.Koen02.ScarLang.RunTimeResult;
import nl.Koen02.ScarLang.Type.Type;

public class Node {
    public Position posStart, posEnd;

    public Node() {}

    public RunTimeResult visit(Context context) throws Exception {
        return new RunTimeResult().success(new Type().setContext(context).setPos(posStart, posEnd));
    }

    public String get() {
        return "";
    }
}
