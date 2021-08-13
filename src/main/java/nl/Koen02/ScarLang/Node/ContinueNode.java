package nl.Koen02.ScarLang.Node;

import nl.Koen02.ScarLang.Context;
import nl.Koen02.ScarLang.Position;
import nl.Koen02.ScarLang.RunTimeResult;

public class ContinueNode extends Node {
    public ContinueNode(Position posStart, Position posEnd) {
        this.posStart = posStart;
        this.posEnd = posEnd;
    }

    public RunTimeResult visit(Context context) throws Exception {
        return new RunTimeResult().successContinue();
    }
}
