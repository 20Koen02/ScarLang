package nl.Koen02.ScarLang;

public class Token {
    public String type, value;
    public Position posStart, posEnd;

    public Token(String type, String value) {
        this.type = type;
        this.value = value;
    }
    public Token setPosStart(Position posStart) {
        this.posStart = posStart.getCopy();
        this.posEnd = posStart.getCopy();
        this.posEnd.advance();
        return this;
    }

    public Token setPosEnd(Position posEnd) {
        this.posEnd = posEnd;
        return this;
    }

    public Boolean matches( String type, String value) {
        return this.type.equals(type) && this.value.equals(value);
    }

    public String get() {
        return String.format("%s:%s", type, value);
    }
}
