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
        if (other.value == 0) throw new RunTimeError(other.posStart, other.posEnd, "Division by zero", context);
        return new Number(value / other.value).setContext(context).setPos(posStart, posEnd);
    }

    public Number poweredBy(Number other) {
        return new Number(Math.pow(value, other.value)).setContext(context).setPos(posStart, posEnd);
    }

    public Number getComparisonEe(Number other) {
        return new Number(value.equals(other.value) ? 1d : 0d).setContext(context).setPos(posStart, posEnd);
    }

    public Number getComparisonNe(Number other) {
        return new Number(value.equals(other.value) ? 0d : 1d).setContext(context).setPos(posStart, posEnd);
    }

    public Number getComparisonLt(Number other) {
        return new Number(value < other.value ? 1d : 0d).setContext(context).setPos(posStart, posEnd);
    }

    public Number getComparisonGt(Number other) {
        return new Number(value > other.value ? 1d : 0d).setContext(context).setPos(posStart, posEnd);
    }

    public Number getComparisonLte(Number other) {
        return new Number(value <= other.value ? 1d : 0d).setContext(context).setPos(posStart, posEnd);
    }

    public Number getComparisonGte(Number other) {
        return new Number(value >= other.value ? 1d : 0d).setContext(context).setPos(posStart, posEnd);
    }

    public Number andOperated(Number other) {
        return new Number((double) (value.intValue() & other.value.intValue())).setContext(context).setPos(posStart, posEnd);
    }

    public Number orOperated(Number other) {
        return new Number((double) (value.intValue() | other.value.intValue())).setContext(context).setPos(posStart, posEnd);
    }

    public Number notOperated() {
        return new Number(value == 0 ? 1d : 0d).setContext(context).setPos(posStart, posEnd);
    }

    public Number getCopy() {
        Number copy = new Number(value);
        copy.setPos(posStart, posEnd);
        copy.setContext(context);
        return copy;
    }

    public boolean is_true() {
        return value != 0;
    }

    public String get() {
        return value == Math.floor(value) ? String.valueOf(Math.round(value)) : value.toString();
    }

}