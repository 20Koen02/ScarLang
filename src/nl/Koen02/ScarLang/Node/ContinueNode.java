package nl.Koen02.ScarLang.Node;

import nl.Koen02.ScarLang.Position;

public class ContinueNode extends Node {
    public ContinueNode(Position posStart, Position posEnd) {
        this.posStart = posStart;
        this.posEnd = posEnd;
    }
}
