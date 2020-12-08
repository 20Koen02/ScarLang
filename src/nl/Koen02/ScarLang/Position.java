package nl.Koen02.ScarLang;

public class Position {
    public Integer idx, ln, col;
    public String fn, ftxt;

    public Position(Integer idx, Integer ln, Integer col, String fn, String ftxt) {
        this.idx = idx;
        this.ln = ln;
        this.col = col;
        this.fn = fn;
        this.ftxt = ftxt;
    }

    public Position advance() {
        idx++;
        col++;
        return this;
    }

    public Position advance(String curChar) {
        idx++;
        col++;
        if (curChar == null) {
            return this;
        }
        if (curChar.equals("\n")) {
            ln++;
            col = 0;
        }
        return this;
    }

    public Position getCopy() {
        return new Position(idx, ln, col, fn, ftxt);
    }
}
