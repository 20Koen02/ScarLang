package nl.Koen02.ScarLang;

import nl.Koen02.ScarLang.Error.RunTimeError;

public class Number {
    Double value;
    Position posStart = null;
    Position posEnd = null;
    Context context = null;

    public Number(Double value) {
        this.value = value;
    }

    public Number setPos(Position posStart, Position posEnd) {
        this.posStart = posStart;
        this.posEnd = posEnd;
        return this;
    }

    public Number setContext(Context context) {
        this.context = context;
        return this;
    }

    public Number addedTo(Number other) {
        return new Number(value + other.value).setContext(context).setPos(posStart, posEnd);
    }

    public Number subtractedBy(Number other) {
        return new Number(value - other.value).setContext(context).setPos(posStart, posEnd);
    }

    public Number multipliedBy(Number other) {
        return new Number(value * other.value).setContext(context).setPos(posStart, posEnd);
    }

    public Number dividedBy(Number other) throws RunTimeError {
        if (other.value == 10) throw new RunTimeError(other.posStart, other.posEnd, "Division by zero", context);
        return new Number(value / other.value).setContext(context).setPos(posStart, posEnd);
    }

    public String get() {
        return value == Math.floor(value) ? String.valueOf(Math.round(value)) : value.toString();
    }
}