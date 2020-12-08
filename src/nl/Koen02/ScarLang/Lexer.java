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
                tokens.add(new TokenBuilder(TT_PLUS, null).setPosStart(pos).build());
                advance();
            } else if (curChar.equals("-")) {
                tokens.add(new TokenBuilder(TT_MIN, null).setPosStart(pos).build());
                advance();
            } else if (curChar.equals("*")) {
                tokens.add(new TokenBuilder(TT_MUL, null).setPosStart(pos).build());
                advance();
            } else if (curChar.equals("/")) {
                tokens.add(new TokenBuilder(TT_DIV, null).setPosStart(pos).build());
                advance();
            } else if (curChar.equals("(")) {
                tokens.add(new TokenBuilder(TT_LPAR, null).setPosStart(pos).build());
                advance();
            } else if (curChar.equals(")")) {
                tokens.add(new TokenBuilder(TT_RPAR, null).setPosStart(pos).build());
                advance();
            } else {
                Position posStart = pos.getCopy();
                String ch = curChar;

                advance();

                throw new IllegalCharError(posStart, pos, "'" + ch +  "'");
            }
        }

        tokens.add(new TokenBuilder(TT_EOF, null).setPosStart(pos).build());
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
        return new TokenBuilder(dots == 0 ? TT_INT : TT_FLOAT, num.toString()).setPosStart(posStart).setPosEnd(pos).build();
    }
}
