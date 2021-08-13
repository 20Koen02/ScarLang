package nl.Koen02.ScarLang.Node;

import nl.Koen02.ScarLang.Position;

public class BreakNode extends Node {
    public BreakNode(Position posStart, Position posEnd) {
        this.posStart = posStart;
        this.posEnd = posEnd;
    }
}
