package nl.Koen02.ScarLang;

import nl.Koen02.ScarLang.Error.InvalidSyntaxError;
import nl.Koen02.ScarLang.Node.*;

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

    public ParseResult parse() throws InvalidSyntaxError {
        ParseResult res = expr();
        if (res.error == null && !curTok.type.equals(TT_EOF)) {
            return res.failure(new InvalidSyntaxError(curTok.posStart, curTok.posEnd, "Expected '+', '-', '*' or '/'"));
        }
        return res;
    }

    public ParseResult atom() throws InvalidSyntaxError {
        ParseResult res = new ParseResult();
        Token tok = curTok;

        if (TT_INT.equals(tok.type) || TT_FLOAT.equals(tok.type)) {
            res.regAdvancement();
            advance();
            return res.success(new NumberNode(tok));
        } else if (TT_IDENTIFIER.equals(tok.type)) {
            res.regAdvancement();
            advance();
            return res.success(new VarAccessNode(tok));
        } else if (TT_LPAR.equals(tok.type)) {
            res.regAdvancement();
            advance();
            Node expr = res.register(expr());
            if (res.error != null) return res;
            if (curTok.type.equals(TT_RPAR)) {
                res.regAdvancement();
                advance();
                return res.success(expr);
            } else {
                return res.failure(new InvalidSyntaxError(tok.posStart, tok.posEnd, "Expected ')'"));
            }
        }
        return res.failure(new InvalidSyntaxError(curTok.posStart, curTok.posEnd, "Expected int, float, identifier, '+', '-' or '('"));
    }

    public ParseResult power() throws InvalidSyntaxError {
        ParseResult res = new ParseResult();
        Node left = res.register(atom());
        if (res.error != null) return res;
        while (curTok.type.equals(TT_POW)) {
            Token opTok = curTok;
            res.regAdvancement();
            advance();
            Node right = res.register(factor());
            if (res.error != null) return res;
            left = new BinOpNode(left, opTok, right);
        }
        return res.success(left);
    }

    public ParseResult factor() throws InvalidSyntaxError {
        ParseResult res = new ParseResult();
        Token tok = curTok;

        if (TT_PLUS.equals(tok.type) || TT_MIN.equals(tok.type)) {
            res.regAdvancement();
            advance();
            Node factor = res.register(factor());
            if (res.error != null) return res;
            return res.success(new UnaryOpNode(tok, factor));
        }
        return power();
    }

    public ParseResult term() throws InvalidSyntaxError {
        ParseResult res = new ParseResult();
        Node left = res.register(factor());
        if (res.error != null) return res;
        while (curTok.type.equals(TT_MUL) || curTok.type.equals(TT_DIV)) {
            Token opTok = curTok;
            res.regAdvancement();
            advance();
            Node right = res.register(factor());
            if (res.error != null) return res;
            left = new BinOpNode(left, opTok, right);
        }
        return res.success(left);
    }

    public ParseResult expr() throws InvalidSyntaxError {
        ParseResult res = new ParseResult();

        if (curTok.matches(TT_KEYWORD, "var")) {
            res.regAdvancement();
            advance();
            if (!curTok.type.equals(TT_IDENTIFIER)) return res.failure(new InvalidSyntaxError(curTok.posStart, curTok.posEnd, "Expected identifier"));
            Token varName = curTok;
            res.regAdvancement();
            advance();
            if (!curTok.type.equals(TT_EQ)) return res.failure(new InvalidSyntaxError(curTok.posStart, curTok.posEnd, "Expected '='"));
            res.regAdvancement();
            advance();
            Node expr = res.register(expr());
            if (res.error != null) return res;
            return res.success(new VarAssignNode(varName, expr));
        }

        Node node = res.register(binOp());

        if (res.error != null) {
            return res.failure(new InvalidSyntaxError(curTok.posStart, curTok.posEnd, "Expected 'VAR', int, float, identifier, '+', '-' or '('"));
        }

        return res.success(node);
    }

    public ParseResult binOp() throws InvalidSyntaxError {
        ParseResult res = new ParseResult();
        Node left = res.register(term());
        if (res.error != null) return res;
        while (curTok.type.equals(TT_PLUS) || curTok.type.equals(TT_MIN)) {
            Token opTok = curTok;
            res.regAdvancement();
            advance();
            Node right = res.register(term());
            if (res.error != null) return res;
            left = new BinOpNode(left, opTok, right);
        }
        return res.success(left);
    }
}
