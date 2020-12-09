package nl.Koen02.ScarLang.Node;

import nl.Koen02.ScarLang.Node.Node;

import java.util.ArrayList;

public class IfNode extends Node {
    public ArrayList<ArrayList<Node>> cases;
    public Node elseCase;

    public IfNode(ArrayList<ArrayList<Node>> cases, Node elseCase) {
        this.cases = cases;
        this.elseCase = elseCase;

        posStart = cases.get(0).get(0).posStart;
        posEnd = elseCase != null ? elseCase.posEnd : cases.get(cases.size() - 1).get(0).posEnd;
    }
}
