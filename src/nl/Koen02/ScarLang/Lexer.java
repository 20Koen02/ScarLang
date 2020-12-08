package nl.Koen02.ScarLang;

import nl.Koen02.ScarLang.Error.IllegalCharError;

import java.util.ArrayList;

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

    public ArrayList<Token> makeTokens() throws IllegalCharError {
        ArrayList<Token> tokens = new ArrayList<>();
        while (curChar != null) {

            if (curChar.equals("\t") || curChar.equals(" ")) {
                advance();
            } else if (TT_DIG.contains(curChar)) {
                tokens.add(makeNumber());
            } else if (curChar.equals("+")) {
                tokens.add(new Token(TT_PLUS, null).setPosStart(pos));
                advance();
            } else if (curChar.equals("-")) {
                tokens.add(new Token(TT_MIN, null).setPosStart(pos));
                advance();
            } else if (curChar.equals("*")) {
                tokens.add(new Token(TT_MUL, null).setPosStart(pos));
                advance();
            } else if (curChar.equals("/")) {
                tokens.add(new Token(TT_DIV, null).setPosStart(pos));
                advance();
            } else if (curChar.equals("(")) {
                tokens.add(new Token(TT_LPAR, null).setPosStart(pos));
                advance();
            } else if (curChar.equals(")")) {
                tokens.add(new Token(TT_RPAR, null).setPosStart(pos));
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

    private Token makeNumber() {
        StringBuilder num = new StringBuilder();
        int dots = 0;
        Position posStart = pos.getCopy();

        while (curChar != null && (TT_DIG + ".").contains(curChar)) {
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
}
