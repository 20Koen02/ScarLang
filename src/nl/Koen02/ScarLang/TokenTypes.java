package nl.Koen02.ScarLang;

import java.util.Arrays;
import java.util.List;

public class TokenTypes {
    public static final String TT_DIGITS = "0123456789";
    public static final String TT_LETTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String TT_ALPHANUMERIC = TT_DIGITS + TT_LETTERS;
    public static final String TT_INT = "INT";
    public static final String TT_FLOAT = "FLOAT";
    public static final String TT_STRING = "STRING";
    public static final String TT_IDENTIFIER = "IDENTIFIER";
    public static final String TT_KEYWORD = "KEYWORD";
    public static final String TT_PLUS = "PLUS";
    public static final String TT_MIN = "MIN";
    public static final String TT_MUL = "MUL";
    public static final String TT_DIV = "DIV";
    public static final String TT_POW = "POW";
    public static final String TT_MOD = "MOD";
    public static final String TT_EQ = "EQ";
    public static final String TT_LPAR = "LPAR";
    public static final String TT_RPAR = "RPAR";
    public static final String TT_LSQUARE = "LSQUARE";
    public static final String TT_RSQUARE = "RSQUARE";
    public static final String TT_LCURL = "LCURL";
    public static final String TT_RCURL= "RCURL";
    public static final String TT_EE = "EE";
    public static final String TT_NE = "NE";
    public static final String TT_LT = "LT";
    public static final String TT_GT = "GT";
    public static final String TT_LTE = "LTE";
    public static final String TT_GTE = "GTE";
    public static final String TT_COMMA = "COMMA";
    public static final String TT_ARROW = "ARROW";
    public static final String TT_NEWLN = "NEWLN";
    public static final String TT_EOF = "EOF";

    private static final String[] KEYWORDS_ARRAY = {
            "var",
            "and",
            "or",
            "not",
            "if",
            "then",
            "elif",
            "else",
            "for",
            "to",
            "step",
            "while",
            "func",
            "end",
            "return",
            "continue",
            "break"
    };
    public static final List<String> KEYWORDS = Arrays.asList(KEYWORDS_ARRAY);
}
