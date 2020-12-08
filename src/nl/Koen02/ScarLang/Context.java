package nl.Koen02.ScarLang;

public class Context {
    String displayName;
    Context parent;
    Position parentEntryPos;

    public Context(String displayName) {
        this.displayName = displayName;
    }

}
