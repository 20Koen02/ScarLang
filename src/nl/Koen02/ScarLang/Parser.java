package nl.Koen02.ScarLang;

import nl.Koen02.ScarLang.Error.InvalidSyntaxError;
import nl.Koen02.ScarLang.Node.BinOpNode;
import nl.Koen02.ScarLang.Node.Node;
import nl.Koen02.ScarLang.Node.NumberNode;
import nl.Koen02.ScarLang.Node.UnaryOpNode;

import java.util.ArrayList;

import static nl.Koen02.ScarLang.TokenTypes.*;

public class Parser {
    ArrayList<Token> tokens;
    Token curTok;
    Integer tokIdx;

    public Parser(ArrayList<Token> tokens) {
        this.tokens = tokens;
        curTok = null;
        tokIdx = -1;
        advance();
    }

    private Token advance() {
        tokIdx++;
        if (tokIdx < tokens.size()) {
            curTok = tokens.get(tokIdx);
        }
        return curTok;
    }

    public Node parse() throws InvalidSyntaxError {
        Node res = expr();
        if (!curTok.type.equals(TT_EOF)) {
            throw new InvalidSyntaxError(curTok.posStart, curTok.posEnd, "Expected '+', '-', '*' or '/'");
        }
        return res;
    }

    public Node atom() throws InvalidSyntaxError {
        Token tok = curTok;

        if (TT_INT.equals(tok.type) || TT_FLOAT.equals(tok.type)) {
            advance();
            return new NumberNode(tok);
        } else if (TT_LPAR.equals(tok.type)) {
            advance();
            Node expr = expr();
            if (curTok.type.equals(TT_RPAR)) {
                advance();
                return expr;
            } else {
                throw new InvalidSyntaxError(tok.posStart, tok.posEnd, "Expected ')'");
            }
        }
        throw new InvalidSyntaxError(curTok.posStart, curTok.posEnd, "Expected int, float, '+', '-' or '('");
    }

    public Node power() throws InvalidSyntaxError {
        Node left = atom();
        while (curTok.type.equals(TT_POW)) {
            Token opTok = curTok;
            advance();
            Node right = factor();
            left = new BinOpNode(left, opTok, right);
        }
        return left;
    }

    public Node factor() throws InvalidSyntaxError {
        Token tok = curTok;

        if (TT_PLUS.equals(tok.type) || TT_MIN.equals(tok.type)) {
            advance();
            Node factor = factor();
            return new UnaryOpNode(tok, factor);
        }
        return power();
    }

    public Node term() throws InvalidSyntaxError {
        Node left = factor();
        while (curTok.type.equals(TT_MUL) || curTok.type.equals(TT_DIV)) {
            Token opTok = curTok;
            advance();
            Node right = factor();
            left = new BinOpNode(left, opTok, right);
        }
        return left;
    }

    public Node expr() throws InvalidSyntaxError {
        Node left = term();
        while (curTok.type.equals(TT_PLUS) || curTok.type.equals(TT_MIN)) {
            Token opTok = curTok;
            advance();
            Node right = term();
            left = new BinOpNode(left, opTok, right);
        }
        return left;
    }
}
