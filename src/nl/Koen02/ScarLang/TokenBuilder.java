package nl.Koen02.ScarLang;

public class TokenBuilder {
    final String type;
    final String value;
    Position posStart = null;
    Position posEnd = null;

    public TokenBuilder(String type, String value) {
        this.type = type;
        this.value = value;
    }

    public TokenBuilder setPosStart(Position posStart) {
        this.posStart = posStart;
        return this;
    }

    public TokenBuilder setPosEnd(Position posEnd) {
        this.posEnd = posEnd;
        return this;
    }

    public Token build() {
        return new Token(type, value, posStart, posEnd);
    }
}
