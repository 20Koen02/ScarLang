package nl.Koen02.ScarLang.Node;

import java.util.ArrayList;

public class CallNode extends Node {
    public Node nodeToCall;
    public ArrayList<Node> argNodes;

    public CallNode(Node nodeToCall, ArrayList<Node> argNodes) {
        this.nodeToCall = nodeToCall;
        this.argNodes = argNodes;

        posStart = nodeToCall.posStart;
        posEnd = argNodes.size() > 0 ? argNodes.get(argNodes.size() - 1).posEnd : nodeToCall.posEnd;
    }
}
