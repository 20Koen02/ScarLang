package nl.Koen02.ScarLang.Error;

import nl.Koen02.ScarLang.Context;
import nl.Koen02.ScarLang.Position;

public class RunTimeError extends Error {
    Context context;

    public RunTimeError(Position posStart, Position posEnd, String details, Context context) {
        super(posStart, posEnd, "Runtime Error", details);
        this.context = context;
    }

    public String getError() {
        String result = generateTraceback();
        result += String.format("%s: %s", errorName, details);
        result += "\n\n" + Error.stringWithArrows(posStart.ftxt, posStart, posEnd);
        return result;
    }

    public String generateTraceback() {
        String result = "";
        Position pos = posStart;
        Context ctx = context;
        while (ctx != null) {
            result = String.format("  File %s, line %s, in %s\n%s", pos.fn, pos.ln + 1, ctx.displayName, result);
            pos = ctx.parentEntryPos;
            ctx = ctx.parent;
        }
        return "Traceback (most recent call last):\n" + result;
    }
}
