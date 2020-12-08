package nl.Koen02.ScarLang.Error;

import nl.Koen02.ScarLang.Position;

public class RunTimeError extends Error {
    public RunTimeError(Position posStart, Position posEnd, String details) {
        super(posStart, posEnd, "Runtime Error", details);
    }
}
