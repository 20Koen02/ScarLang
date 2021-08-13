package nl.Koen02.ScarLang.Error;

import nl.Koen02.ScarLang.Position;

public class InvalidSyntaxError extends Error {
    public InvalidSyntaxError(Position posStart, Position posEnd, String details) {
        super(posStart, posEnd, "Invalid Syntax", details);
    }
}
