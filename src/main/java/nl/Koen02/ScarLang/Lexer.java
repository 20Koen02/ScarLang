package nl.Koen02.ScarLang;

import nl.Koen02.ScarLang.Error.ExpectedCharError;
import nl.Koen02.ScarLang.Error.IllegalCharError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static nl.Koen02.ScarLang.TokenTypes.*;

/**
 * Lexical analysis, lexing or tokenization is the process of converting a sequence of characters into a sequence of tokens
 * A program that performs lexical analysis may be termed a lexer, tokenizer, or scanner
 */
public class Lexer {
    private final String code;
    private final ArrayList<Token> tokens = new ArrayList<>();
    private String curChar;
    private final Position pos;

    /**
     * @param fn   Filename
     * @param code Source Code
     */
    public Lexer(String fn, String code) {
        this.code = code;
        pos = new Position(-1, 0, -1, fn, code);
        curChar = null;
        advance();
    }

    /**
     * Advance position and set curChar
     */
    private void advance() {
        pos.advance(curChar);
        if (pos.idx < code.length()) {
            curChar = Character.toString(code.charAt(pos.idx));
        } else {
            curChar = null;
        }
    }

    /**
     * Add a token to the tokens list and advance
     *
     * @param tokenType A token type from the TokenTypes class
     */
    private void addToken(String tokenType) {
        tokens.add(new Token(tokenType, null).setPosStart(pos));
        advance();
    }

    /**
     * Starting point for the lexer.
     * Performs lexical analysis on the source code
     *
     * @return A list of tokens
     * @throws ExpectedCharError Occurs when a character was expected but not present
     * @throws IllegalCharError  Occurs when there is an invalid or unexpected token that doesn't belong at this position in the code
     */
    public ArrayList<Token> scanTokens() throws ExpectedCharError, IllegalCharError {
        while (curChar != null) {
            scanToken();
        }

        tokens.add(new Token(TT_EOF, null).setPosStart(pos));
        return tokens;
    }

    /**
     * Performs lexical analysis on the current character
     *
     * @throws ExpectedCharError Occurs when a character was expected but not present
     * @throws IllegalCharError  Occurs when there is an invalid or unexpected token that doesn't belong at this position in the code
     */
    private void scanToken() throws ExpectedCharError, IllegalCharError {
        switch (curChar) {
            case " ":
            case "\t": advance(); break;

            case "#": skipComment(); break;

            case ";":
            case "\n": addToken(TT_NEWLN); break;

            case "\"": makeString(); break;
            case "!": makeNotEquals(); break;

            case "+": addToken(TT_PLUS); break;
            case "*": addToken(TT_MUL); break;
            case "%": addToken(TT_MOD); break;
            case "/": addToken(TT_DIV); break;
            case "^": addToken(TT_POW); break;
            case "(": addToken(TT_LPAR); break;
            case ")": addToken(TT_RPAR); break;
            case "[": addToken(TT_LSQUARE); break;
            case "]": addToken(TT_RSQUARE); break;
            case "{": addToken(TT_LCURL); break;
            case "}": addToken(TT_RCURL); break;
            case ",": addToken(TT_COMMA); break;

            case "-": makeOneOrOther(">", TT_MIN, TT_ARROW); break;
            case "=": makeOneOrOther("=", TT_EQ, TT_EE); break;
            case "<": makeOneOrOther("=", TT_LT, TT_LTE); break;
            case ">": makeOneOrOther("=", TT_GT, TT_GTE); break;

            default:
                if (TT_DIGITS.contains(curChar)) makeNumber();
                else if (TT_LETTERS.contains(curChar)) makeIdentifier();
                else {
                    Position posStart = pos.getCopy();
                    String illegalChar = curChar;
                    advance();
                    throw new IllegalCharError(posStart, pos, "'" + illegalChar + "'");
                }

        }
    }

    /**
     * Advance until newline or EOF (end of file)
     */
    private void skipComment() {
        while (!curChar.equals("\n")) {
            advance();
            if (curChar == null) break;
        }
    }

    /**
     * Looks for `=` after `!` to create NE token.
     * If `!` is not followed by `=`, throw ExpectedCharError
     *
     * @throws ExpectedCharError Occurs when a character was expected but not present
     */
    private void makeNotEquals() throws ExpectedCharError {
        Position posStart = pos.getCopy();
        advance();

        if (curChar.equals("=")) {
            advance();
            tokens.add(new Token(TT_NE, null).setPosStart(posStart).setPosEnd(pos.getCopy()));
        } else {
            advance();
            throw new ExpectedCharError(posStart, pos, "'=' (after '!')");
        }
    }

    /**
     * Checks the next character against `ch`, if it matches use `isEq` token, else use `notEq`
     *
     * @param ch    Character to check next character against
     * @param notEq Token to add when `ch` does not equal the next character
     * @param isEq  Token to add when `ch` does equal the next character
     */
    private void makeOneOrOther(String ch, String notEq, String isEq) {
        String tokenType = notEq;
        Position posStart = pos.getCopy();
        advance();

        if (curChar.equals(ch)) {
            advance();
            tokenType = isEq;
        }

        tokens.add(new Token(tokenType, null).setPosStart(posStart).setPosEnd(pos.getCopy()));
    }

    /**
     * Add a keyword or identifier token to the list by advancing through all alphanumerics and underscores
     */
    private void makeIdentifier() {
        StringBuilder idStrBuild = new StringBuilder();
        Position posStart = pos.getCopy();

        while (curChar != null && (TT_ALPHANUMERIC + "_").contains(curChar)) {
            idStrBuild.append(curChar);
            advance();
        }
        String idStr = idStrBuild.toString();

        String tokType = KEYWORDS.contains(idStr) ? TT_KEYWORD : TT_IDENTIFIER;
        tokens.add(new Token(tokType, idStr).setPosStart(posStart).setPosEnd(pos.getCopy()));
    }

    /**
     * Add an int or float token to the list by advancing through all digits
     */
    private void makeNumber() {
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

        String tokenType = dots == 0 ? TT_INT : TT_FLOAT;
        tokens.add(new Token(tokenType, num.toString()).setPosStart(posStart));
    }

    /**
     * Add a string token to the list by advancing through the characters until the next double quotation mark
     */
    private void makeString() {
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
        tokens.add(new Token(TT_STRING, string.toString()).setPosStart(posStart).setPosEnd(pos.getCopy()));
    }
}
