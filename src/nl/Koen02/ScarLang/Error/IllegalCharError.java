package nl.Koen02.ScarLang.Error;

import nl.Koen02.ScarLang.Error.Error;
import nl.Koen02.ScarLang.Position;

public class IllegalCharError extends Error {


    public IllegalCharError(Position posStart, Position posEnd, String details) {
        super(posStart, posEnd, "Illegal Character", details);
    }
}
