package nl.Koen02.ScarLang.Type;

import nl.Koen02.ScarLang.Context;
import nl.Koen02.ScarLang.Error.RunTimeError;
import nl.Koen02.ScarLang.Position;
import nl.Koen02.ScarLang.RunTimeResult;

import java.util.ArrayList;

public class Type {
    public Position posStart = null;
    public Position posEnd = null;
    public Context context = null;

    public Type setPos(Position posStart, Position posEnd) {
        this.posStart = posStart;
        this.posEnd = posEnd;
        return this;
    }

    public Type setContext(Context context) {
        this.context = context;
        return this;
    }

    public Type addedTo(Type other) throws RunTimeError {
        throw illegalOperation(other);
    }

    public Type subtractedBy(Type other) throws RunTimeError {
        throw illegalOperation(other);
    }

    public Type multipliedBy(Type other) throws RunTimeError {
        throw illegalOperation(other);
    }

    public Type multipliedByMinOne() throws RunTimeError {
        throw illegalOperation(new IntegerType(-1));
    }

    public Type dividedBy(Type other) throws RunTimeError {
        throw illegalOperation(other);
    }

    public Type poweredBy(Type other) throws RunTimeError {
        throw illegalOperation(other);
    }

    public Type getComparisonEe(Type other) throws RunTimeError {
        throw illegalOperation(other);
    }

    public Type getComparisonNe(Type other) throws RunTimeError {
        throw illegalOperation(other);
    }

    public Type getComparisonLt(Type other) throws RunTimeError {
        throw illegalOperation(other);
    }

    public Type getComparisonGt(Type other) throws RunTimeError {
        throw illegalOperation(other);
    }

    public Type getComparisonLte(Type other) throws RunTimeError {
        throw illegalOperation(other);
    }

    public Type getComparisonGte(Type other) throws RunTimeError {
        throw illegalOperation(other);
    }

    public Type andOperated(Type other) throws RunTimeError {
        throw illegalOperation(other);
    }

    public Type orOperated(Type other) throws RunTimeError {
        throw illegalOperation(other);
    }

    public Type notOperated() throws RunTimeError {
        throw illegalOperation(null);
    }

    public RunTimeResult execute(ArrayList<Type> args) throws Exception {
        throw illegalOperation(null);
    }

    public Type getCopy() throws Exception {
        throw new Exception("No copy method defined");
    }

    public boolean isPositive() throws RunTimeError {
        throw illegalOperation(null);
    }

    public boolean isTrue() {
        return false;
    }
    
    public RunTimeError illegalOperation(Type other) {
        if (other == null) other = this;
        return new RunTimeError(posStart, other.posEnd, "Illegal operation", context);
    }

    public String get() throws Exception {
        throw new Exception("No get method defined");
    }
}
