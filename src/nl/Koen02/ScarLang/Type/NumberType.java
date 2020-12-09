package nl.Koen02.ScarLang.Type;

import nl.Koen02.ScarLang.Error.RunTimeError;

public class NumberType extends Type {
    public Double value;

    public NumberType(Double value) {
        this.value = value;
    }

    public NumberType addedTo(Type other) throws RunTimeError {
        if (other instanceof NumberType) {
            return (NumberType) new NumberType(value + ((NumberType) other).value).setContext(context).setPos(posStart, posEnd);
        } else throw illegalOperation(other);
    }

    public NumberType subtractedBy(Type other) throws RunTimeError {
        if (other instanceof NumberType) {
            return (NumberType) new NumberType(value - ((NumberType) other).value).setContext(context).setPos(posStart, posEnd);
        } else throw illegalOperation(other);
    }

    public NumberType multipliedBy(Type other) throws RunTimeError {
        if (other instanceof NumberType) {
            return (NumberType) new NumberType(value * ((NumberType) other).value).setContext(context).setPos(posStart, posEnd);
        } else throw illegalOperation(other);
    }

    public NumberType dividedBy(Type other) throws RunTimeError {
        if (other instanceof NumberType) {
            if (((NumberType) other).value == 0)
                throw new RunTimeError(other.posStart, other.posEnd, "Division by zero", context);
            return (NumberType) new NumberType(value / ((NumberType) other).value).setContext(context).setPos(posStart, posEnd);
        } else throw illegalOperation(other);
    }

    public NumberType poweredBy(Type other) throws RunTimeError {
        if (other instanceof NumberType) {
            return (NumberType) new NumberType(Math.pow(value, ((NumberType) other).value)).setContext(context).setPos(posStart, posEnd);
        } else throw illegalOperation(other);
    }

    public NumberType getComparisonEe(Type other) throws RunTimeError {
        if (other instanceof NumberType) {
            return (NumberType) new NumberType(value.equals(((NumberType) other).value) ? 1d : 0d).setContext(context).setPos(posStart, posEnd);
        } else throw illegalOperation(other);

    }

    public NumberType getComparisonNe(Type other) throws RunTimeError {
        if (other instanceof NumberType) {

            return (NumberType) new NumberType(value.equals(((NumberType) other).value) ? 0d : 1d).setContext(context).setPos(posStart, posEnd);
        } else throw illegalOperation(other);

    }

    public NumberType getComparisonLt(Type other) throws RunTimeError {
        if (other instanceof NumberType) {

            return (NumberType) new NumberType(value < ((NumberType) other).value ? 1d : 0d).setContext(context).setPos(posStart, posEnd);
        } else throw illegalOperation(other);

    }

    public NumberType getComparisonGt(Type other) throws RunTimeError {
        if (other instanceof NumberType) {

            return (NumberType) new NumberType(value > ((NumberType) other).value ? 1d : 0d).setContext(context).setPos(posStart, posEnd);
        } else throw illegalOperation(other);

    }

    public NumberType getComparisonLte(Type other) throws RunTimeError {
        if (other instanceof NumberType) {

            return (NumberType) new NumberType(value <= ((NumberType) other).value ? 1d : 0d).setContext(context).setPos(posStart, posEnd);
        } else throw illegalOperation(other);

    }

    public NumberType getComparisonGte(Type other) throws RunTimeError {
        if (other instanceof NumberType) {

            return (NumberType) new NumberType(value >= ((NumberType) other).value ? 1d : 0d).setContext(context).setPos(posStart, posEnd);
        } else throw illegalOperation(other);

    }

    public NumberType andOperated(Type other) throws RunTimeError {
        if (other instanceof NumberType) {

            return (NumberType) new NumberType((double) (value.intValue() & ((NumberType) other).value.intValue())).setContext(context).setPos(posStart, posEnd);
        } else throw illegalOperation(other);

    }

    public NumberType orOperated(Type other) throws RunTimeError {
        if (other instanceof NumberType) {

            return (NumberType) new NumberType((double) (value.intValue() | ((NumberType) other).value.intValue())).setContext(context).setPos(posStart, posEnd);
        } else throw illegalOperation(other);

    }

    public NumberType notOperated() {
        return (NumberType) new NumberType(value == 0 ? 1d : 0d).setContext(context).setPos(posStart, posEnd);
    }

    public NumberType getCopy() {
        NumberType copy = new NumberType(value);
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