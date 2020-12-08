package nl.Koen02.ScarLang;

import nl.Koen02.ScarLang.Error.RunTimeError;
import nl.Koen02.ScarLang.Node.BinOpNode;
import nl.Koen02.ScarLang.Node.Node;
import nl.Koen02.ScarLang.Node.NumberNode;
import nl.Koen02.ScarLang.Node.UnaryOpNode;

import static nl.Koen02.ScarLang.TokenTypes.*;

public class Interpreter {
    public Number visit(Node node) throws RunTimeError {
        if (node instanceof NumberNode) {
            return visitNumberNode((NumberNode) node);
        } else if (node instanceof BinOpNode) {
            return visitBinOpNode((BinOpNode) node);
        } else if (node instanceof UnaryOpNode) {
            return visitUnaryOpNode((UnaryOpNode) node);
        }
        return null;
    }

    public Number visitNumberNode(NumberNode node) {
        return new Number(Integer.parseInt(node.tok.value)).setPos(node.posStart, node.posEnd);
    }

    public Number visitBinOpNode(BinOpNode node) throws RunTimeError {
        Number left = visit(node.leftNode);
        Number right = visit(node.rightNode);

        Number number = null;
        switch (node.opTok.type) {
            case TT_PLUS -> number = left.addedTo(right);
            case TT_MIN -> number = left.subtractedBy(right);
            case TT_MUL -> number = left.multipliedBy(right);
            case TT_DIV -> number = left.dividedBy(right);
        }

        if (number == null) return null;
        return number.setPos(node.posStart, node.posEnd);
    }

    public Number visitUnaryOpNode(UnaryOpNode node) throws RunTimeError {
        Number number = visit(node.node);
        if (node.opTok.type.equals(TT_MIN)) {
            number = number.multipliedBy(new Number(-1));
        }
        return number.setPos(node.posStart, node.posEnd);
    }
}
