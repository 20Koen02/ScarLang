package nl.Koen02.ScarLang.Type;

import nl.Koen02.ScarLang.Error.RunTimeError;

public class FloatType extends Type {
    public Float value;

    public static FloatType mathPi = new FloatType((float) Math.PI);

    public FloatType(Float value) {
        this.value = value;
    }

    public FloatType addedTo(Type other) throws RunTimeError {
        if (other instanceof FloatType) {
            return (FloatType) new FloatType(value + ((FloatType) other).value).setContext(context).setPos(posStart, posEnd);
        } else throw illegalOperation(other);
    }

    public FloatType subtractedBy(Type other) throws RunTimeError {
        if (other instanceof FloatType) {
            return (FloatType) new FloatType(value - ((FloatType) other).value).setContext(context).setPos(posStart, posEnd);
        } else throw illegalOperation(other);
    }

    public FloatType multipliedBy(Type other) throws RunTimeError {
        if (other instanceof FloatType) {
            return (FloatType) new FloatType(value * ((FloatType) other).value).setContext(context).setPos(posStart, posEnd);
        } else throw illegalOperation(other);
    }

    public FloatType multipliedByMinOne() {
        return (FloatType) new FloatType(value * new FloatType(-1f).value).setContext(context).setPos(posStart, posEnd);
    }

    public FloatType dividedBy(Type other) throws RunTimeError {
        if (other instanceof FloatType) {
            if (((FloatType) other).value == 0f)
                throw new RunTimeError(other.posStart, other.posEnd, "Division by zero", context);
            return (FloatType) new FloatType(value / ((FloatType) other).value).setContext(context).setPos(posStart, posEnd);
        } else throw illegalOperation(other);
    }

    public FloatType poweredBy(Type other) throws RunTimeError {
        if (other instanceof FloatType) {
            return (FloatType) new FloatType((float) Math.pow(value, ((FloatType) other).value)).setContext(context).setPos(posStart, posEnd);
        } else throw illegalOperation(other);
    }

    public FloatType moduloBy(Type other) throws RunTimeError {
        if (other instanceof FloatType) {
            return (FloatType) new FloatType(value % ((FloatType) other).value).setContext(context).setPos(posStart, posEnd);
        } else throw illegalOperation(other);
    }

    public IntegerType getComparisonEe(Type other) throws RunTimeError {
        if (other instanceof FloatType) {
            return (IntegerType) new IntegerType(value.equals(((FloatType) other).value) ? 1 : 0).setContext(context).setPos(posStart, posEnd);
        } else throw illegalOperation(other);
    }

    public IntegerType getComparisonNe(Type other) throws RunTimeError {
        if (other instanceof FloatType) {
            return (IntegerType) new IntegerType(value.equals(((FloatType) other).value) ? 0 : 1).setContext(context).setPos(posStart, posEnd);
        } else throw illegalOperation(other);
    }

    public IntegerType getComparisonLt(Type other) throws RunTimeError {
        if (other instanceof FloatType) {
            return (IntegerType) new IntegerType(value < ((FloatType) other).value ? 1 : 0).setContext(context).setPos(posStart, posEnd);
        } else throw illegalOperation(other);
    }

    public IntegerType getComparisonGt(Type other) throws RunTimeError {
        if (other instanceof FloatType) {
            return (IntegerType) new IntegerType(value > ((FloatType) other).value ? 1 : 0).setContext(context).setPos(posStart, posEnd);
        } else throw illegalOperation(other);
    }

    public IntegerType getComparisonLte(Type other) throws RunTimeError {
        if (other instanceof FloatType) {
            return (IntegerType) new IntegerType(value <= ((FloatType) other).value ? 1 : 0).setContext(context).setPos(posStart, posEnd);
        } else throw illegalOperation(other);
    }

    public IntegerType getComparisonGte(Type other) throws RunTimeError {
        if (other instanceof FloatType) {
            return (IntegerType) new IntegerType(value >= ((FloatType) other).value ? 1 : 0).setContext(context).setPos(posStart, posEnd);
        } else throw illegalOperation(other);
    }

    public boolean isPositive() {
        return value >= 0f;
    }

    public FloatType getCopy() {
        FloatType copy = new FloatType(value);
        copy.setPos(posStart, posEnd);
        copy.setContext(context);
        return copy;
    }

    public String get() {
        return value.toString();
    }
}