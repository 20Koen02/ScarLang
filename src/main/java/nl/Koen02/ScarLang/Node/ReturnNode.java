package nl.Koen02.ScarLang.Node;

import nl.Koen02.ScarLang.Position;

public class ReturnNode extends Node {
    public Node nodeToReturn;

    public ReturnNode(Node nodeToReturn, Position posStart, Position posEnd) {
        this.nodeToReturn = nodeToReturn;
        this.posStart = posStart;
        this.posEnd = posEnd;
    }
}
