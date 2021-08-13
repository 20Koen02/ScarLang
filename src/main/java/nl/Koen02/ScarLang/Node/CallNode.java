package nl.Koen02.ScarLang.Node;

import nl.Koen02.ScarLang.Context;
import nl.Koen02.ScarLang.RunTimeResult;
import nl.Koen02.ScarLang.Type.Type;

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

    public RunTimeResult visit(Context context) throws Exception {
        RunTimeResult res = new RunTimeResult();

        ArrayList<Type> args = new ArrayList<>();
        Type valueToCall = res.register(nodeToCall.visit(context));
        if (res.shouldReturn()) return res;
        valueToCall = valueToCall.getCopy().setPos(posStart, posEnd);

        for (Node argNode : argNodes) {
            args.add(res.register(argNode.visit(context)));
            if (res.shouldReturn()) return res;
        }

        Type ret = res.register(valueToCall.execute(args));
        if (res.shouldReturn()) return res;
        ret = ret.getCopy().setPos(posStart, posEnd).setContext(context);

        return res.success(ret);
    }
}
