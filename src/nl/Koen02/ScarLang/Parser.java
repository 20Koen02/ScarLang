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

    public Node factor() throws InvalidSyntaxError {
        Token tok = curTok;

        switch (tok.type) {
            case TT_PLUS, TT_MIN -> {
                advance();
                Node factor = factor();
                return new UnaryOpNode(tok, factor);
            }
            case TT_INT, TT_FLOAT -> {
                advance();
                return new NumberNode(tok);
            }
            case TT_LPAR -> {
                advance();
                Node expr = expr();
                if (curTok.type.equals(TT_RPAR)) {
                    advance();
                    return expr;
                } else {
                    throw new InvalidSyntaxError(tok.posStart, tok.posEnd, "Expected ')'");
                }
            }
        }
        throw new InvalidSyntaxError(tok.posStart, tok.posEnd, "Expected int or float");
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
