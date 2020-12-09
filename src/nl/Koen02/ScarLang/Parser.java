package nl.Koen02.ScarLang;

import nl.Koen02.ScarLang.Node.IfNode;
import nl.Koen02.ScarLang.Error.InvalidSyntaxError;
import nl.Koen02.ScarLang.Node.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static nl.Koen02.ScarLang.TokenTypes.*;

public class Parser {
    ArrayList<Token> tokens;
    Token curTok;
    Integer tokIdx;

    public Parser(ArrayList<Token> tokens) {
        this.tokens = tokens;
        curTok = null;
        tokIdx = -1;
        advance(null);
    }

    public ParseResult advance(ParseResult res) {
        tokIdx++;
        if (tokIdx < tokens.size()) {
            curTok = tokens.get(tokIdx);
        }
        if (res == null) return new ParseResult();
        return res;
    }

    public ParseResult parse() throws InvalidSyntaxError, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        ParseResult res = expr();
        if (res.error == null && !curTok.type.equals(TT_EOF)) {
            return res.failure(new InvalidSyntaxError(curTok.posStart, curTok.posEnd, "Expected '+', '-', '*', '/', '^', '==', '!=', '<', '>', <=', '>=', 'AND' or 'OR'"));
        }
        return res;
    }

    public ParseResult ifExpr() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        ParseResult res = new ParseResult();
        ArrayList<ArrayList<Node>> cases = new ArrayList<>();
        Node elseCase = null;

        if (!curTok.matches(TT_KEYWORD, "if"))
            return res.failure(new InvalidSyntaxError(curTok.posStart, curTok.posEnd, "Expected 'if'"));

        do {
            res = advance(res);

            Node condition = res.register(expr());
            if (res.error != null) return res;

            if (!curTok.matches(TT_KEYWORD, "then"))
                return res.failure(new InvalidSyntaxError(curTok.posStart, curTok.posEnd, "Expected 'then'"));
            res = advance(res);

            Node expr = res.register(expr());
            if (res.error != null) return res;

            ArrayList<Node> conditionExpressionList = new ArrayList<>();
            conditionExpressionList.add(condition);
            conditionExpressionList.add(expr);
            cases.add(conditionExpressionList);
        } while (curTok.matches(TT_KEYWORD, "elif"));

        if (curTok.matches(TT_KEYWORD, "else")) {
            res = advance(res);
            elseCase = res.register(expr());
            if (res.error != null) return res;
        }

        return res.success(new IfNode(cases, elseCase));
    }

    public ParseResult forExpr() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        ParseResult res = new ParseResult();

        if (!curTok.matches(TT_KEYWORD, "for"))
            return res.failure(new InvalidSyntaxError(curTok.posStart, curTok.posEnd, "Expected 'for'"));

        res = advance(res);

        if (!curTok.type.equals(TT_IDENTIFIER))
            return res.failure(new InvalidSyntaxError(curTok.posStart, curTok.posEnd, "Expected identifier"));

        Token var_name = curTok;

        res = advance(res);

        if (!curTok.type.equals(TT_EQ))
            return res.failure(new InvalidSyntaxError(curTok.posStart, curTok.posEnd, "Expected '='"));

        res = advance(res);

        Node startValue = res.register(expr());
        if (res.error != null) return res;

        if (!curTok.matches(TT_KEYWORD, "to"))
            return res.failure(new InvalidSyntaxError(curTok.posStart, curTok.posEnd, "Expected 'to'"));

        res = advance(res);

        Node endValue = res.register(expr());
        if (res.error != null) return res;

        Node stepValue = null;
        if (curTok.matches(TT_KEYWORD, "step")) {
            res = advance(res);
            stepValue = res.register(expr());
            if (res.error != null) return res;
        }

        if (!curTok.matches(TT_KEYWORD, "then"))
            return res.failure(new InvalidSyntaxError(curTok.posStart, curTok.posEnd, "Expected 'then'"));

        res = advance(res);

        Node body = res.register(expr());
        if (res.error != null) return res;

        return res.success(new ForNode(var_name, startValue, endValue, stepValue, body));
    }

    public ParseResult whileExpr() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        ParseResult res = new ParseResult();
        if (!curTok.matches(TT_KEYWORD, "while"))
            return res.failure(new InvalidSyntaxError(curTok.posStart, curTok.posEnd, "Expected 'while'"));

        res = advance(res);
        Node condition = res.register(expr());
        if (res.error != null) return res;

        if (!curTok.matches(TT_KEYWORD, "then"))
            return res.failure(new InvalidSyntaxError(curTok.posStart, curTok.posEnd, "Expected 'then'"));

        res = advance(res);
        Node body = res.register(expr());
        if (res.error != null) return res;

        return res.success(new WhileNode(condition, body));
    }

    public ParseResult atom() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        ParseResult res = new ParseResult();
        Token tok = curTok;

        if (TT_INT.equals(tok.type) || TT_FLOAT.equals(tok.type)) {
            res = advance(res);
            return res.success(new NumberNode(tok));
        } else if (TT_IDENTIFIER.equals(tok.type)) {
            res = advance(res);
            return res.success(new VarAccessNode(tok));
        } else if (TT_LPAR.equals(tok.type)) {
            res = advance(res);
            Node expr = res.register(expr());
            if (res.error != null) return res;
            if (curTok.type.equals(TT_RPAR)) {
                res = advance(res);
                return res.success(expr);
            } else {
                return res.failure(new InvalidSyntaxError(tok.posStart, tok.posEnd, "Expected ')'"));
            }
        } else if (tok.matches(TT_KEYWORD, "if")) {
            Node ifExpr = res.register(ifExpr());
            if (res.error != null) return res;
            return res.success(ifExpr);
        } else if (tok.matches(TT_KEYWORD, "for")) {
            Node forExpr = res.register(forExpr());
            if (res.error != null) return res;
            return res.success(forExpr);
        } else if (tok.matches(TT_KEYWORD, "while")) {
            Node whileExpr = res.register(whileExpr());
            if (res.error != null) return res;
            return res.success(whileExpr);
        }

        return res.failure(new InvalidSyntaxError(curTok.posStart, curTok.posEnd, "Expected int, float, identifier, '+', '-' or '('"));
    }

    public ParseResult power() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String[] binOps = {TT_POW};
        return binOp(this.getClass().getMethod("atom"), null, Arrays.asList(binOps));
    }

    public ParseResult factor() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        ParseResult res = new ParseResult();
        Token tok = curTok;

        if (TT_PLUS.equals(tok.type) || TT_MIN.equals(tok.type)) {
            res = advance(res);
            Node factor = res.register(factor());
            if (res.error != null) return res;
            return res.success(new UnaryOpNode(tok, factor));
        }
        return power();
    }

    public ParseResult term() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String[] binOps = {TT_MUL, TT_DIV};
        return binOp(this.getClass().getMethod("factor"), null, Arrays.asList(binOps));
    }

    public ParseResult arithExpr() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String[] binOps = {TT_PLUS, TT_MIN};
        return binOp(this.getClass().getMethod("term"), null, Arrays.asList(binOps));
    }

    public ParseResult compExpr() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        ParseResult res = new ParseResult();
        if (curTok.matches(TT_KEYWORD, "not")) {
            Token opTok = curTok;
            res = advance(res);
            Node node = res.register(compExpr());
            if (res.error != null) return res;
            return res.success(new UnaryOpNode(opTok, node));
        }

        String[] binOps = {TT_EE, TT_NE, TT_LT, TT_GT, TT_LTE, TT_GTE};
        Node node = res.register(binOp(this.getClass().getMethod("arithExpr"), null, Arrays.asList(binOps)));
        if (res.error != null) {
            return res.failure(new InvalidSyntaxError(curTok.posStart, curTok.posEnd, "Expected int, float, identifier, '+', '-', '(' or 'not'"));
        }
        return res.success(node);
    }

    public ParseResult expr() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        ParseResult res = new ParseResult();

        if (curTok.matches(TT_KEYWORD, "var")) {
            res = advance(res);
            if (!curTok.type.equals(TT_IDENTIFIER))
                return res.failure(new InvalidSyntaxError(curTok.posStart, curTok.posEnd, "Expected identifier"));
            Token varName = curTok;
            res = advance(res);
            if (!curTok.type.equals(TT_EQ))
                return res.failure(new InvalidSyntaxError(curTok.posStart, curTok.posEnd, "Expected '='"));
            res = advance(res);
            Node expr = res.register(expr());
            if (res.error != null) return res;
            return res.success(new VarAssignNode(varName, expr));
        }

        Node node = res.register(binOpExpr());

        if (res.error != null) {
            return res.failure(new InvalidSyntaxError(curTok.posStart, curTok.posEnd, "Expected 'var', int, float, identifier, '+', '-' or '('"));
        }

        return res.success(node);
    }

    public ParseResult binOpExpr() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        ParseResult res = new ParseResult();
        Node left = res.register(compExpr());
        if (res.error != null) return res;
        while (curTok.type.equals(TT_KEYWORD) && (curTok.value.equals("and") || curTok.value.equals("or"))) {
            Token opTok = curTok;
            res = advance(res);
            Node right = res.register(compExpr());
            if (res.error != null) return res;
            left = new BinOpNode(left, opTok, right);
        }
        return res.success(left);
    }

    public ParseResult binOp(Method method, Method method2, List<String> ops) throws InvocationTargetException, IllegalAccessException {
        if (method2 == null) method2 = method;
        ParseResult res = new ParseResult();
        Node left = res.register((ParseResult) method.invoke(this));
        if (res.error != null) return res;
        while (ops.contains(curTok.type)) {
            Token opTok = curTok;
            res = advance(res);
            Node right = res.register((ParseResult) method2.invoke(this));
            if (res.error != null) return res;
            left = new BinOpNode(left, opTok, right);
        }
        return res.success(left);
    }
}
