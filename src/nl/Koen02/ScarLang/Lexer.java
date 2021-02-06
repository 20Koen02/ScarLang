package nl.Koen02.ScarLang;

import nl.Koen02.ScarLang.Error.ExpectedCharError;
import nl.Koen02.ScarLang.Error.IllegalCharError;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static nl.Koen02.ScarLang.TokenTypes.*;

public class Lexer {
    String code, curChar;
    Position pos;

    public Lexer(String fn, String code) {
        this.code = code;
        pos = new Position(-1, 0, -1, fn, code);
        curChar = null;
        advance();
    }

    private void advance() {
        pos.advance(curChar);
        if (pos.idx < code.length()) {
            curChar = Character.toString(code.charAt(pos.idx));
        } else {
            curChar = null;
        }
    }

    public ArrayList<Token> makeTokens() throws IllegalCharError, ExpectedCharError {
        ArrayList<Token> tokens = new ArrayList<>();
        while (curChar != null) {

            if (curChar.equals("\t") || curChar.equals(" ")) {
                advance();
            } else if (curChar.equals("#")) {
                skipComment();
            } else if (curChar.equals(";") || curChar.equals("\n")) {
                tokens.add(new Token(TT_NEWLN, null).setPosStart(pos));
                advance();
            } else if (TT_DIGITS.contains(curChar)) {
                tokens.add(makeNumber());
            } else if (TT_LETTERS.contains(curChar)) {
                tokens.add(makeIdentifier());
            } else if (curChar.equals("\"")) {
                tokens.add(makeString());
            } else if (curChar.equals("+")) {
                tokens.add(new Token(TT_PLUS, null).setPosStart(pos));
                advance();
            } else if (curChar.equals("-")) {
                tokens.add(makeOneOrOther(">", TT_MIN, TT_ARROW));
            } else if (curChar.equals("*")) {
                tokens.add(new Token(TT_MUL, null).setPosStart(pos));
                advance();
            } else if (curChar.equals("/")) {
                tokens.add(new Token(TT_DIV, null).setPosStart(pos));
                advance();
            } else if (curChar.equals("^")) {
                tokens.add(new Token(TT_POW, null).setPosStart(pos));
                advance();
            } else if (curChar.equals("(")) {
                tokens.add(new Token(TT_LPAR, null).setPosStart(pos));
                advance();
            } else if (curChar.equals(")")) {
                tokens.add(new Token(TT_RPAR, null).setPosStart(pos));
                advance();
            } else if (curChar.equals("[")) {
                tokens.add(new Token(TT_LSQUARE, null).setPosStart(pos));
                advance();
            } else if (curChar.equals("]")) {
                tokens.add(new Token(TT_RSQUARE, null).setPosStart(pos));
                advance();
            } else if (curChar.equals("{")) {
                tokens.add(new Token(TT_LCURL, null).setPosStart(pos));
                advance();
            } else if (curChar.equals("}")) {
                tokens.add(new Token(TT_RCURL, null).setPosStart(pos));
                advance();
            } else if (curChar.equals("!")) {
                tokens.add(makeNotEquals());
            } else if (curChar.equals("=")) {
                tokens.add(makeOneOrOther("=", TT_EQ, TT_EE));
            } else if (curChar.equals("<")) {
                tokens.add(makeOneOrOther("=", TT_LT, TT_LTE));
            } else if (curChar.equals(">")) {
                tokens.add(makeOneOrOther("=", TT_GT, TT_GTE));
            } else if (curChar.equals(",")) {
                tokens.add(new Token(TT_COMMA, null).setPosStart(pos));
                advance();
            } else {
                Position posStart = pos.getCopy();
                String ch = curChar;

                advance();

                throw new IllegalCharError(posStart, pos, "'" + ch +  "'");
            }
        }

        tokens.add(new Token(TT_EOF, null).setPosStart(pos));
        return tokens;
    }

    private void skipComment() {
        while (!curChar.equals("\n")) {
            advance();
            if (curChar == null) break;
        }
        advance();
    }

    private Token makeNotEquals() throws ExpectedCharError {
        Position posStart = pos.getCopy();
        advance();

        if (curChar.equals("=")) {
            advance();
            return new Token(TT_NE, null).setPosStart(posStart).setPosEnd(pos.getCopy());
        }

        advance();
        throw new ExpectedCharError(posStart, pos, "'=' (after '!')");
    }

    private Token makeOneOrOther(String ch, String notEq, String isEq) {
        String tokenType = notEq;
        Position posStart = pos.getCopy();
        advance();

        if (curChar.equals(ch)) {
            advance();
            tokenType = isEq;
        }

        return new Token(tokenType, null).setPosStart(posStart).setPosEnd(pos.getCopy());
    }

    private Token makeIdentifier() {
        StringBuilder idStrBuild = new StringBuilder();
        Position posStart = pos.getCopy();

        while (curChar != null && (TT_ALPHANUMERIC + "_").contains(curChar)) {
            idStrBuild.append(curChar);
            advance();
        }
        String idStr = idStrBuild.toString();

        String tokType = KEYWORDS.contains(idStr) ? TT_KEYWORD : TT_IDENTIFIER;
        return new Token(tokType, idStr).setPosStart(posStart).setPosEnd(pos.getCopy());
    }

    private Token makeNumber() {
        StringBuilder num = new StringBuilder();
        int dots = 0;
        Position posStart = pos.getCopy();

        while (curChar != null && (TT_DIGITS + ".").contains(curChar)) {
            if (curChar.equals(".")) {
                dots++;
                num.append(".");
            } else {
                num.append(curChar);
            }
            advance();
        }
        return new Token(dots == 0 ? TT_INT : TT_FLOAT, num.toString()).setPosStart(posStart);
    }

    private Token makeString() {
        StringBuilder string = new StringBuilder();
        Position posStart = pos.getCopy();
        advance();

        HashMap<String, String> escChars = new HashMap<>();
        escChars.put("n", "\n");
        escChars.put("t", "\t");

        while (curChar != null && !curChar.equals("\"")) {
            if (curChar.equals("\\")) {
                advance();
                String getEsc = escChars.get(curChar);
                string.append(getEsc != null ? getEsc : curChar);
            } else {
                string.append(curChar);
            }

            advance();
        }
        advance();
        return new Token(TT_STRING, string.toString()).setPosStart(posStart).setPosEnd(pos.getCopy());
    }
}
