package nl.Koen02.ScarLang.Type;

import nl.Koen02.ScarLang.Error.RunTimeError;

public class StringType extends Type {
    public String value;

    public StringType(String value) {
        this.value = value;
    }

    public StringType addedTo(Type other) throws RunTimeError {
        if (other instanceof StringType) {
            return (StringType) new StringType(value + ((StringType) other).value).setContext(context).setPos(posStart, posEnd);
        } else throw illegalOperation(other);
    }

    public StringType multipliedBy(Type other) throws RunTimeError {
        if (other instanceof IntegerType) {
            return (StringType) new StringType(value.repeat(((IntegerType) other).value)).setContext(context).setPos(posStart, posEnd);
        } else throw illegalOperation(other);
    }

    public boolean isTrue() {
        return value.length() > 0;
    }

    public StringType getCopy() {
        StringType copy = new StringType(value);
        copy.setPos(posStart, posEnd);
        copy.setContext(context);
        return copy;
    }

    public String get() {
        return String.format("\"%s\"", value);
    }

}
