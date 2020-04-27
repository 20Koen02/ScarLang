package nl.Koen02.ScarLang;

public class Token {
    String type, value;
    public Token(String type, String value) {
        this.type = type;
        this.value = value;
    }

    public String getToken() {
        return String.format("%s:%s", type, value);
    }
}
