package nl.Koen02.ScarLang;

public class Context {
    public String displayName;
    public Context parent;
    public Position parentEntryPos;

    public Context(String displayName) {
        this.displayName = displayName;
    }

    public Context(String displayName, Context parent, Position parentEntryPos) {
        this.displayName = displayName;
        this.parent = parent;
        this.parentEntryPos = parentEntryPos;
    }

}
