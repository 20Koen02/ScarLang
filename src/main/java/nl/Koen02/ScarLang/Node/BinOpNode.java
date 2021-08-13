package nl.Koen02.ScarLang.Node;

import nl.Koen02.ScarLang.Context;
import nl.Koen02.ScarLang.Error.RunTimeError;
import nl.Koen02.ScarLang.RunTimeResult;
import nl.Koen02.ScarLang.Token;
import nl.Koen02.ScarLang.Type.FloatType;
import nl.Koen02.ScarLang.Type.Type;

import static nl.Koen02.ScarLang.TokenTypes.*;
import static nl.Koen02.ScarLang.TokenTypes.TT_KEYWORD;

public class BinOpNode extends Node {
    public Node leftNode, rightNode;
    public Token opTok;

    public BinOpNode(Node leftNode, Token opTok, Node rightNode) {
        this.leftNode = leftNode;
        this.opTok = opTok;
        this.rightNode = rightNode;

        posStart = leftNode.posStart;
        posEnd = rightNode.posEnd;
    }

    public RunTimeResult visit(Context context) throws Exception {
        RunTimeResult res = new RunTimeResult();

        Type left = res.register(leftNode.visit(context));
        if (res.shouldReturn()) return res;
        Type right = res.register(rightNode.visit(context));
        if (res.shouldReturn()) return res;

        Type type = null;
        if (TT_PLUS.equals(opTok.type)) {
            type = left.addedTo(right);
        } else if (TT_MIN.equals(opTok.type)) {
            type = left.subtractedBy(right);
        } else if (TT_MUL.equals(opTok.type)) {
            type = left.multipliedBy(right);
        } else if (TT_MOD.equals(opTok.type)) {
            type = left.moduloBy(right);
        } else if (TT_DIV.equals(opTok.type)) {
            type = left.dividedBy(right);
        } else if (TT_POW.equals(opTok.type)) {
            type = left.poweredBy(right);
        } else if (TT_EE.equals(opTok.type)) {
            type = left.getComparisonEe(right);
        } else if (TT_NE.equals(opTok.type)) {
            type = left.getComparisonNe(right);
        } else if (TT_LT.equals(opTok.type)) {
            type = left.getComparisonLt(right);
        } else if (TT_GT.equals(opTok.type)) {
            type = left.getComparisonGt(right);
        } else if (TT_LTE.equals(opTok.type)) {
            type = left.getComparisonLte(right);
        } else if (TT_GTE.equals(opTok.type)) {
            type = left.getComparisonGte(right);
        } else if (opTok.matches(TT_KEYWORD, "and")) {
            type = left.andOperated(right);
        } else if (opTok.matches(TT_KEYWORD, "or")) {
            type = left.orOperated(right);
        }

        if (type == null) return null;
        return res.success(type.setPos(posStart, posEnd));
    }

    public String get() {
        return String.format("(%s, %s, %s)", leftNode.get(), opTok.get(), rightNode.get());
    }
}
