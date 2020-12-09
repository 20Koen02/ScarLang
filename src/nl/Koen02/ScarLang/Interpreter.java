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
        switch (node.opTok.type) {
            case TT_PLUS -> number = left.addedTo(right);
            case TT_MIN -> number = left.subtractedBy(right);
            case TT_MUL -> number = left.multipliedBy(right);
            case TT_DIV -> number = left.dividedBy(right);
            case TT_POW -> number = left.poweredBy(right);
        }

        if (number == null) return null;
        return number.setPos(node.posStart, node.posEnd);
    }

    public Number visitUnaryOpNode(UnaryOpNode node, Context context) throws RunTimeError {
        Number number = visit(node.node, context);
        if (node.opTok.type.equals(TT_MIN)) {
            number = number.multipliedBy(new Number((double) -1));
        }
        return number.setPos(node.posStart, node.posEnd);
    }
}
