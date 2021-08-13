package nl.Koen02.ScarLang.Node;

import nl.Koen02.ScarLang.Position;

import java.util.ArrayList;

public class ArrayNode extends Node {
    public ArrayList<Node> elementNodes;

    public ArrayNode(ArrayList<Node> elementNodes, Position posStart, Position posEnd) {
        this.elementNodes = elementNodes;
        this.posStart = posStart;
        this.posEnd = posEnd;
    }
}
