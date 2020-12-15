package nl.Koen02.ScarLang.Type;

import nl.Koen02.ScarLang.Error.RunTimeError;

public class IntegerType extends Type {
    public Integer value;

    public IntegerType(Integer value) {
        this.value = value;
    }

    public IntegerType addedTo(Type other) throws RunTimeError {
        if (other instanceof IntegerType) {
            return (IntegerType) new IntegerType(value + ((IntegerType) other).value).setContext(context).setPos(posStart, posEnd);
        } else throw illegalOperation(other);
    }

    public IntegerType subtractedBy(Type other) throws RunTimeError {
        if (other instanceof IntegerType) {
            return (IntegerType) new IntegerType(value - ((IntegerType) other).value).setContext(context).setPos(posStart, posEnd);
        } else throw illegalOperation(other);
    }

    public IntegerType multipliedBy(Type other) throws RunTimeError {
        if (other instanceof IntegerType) {
            return (IntegerType) new IntegerType(value * ((IntegerType) other).value).setContext(context).setPos(posStart, posEnd);
        } else throw illegalOperation(other);
    }

    public IntegerType multipliedByMinOne() {
        return (IntegerType) new IntegerType(value * new IntegerType(-1).value).setContext(context).setPos(posStart, posEnd);
    }

    public IntegerType dividedBy(Type other) throws RunTimeError {
        if (other instanceof IntegerType) {
            if (((IntegerType) other).value == 0)
                throw new RunTimeError(other.posStart, other.posEnd, "Division by zero", context);
            return (IntegerType) new IntegerType(value / ((IntegerType) other).value).setContext(context).setPos(posStart, posEnd);
        } else throw illegalOperation(other);
    }

    public IntegerType poweredBy(Type other) throws RunTimeError {
        if (other instanceof IntegerType) {
            return (IntegerType) new IntegerType((int) Math.pow(value, ((IntegerType) other).value)).setContext(context).setPos(posStart, posEnd);
        } else throw illegalOperation(other);
    }

    public IntegerType getComparisonEe(Type other) throws RunTimeError {
        if (other instanceof IntegerType) {
            return (IntegerType) new IntegerType(value.equals(((IntegerType) other).value) ? 1 : 0).setContext(context).setPos(posStart, posEnd);
        } else throw illegalOperation(other);
    }

    public IntegerType getComparisonNe(Type other) throws RunTimeError {
        if (other instanceof IntegerType) {
            return (IntegerType) new IntegerType(value.equals(((IntegerType) other).value) ? 0 : 1).setContext(context).setPos(posStart, posEnd);
        } else throw illegalOperation(other);
    }

    public IntegerType getComparisonLt(Type other) throws RunTimeError {
        if (other instanceof IntegerType) {
            return (IntegerType) new IntegerType(value < ((IntegerType) other).value ? 1 : 0).setContext(context).setPos(posStart, posEnd);
        } else throw illegalOperation(other);
    }

    public IntegerType getComparisonGt(Type other) throws RunTimeError {
        if (other instanceof IntegerType) {
            return (IntegerType) new IntegerType(value > ((IntegerType) other).value ? 1 : 0).setContext(context).setPos(posStart, posEnd);
        } else throw illegalOperation(other);
    }

    public IntegerType getComparisonLte(Type other) throws RunTimeError {
        if (other instanceof IntegerType) {
            return (IntegerType) new IntegerType(value <= ((IntegerType) other).value ? 1 : 0).setContext(context).setPos(posStart, posEnd);
        } else throw illegalOperation(other);
    }

    public IntegerType getComparisonGte(Type other) throws RunTimeError {
        if (other instanceof IntegerType) {
            return (IntegerType) new IntegerType(value >= ((IntegerType) other).value ? 1 : 0).setContext(context).setPos(posStart, posEnd);
        } else throw illegalOperation(other);
    }

    public IntegerType andOperated(Type other) throws RunTimeError {
        if (other instanceof IntegerType) {
            return (IntegerType) new IntegerType(value & ((IntegerType) other).value).setContext(context).setPos(posStart, posEnd);
        } else throw illegalOperation(other);
    }

    public IntegerType orOperated(Type other) throws RunTimeError {
        if (other instanceof IntegerType) {
            return (IntegerType) new IntegerType(value | ((IntegerType) other).value).setContext(context).setPos(posStart, posEnd);
        } else throw illegalOperation(other);
    }

    public IntegerType notOperated() {
        return (IntegerType) new IntegerType(value == 0 ? 1 : 0).setContext(context).setPos(posStart, posEnd);
    }

    public boolean isPositive() {
        return value >= 0;
    }

    public IntegerType getCopy() {
        IntegerType copy = new IntegerType(value);
        copy.setPos(posStart, posEnd);
        copy.setContext(context);
        return copy;
    }

    public boolean isTrue() {
        return value != 0;
    }

    public String get() {
        return value.toString();
    }

}