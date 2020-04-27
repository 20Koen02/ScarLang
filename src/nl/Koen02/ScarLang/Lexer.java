package nl.Koen02.ScarLang;

import nl.Koen02.ScarLang.Error.IllegalCharError;

import java.util.ArrayList;

public class Lexer {
    static String DIG = "0123456789";
    static String ERR = "ERR";
    static String OP = "OP";
    static String INT = "INT";
    static String FLOAT = "FLOAT";
    static String PLUS = "PLUS";
    static String MIN = "MIN";
    static String MUL = "MUL";
    static String DIV = "DIV";
    static String LPAR = "LPAR";
    static String RPAR = "RPAR";

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
        if(pos.idx < code.length()) {
            curChar = Character.toString(code.charAt(pos.idx));
        } else {
            curChar = null;
        }
    }

    public ArrayList<String> makeTokens() {
        ArrayList<String> tokens = new ArrayList<>();
        while (curChar != null) {

            if (curChar.equals("\t") || curChar.equals(" ")) {
                advance();
            } else if (DIG.contains(curChar)) {
                tokens.add(makeNumber().getToken());
            } else if (curChar.equals("+")) {
                tokens.add(new Token(OP, PLUS).getToken());
                advance();
            } else if (curChar.equals("-")) {
                tokens.add(new Token(OP, MIN).getToken());
                advance();
            } else if (curChar.equals("*")) {
                tokens.add(new Token(OP, MUL).getToken());
                advance();
            } else if (curChar.equals("/")) {
                tokens.add(new Token(OP, DIV).getToken());
                advance();
            } else if (curChar.equals("(")) {
                tokens.add(new Token(OP, LPAR).getToken());
                advance();
            } else if (curChar.equals(")")) {
                tokens.add(new Token(OP, RPAR).getToken());
                advance();
            } else {
                Position posStart = pos.getCopy();
                String ch = curChar;

                tokens.clear();
                tokens.add(ERR);

                IllegalCharError err = new IllegalCharError(posStart, pos, ch);
                tokens.add(err.getError());
                return tokens;
            }
        }

        return tokens;
    }

    private Token makeNumber() {
        StringBuilder num = new StringBuilder();
        int dots = 0;

        while (curChar != null && (DIG + ".").contains(curChar)) {
            if (curChar.equals(".")) {
                dots++;
                num.append(".");
            } else {
                num.append(curChar);
            }
            advance();
        }
        if (dots == 0) {
            return new Token(INT, num.toString());
        } else {
            return new Token(FLOAT, num.toString());
        }

    }
}
