package nl.Koen02.ScarLang.Node;

import nl.Koen02.ScarLang.Context;
import nl.Koen02.ScarLang.Position;
import nl.Koen02.ScarLang.RunTimeResult;
import nl.Koen02.ScarLang.Type.ArrayType;
import nl.Koen02.ScarLang.Type.Type;

import java.util.ArrayList;

public class ArrayNode extends Node {
    public ArrayList<Node> elementNodes;

    public ArrayNode(ArrayList<Node> elementNodes, Position posStart, Position posEnd) {
        this.elementNodes = elementNodes;
        this.posStart = posStart;
        this.posEnd = posEnd;
    }

    public RunTimeResult visit(Context context) throws Exception {
        RunTimeResult res = new RunTimeResult();
        ArrayList<Type> elements = new ArrayList<>();
        for (Node elementNode : elementNodes) {
            elements.add(res.register(elementNode.visit(context)));
            if (res.shouldReturn()) return res;
        }
        return res.success(new ArrayType(elements).setContext(context).setPos(posStart, posEnd));
    }
}
