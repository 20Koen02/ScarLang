package nl.Koen02.ScarLang;

import nl.Koen02.ScarLang.Error.RunTimeError;

public class Number {
    Integer value;
    Position posStart = null;
    Position posEnd = null;

    public Number(Integer value) {
        this.value = value;
    }

    public Number setPos(Position posStart, Position posEnd) {
        this.posStart = posStart;
        this.posEnd = posEnd;
        return this;
    }

    public Number addedTo(Number other) {
        return new Number(value + other.value).setPos(posStart, posEnd);
    }

    public Number subtractedBy(Number other) {
        return new Number(value - other.value).setPos(posStart, posEnd);
    }

    public Number multipliedBy(Number other) {
        return new Number(value * other.value).setPos(posStart, posEnd);
    }

    public Number dividedBy(Number other) throws RunTimeError {
        if (other.value == 0) throw new RunTimeError(other.posStart, other.posEnd, "Division by zero");
        return new Number(value / other.value).setPos(posStart, posEnd);
    }

    public String get() {
        return value.toString();
    }
}