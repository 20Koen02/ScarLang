package nl.Koen02.ScarLang;

public class Token {
    String type, value;
    Position posStart, posEnd;

    public Token(String type, String value, Position posStart, Position posEnd) {
        this.type = type;
        this.value = value;

        if (posStart != null) {
            this.posStart = posStart.getCopy();
            this.posEnd = posStart.getCopy();
            this.posEnd.advance();
        }

        if (posEnd != null) {
            this.posEnd = posEnd;
        }
    }

    public String get() {
        return String.format("%s:%s", type, value);
    }
}
