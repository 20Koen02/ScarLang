package nl.Koen02.ScarLang.Error;

import nl.Koen02.ScarLang.Position;

public class Error {
    String errorName, details;
    Position posStart, posEnd;

    public Error(Position posStart, Position posEnd, String errorName, String details) {
        this.posStart = posStart;
        this.posEnd = posEnd;
        this.errorName = errorName;
        this.details = details;
    }

    public String getError() {
        String result = String.format("%s: %s", errorName, details);
        result += String.format("\n\tFile %s, line %s, column %s", posStart.fn, posStart.ln, posStart.col);
        return result;
    }
}
