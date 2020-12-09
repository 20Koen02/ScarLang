package nl.Koen02.ScarLang;

import nl.Koen02.ScarLang.Error.RunTimeError;
import nl.Koen02.ScarLang.Node.*;

import static nl.Koen02.ScarLang.TokenTypes.*;

public class Interpreter {
    public Number visit(Node node, Context context) throws RunTimeError {
        if (node instanceof NumberNode) {
            return visitNumberNode((NumberNode) node, context);
        } else if (node instanceof BinOpNode) {
            return visitBinOpNode((BinOpNode) node, context);
        } else if (node instanceof UnaryOpNode) {
            return visitUnaryOpNode((UnaryOpNode) node, context);
        } else if (node instanceof VarAccessNode) {
            return visitVarAccessNode((VarAccessNode) node, context);
        } else if (node instanceof VarAssignNode) {
            return visitVarAssignNode((VarAssignNode) node, context);
        }
        return null;
    }

    public Number visitVarAccessNode(VarAccessNode node, Context context) throws RunTimeError {
        String varName = node.varNameToken.value;
        Number value = context.symbolTable.get(varName);

        if (value == null) throw new RunTimeError(node.posStart, node.posEnd, String.format("'%s' is not defined", varName), context);

        value = value.getCopy().setPos(node.posStart, node.posEnd);
        return value;
    }

    public Number visitVarAssignNode(VarAssignNode node, Context context) throws RunTimeError {
        String varName = node.varNameToken.value;
        Number value = visit(node.valueNode, context);

        context.symbolTable.set(varName, value);
        return value;
    }

    public Number visitNumberNode(NumberNode node, Context context) {
        return new Number(Double.parseDouble(node.tok.value)).setContext(context).setPos(node.posStart, node.posEnd);
    }

    public Number visitBinOpNode(BinOpNode node, Context context) throws RunTimeError {
        Number left = visit(node.leftNode, context);
        Number right = visit(node.rightNode, context);

        Number number = null;
        if (TT_PLUS.equals(node.opTok.type)) {
            number = left.addedTo(right);
        } else if (TT_MIN.equals(node.opTok.type)) {
            number = left.subtractedBy(right);
        } else if (TT_MUL.equals(node.opTok.type)) {
            number = left.multipliedBy(right);
        } else if (TT_DIV.equals(node.opTok.type)) {
            number = left.dividedBy(right);
        } else if (TT_POW.equals(node.opTok.type)) {
            number = left.poweredBy(right);
        } else if (TT_EE.equals(node.opTok.type)) {
            number = left.getComparisonEe(right);
        } else if (TT_NE.equals(node.opTok.type)) {
            number = left.getComparisonNe(right);
        } else if (TT_LT.equals(node.opTok.type)) {
            number = left.getComparisonLt(right);
        } else if (TT_GT.equals(node.opTok.type)) {
            number = left.getComparisonGt(right);
        } else if (TT_LTE.equals(node.opTok.type)) {
            number = left.getComparisonLte(right);
        } else if (TT_GTE.equals(node.opTok.type)) {
            number = left.getComparisonGte(right);
        } else if (node.opTok.matches(TT_KEYWORD, "and")) {
            number = left.andOperated(right);
        } else if (node.opTok.matches(TT_KEYWORD, "or")) {
            number = left.orOperated(right);
        }

        if (number == null) return null;
        return number.setPos(node.posStart, node.posEnd);
    }

    public Number visitUnaryOpNode(UnaryOpNode node, Context context) throws RunTimeError {
        Number number = visit(node.node, context);
        if (node.opTok.type.equals(TT_MIN)) {
            number = number.multipliedBy(new Number((double) -1));
        } else if (node.opTok.matches(TT_KEYWORD, "not")) {
            number = number.notOperated();
        }
        return number.setPos(node.posStart, node.posEnd);
    }
}
