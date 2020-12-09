package nl.Koen02.ScarLang.Error;

import nl.Koen02.ScarLang.Position;

public class ExpectedCharError extends Error {
    public ExpectedCharError(Position posStart, Position posEnd, String details) {
        super(posStart, posEnd, "Expected Character", details);
    }
}
