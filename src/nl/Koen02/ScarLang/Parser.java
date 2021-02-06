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
        updateCurTok();
        if (res == null) return null;
        res.regAdvancement();
        return res;
    }

    public void reverse(int amount) {
        tokIdx -= amount;
        updateCurTok();
    }

    public void updateCurTok() {
        if (tokIdx >= 0 && tokIdx < tokens.size()) {
            curTok = tokens.get(tokIdx);
        }
    }

    public ParseResult parse() throws InvalidSyntaxError, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        ParseResult res = statements();
        if (res.error == null && !curTok.type.equals(TT_EOF)) {
            return res.failure(res.statementError);
        }
        return res;
    }

    public ParseResult statements() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        ParseResult res = new ParseResult();
        ArrayList<Node> statements = new ArrayList<>();
        Position posStart = curTok.posStart.getCopy();

        while (curTok.type.equals(TT_NEWLN)) {
            res = advance(res);
        }
        Node statement = res.register(statement());
        if (res.error != null) return res;
        statements.add(statement);


        boolean moreStatements = true;
        while (true) {
            int newlines = 0;
            while (curTok.type.equals(TT_NEWLN)) {
                res = advance(res);
                newlines++;
            }
            if (newlines == 0) moreStatements = false;
            if (!moreStatements) break;

            ParseResult singleStatement = statement();
            if (singleStatement.error != null) res.statementError = singleStatement.error;
            statement = res.tryRegister(singleStatement);
            if (statement == null) {
                reverse(res.toReverseCount);
                moreStatements = false;
            } else {
                statements.add(statement);
            }
        }

        return res.success(new ArrayNode(statements, posStart, curTok.posEnd.getCopy()));
    }

    public ParseResult statement() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        ParseResult res = new ParseResult();
        Position posStart = curTok.posStart.getCopy();
        if (curTok.matches(TT_KEYWORD, "return")) {
            res = advance(res);
            Node expr = res.tryRegister(expr());
            if (expr == null) reverse(res.toReverseCount);
            return res.success(new ReturnNode(expr, posStart, curTok.posStart.getCopy()));
        }
        if (curTok.matches(TT_KEYWORD, "continue")) {
            res = advance(res);
            return res.success(new ContinueNode(posStart, curTok.posStart.getCopy()));
        }
        if (curTok.matches(TT_KEYWORD, "break")) {
            res = advance(res);
            return res.success(new BreakNode(posStart, curTok.posStart.getCopy()));
        }
        Node expr = res.register(expr());
        if (res.error != null)
            return res.failure(new InvalidSyntaxError(curTok.posStart, curTok.posEnd,
                    "Expected 'return', 'continue', 'break', 'var', 'if', 'for', 'while', 'func', int, float, identifier, '+', '-', '(', '[' or 'not'"));
        return res.success(expr);
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
            return res.failure(new InvalidSyntaxError(curTok.posStart, curTok.posEnd, "Expected 'var', 'if', 'for', 'while', 'func', int, float, identifier, '+', '-', '(', '[' or 'not'"));
        }

        return res.success(node);
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
            return res.failure(new InvalidSyntaxError(curTok.posStart, curTok.posEnd, "Expected int, float, identifier, '+', '-', '(', '[' or 'not'"));
        }
        return res.success(node);
    }

    public ParseResult arithExpr() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String[] binOps = {TT_PLUS, TT_MIN};
        return binOp(this.getClass().getMethod("term"), null, Arrays.asList(binOps));
    }

    public ParseResult term() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String[] binOps = {TT_MUL, TT_DIV};
        return binOp(this.getClass().getMethod("factor"), null, Arrays.asList(binOps));
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

    public ParseResult power() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String[] binOps = {TT_POW};
        return binOp(this.getClass().getMethod("call"), null, Arrays.asList(binOps));
    }

    public ParseResult call() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        ParseResult res = new ParseResult();
        Node atom = res.register(atom());
        if (res.error != null) return res;

        if (curTok.type.equals(TT_LPAR)) {
            res = advance(res);
            ArrayList<Node> argNodes = new ArrayList<>();
            if (curTok.type.equals(TT_RPAR)) {
                res = advance(res);
            } else {
                argNodes.add(res.register(expr()));
                if (res.error != null)
                    return res.failure(new InvalidSyntaxError(curTok.posStart, curTok.posEnd,
                            "Expected ')', 'var', 'if', 'for', 'while', 'fun', int, float, identifier, '+', '-', '(', '[' or 'not'"));

                while (curTok.type.equals(TT_COMMA)) {
                    res = advance(res);
                    argNodes.add(res.register(expr()));
                    if (res.error != null) return res;
                }

                if (!curTok.type.equals(TT_RPAR))
                    return res.failure(new InvalidSyntaxError(curTok.posStart, curTok.posEnd, "Expected ',' or ')'"));

                res = advance(res);
            }
            return res.success(new CallNode(atom, argNodes));
        }
        return res.success(atom);
    }

    public ParseResult atom() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        ParseResult res = new ParseResult();
        Token tok = curTok;

        if (TT_INT.equals(tok.type)) {
            res = advance(res);
            return res.success(new IntegerNode(tok));
        } else if (TT_FLOAT.equals(tok.type)) {
            res = advance(res);
            return res.success(new FloatNode(tok));
        } else if (TT_STRING.equals(tok.type)) {
            res = advance(res);
            return res.success(new StringNode(tok));
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
        } else if (TT_LSQUARE.equals(tok.type)) {
            Node listExpr = res.register(listExpr());
            if (res.error != null) return res;
            return res.success(listExpr);
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
        } else if (tok.matches(TT_KEYWORD, "func")) {
            Node whileExpr = res.register(funcDef());
            if (res.error != null) return res;
            return res.success(whileExpr);
        }

        return res.failure(new InvalidSyntaxError(curTok.posStart, curTok.posEnd, "Expected int, float, identifier, '+', '-', '(', '[', 'if', 'for', 'while', 'func'"));
    }

    private ParseResult listExpr() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        ParseResult res = new ParseResult();
        ArrayList<Node> elementNodes = new ArrayList<>();
        Position posStart = curTok.posStart.getCopy();

        if (!curTok.type.equals(TT_LSQUARE))
            return res.failure(new InvalidSyntaxError(curTok.posStart, curTok.posEnd, "Expected '['"));

        res = advance(res);

        if (curTok.type.equals(TT_RSQUARE)) {
            res = advance(res);
        } else {
            elementNodes.add(res.register(expr()));
            if (res.error != null)
                return res.failure(new InvalidSyntaxError(curTok.posStart, curTok.posEnd,
                        "Expected ']', 'var', 'if', 'for', 'while', 'func', int, float, identifier, '+', '-', '(', '[' or 'not'"));

            while (curTok.type.equals(TT_COMMA)) {
                res = advance(res);
                elementNodes.add(res.register(expr()));
                if (res.error != null) return res;
            }
            res = advance(res);
        }

        return res.success(new ArrayNode(elementNodes, posStart, curTok.posEnd.getCopy()));
    }

    public ParseResult ifExpr() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        ParseResult res = new ParseResult();
        IfNode allCases = (IfNode) res.register(ifExprCases("if"));
        if (res.error != null) return res;
        return res.success(allCases);
    }

    public ParseResult ifExprCases(String caseKeyword) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        ParseResult res = new ParseResult();
        ArrayList<ArrayList<Node>> cases = new ArrayList<>();
        Node elseCase = null;
        boolean shouldReturnNull = false;

        if (!curTok.matches(TT_KEYWORD, caseKeyword))
            return res.failure(new InvalidSyntaxError(curTok.posStart, curTok.posEnd, String.format("Expected '%s'", caseKeyword)));

        res = advance(res);

        if (!curTok.type.equals(TT_LPAR))
            return res.failure(new InvalidSyntaxError(curTok.posStart, curTok.posEnd, "Expected '('"));

        res = advance(res);

        Node condition = res.register(expr());
        if (res.error != null) return res;

        if (!curTok.type.equals(TT_RPAR))
            return res.failure(new InvalidSyntaxError(curTok.posStart, curTok.posEnd, "Expected ')'"));

        res = advance(res);

        if (curTok.type.equals(TT_ARROW)) {
            res = advance(res);
            Node expr = res.register(statement());
            if (res.error != null) return res;
            ArrayList<Node> conditionExpressionList = new ArrayList<>();
            conditionExpressionList.add(condition);
            conditionExpressionList.add(expr);
            cases.add(conditionExpressionList);
            return res.success(new IfNode(cases, elseCase, shouldReturnNull));
        } else if (curTok.type.equals(TT_LCURL)) {
            res = advance(res);
            res = advance(res);
            ArrayNode statements = (ArrayNode) res.register(statements());
            if (res.error != null) return res;

            ArrayList<Node> conditionExpressionList = new ArrayList<>();
            conditionExpressionList.add(condition);
            conditionExpressionList.add(statements);
            cases.add(conditionExpressionList);
            shouldReturnNull = true;

            if (!curTok.type.equals(TT_RCURL))
                return res.failure(new InvalidSyntaxError(curTok.posStart, curTok.posEnd, "Expected valid expression or '}'"));

            res = advance(res);
        } else {
            return res.failure(new InvalidSyntaxError(curTok.posStart, curTok.posEnd, "Expected '->' or '{'"));
        }

        if (curTok.matches(TT_KEYWORD, "elif")) {

            IfNode allCases = (IfNode) res.register(ifExprCases("elif"));
            if (res.error != null) return res;
            elseCase = allCases.elseCase;
            cases.addAll(allCases.cases);

        } else if (curTok.matches(TT_KEYWORD, "else")) {
            res = advance(res);

            if (!curTok.type.equals(TT_LCURL))
                return res.failure(new InvalidSyntaxError(curTok.posStart, curTok.posEnd, "Expected '{'"));
            res = advance(res);

            if (curTok.type.equals(TT_NEWLN)) res = advance(res);

            ArrayNode statements = (ArrayNode) res.register(statements());
            if (res.error != null) return res;
            elseCase = statements;
            shouldReturnNull = true;

            if (!curTok.type.equals(TT_RCURL))
                return res.failure(new InvalidSyntaxError(curTok.posStart, curTok.posEnd, "Expected '}'"));

            res = advance(res);
        }

        return res.success(new IfNode(cases, elseCase, shouldReturnNull));
    }

    public ParseResult forExpr() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        ParseResult res = new ParseResult();

        if (!curTok.matches(TT_KEYWORD, "for"))
            return res.failure(new InvalidSyntaxError(curTok.posStart, curTok.posEnd, "Expected 'for'"));

        res = advance(res);

        if (!curTok.type.equals(TT_LPAR))
            return res.failure(new InvalidSyntaxError(curTok.posStart, curTok.posEnd, "Expected '('"));

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

        if (!curTok.matches(TT_KEYWORD, "step"))
            return res.failure(new InvalidSyntaxError(curTok.posStart, curTok.posEnd, "Expected 'step'"));

        res = advance(res);
        Node stepValue = res.register(statement());
        if (res.error != null) return res;

        if (!curTok.type.equals(TT_RPAR))
            return res.failure(new InvalidSyntaxError(curTok.posStart, curTok.posEnd, "Expected ')'"));

        res = advance(res);

        boolean shouldReturnNull = false;
        Node body;
        if (curTok.type.equals(TT_ARROW)) {
            res = advance(res);
            body = res.register(expr());
            if (res.error != null) return res;

        } else if (curTok.type.equals(TT_LCURL)) {
            res = advance(res);
            body = res.register(statements());
            if (res.error != null) return res;

            if (!curTok.type.equals(TT_RCURL))
                return res.failure(new InvalidSyntaxError(curTok.posStart, curTok.posEnd, "Expected '}'"));
            res = advance(res);
            shouldReturnNull = true;
        } else {
            return res.failure(new InvalidSyntaxError(curTok.posStart, curTok.posEnd, "Expected '->' or '{'"));
        }

        return res.success(new ForNode(var_name, startValue, endValue, stepValue, body, shouldReturnNull));
    }

    public ParseResult whileExpr() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        ParseResult res = new ParseResult();
        if (!curTok.matches(TT_KEYWORD, "while"))
            return res.failure(new InvalidSyntaxError(curTok.posStart, curTok.posEnd, "Expected 'while'"));
        res = advance(res);

        if (!curTok.type.equals(TT_LPAR))
            return res.failure(new InvalidSyntaxError(curTok.posStart, curTok.posEnd, "Expected '('"));
        res = advance(res);

        Node condition = res.register(expr());
        if (res.error != null) return res;

        if (!curTok.type.equals(TT_RPAR))
            return res.failure(new InvalidSyntaxError(curTok.posStart, curTok.posEnd, "Expected ')'"));

        res = advance(res);

        boolean shouldReturnNull = false;
        Node body;

        if (curTok.type.equals(TT_ARROW)) {
            res = advance(res);
            body = res.register(statement());
            if (res.error != null) return res;
        } else if (curTok.type.equals(TT_LCURL)) {
            res = advance(res);
            body = res.register(statements());
            if (res.error != null) return res;

            if (!curTok.type.equals(TT_RCURL))
                return res.failure(new InvalidSyntaxError(curTok.posStart, curTok.posEnd, "Expected '}'"));
            res = advance(res);
            shouldReturnNull = true;
        } else {
            return res.failure(new InvalidSyntaxError(curTok.posStart, curTok.posEnd, "Expected '->' or '{'"));
        }

        return res.success(new WhileNode(condition, body, shouldReturnNull));
    }

    public ParseResult funcDef() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        ParseResult res = new ParseResult();
        if (!curTok.matches(TT_KEYWORD, "func"))
            return res.failure(new InvalidSyntaxError(curTok.posStart, curTok.posEnd, "Expected 'func'"));

        res = advance(res);

        Token varNameTok;
        if (curTok.type.equals(TT_IDENTIFIER)) {
            varNameTok = curTok;
            res = advance(res);
            if (!curTok.type.equals(TT_LPAR))
                return res.failure(new InvalidSyntaxError(curTok.posStart, curTok.posEnd, "Expected '('"));
        } else {
            varNameTok = null;
            if (!curTok.type.equals(TT_LPAR))
                return res.failure(new InvalidSyntaxError(curTok.posStart, curTok.posEnd, "Expected identifier or '('"));
        }

        res = advance(res);
        ArrayList<Token> argNameToks = new ArrayList<>();
        if (curTok.type.equals(TT_IDENTIFIER)) {
            argNameToks.add(curTok);
            res = advance(res);

            while (curTok.type.equals(TT_COMMA)) {
                res = advance(res);
                if (!curTok.type.equals(TT_IDENTIFIER))
                    return res.failure(new InvalidSyntaxError(curTok.posStart, curTok.posEnd, "Expected identifier"));
                argNameToks.add(curTok);
                res = advance(res);
            }
            if (!curTok.type.equals(TT_RPAR))
                return res.failure(new InvalidSyntaxError(curTok.posStart, curTok.posEnd, "Expected ',' or ')'"));
        } else {
            if (!curTok.type.equals(TT_RPAR))
                return res.failure(new InvalidSyntaxError(curTok.posStart, curTok.posEnd, "Expected identifier or ')'"));
        }
        res = advance(res);

        boolean shouldReturnNull = false;
        Node body;
        if (curTok.type.equals(TT_ARROW)) {
            res = advance(res);
            body = res.register(expr());
            if (res.error != null) return res;
            shouldReturnNull = true;
        } else if (curTok.type.equals(TT_LCURL)) {
            res = advance(res);
            body = res.register(statements());
            if (res.error != null) return res;
            if (!curTok.type.equals(TT_RCURL))
                return res.failure(new InvalidSyntaxError(curTok.posStart, curTok.posEnd, "Expected '}'"));
            res = advance(res);
        } else {
            return res.failure(new InvalidSyntaxError(curTok.posStart, curTok.posEnd, "Expected '->' or '{'"));
        }

        return res.success(new FuncDefNode(varNameTok, argNameToks, body, shouldReturnNull));
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
