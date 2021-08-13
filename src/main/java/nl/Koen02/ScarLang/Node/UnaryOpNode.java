package nl.Koen02.ScarLang.Node;

import nl.Koen02.ScarLang.Context;
import nl.Koen02.ScarLang.Error.RunTimeError;
import nl.Koen02.ScarLang.RunTimeResult;
import nl.Koen02.ScarLang.Token;
import nl.Koen02.ScarLang.Type.Type;

import static nl.Koen02.ScarLang.TokenTypes.TT_KEYWORD;
import static nl.Koen02.ScarLang.TokenTypes.TT_MIN;

public class UnaryOpNode extends Node {
    public Token opTok;
    public Node node;

    public UnaryOpNode(Token opTok, Node node) {
        this.opTok = opTok;
        this.node = node;

        posStart = opTok.posStart;
        posEnd = node.posEnd;
    }

    public RunTimeResult visit(Context context) throws Exception {
        RunTimeResult res = new RunTimeResult();

        Type number = res.register(node.visit(context));
        if (res.shouldReturn()) return res;
        if (opTok.type.equals(TT_MIN)) {
            number = number.multipliedByMinOne();
        } else if (opTok.matches(TT_KEYWORD, "not")) {
            number = number.notOperated();
        }
        return res.success(number.setPos(node.posStart, node.posEnd));
    }

    public String get() {
        return String.format("(%s, %s)", opTok.get(), node.get());
    }
}
