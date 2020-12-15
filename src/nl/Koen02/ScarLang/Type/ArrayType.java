package nl.Koen02.ScarLang.Type;

import nl.Koen02.ScarLang.Error.RunTimeError;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class ArrayType extends Type{
    public ArrayList<Type> elements;

    public ArrayType(ArrayList<Type> elements) {
        this.elements = elements;
    }

    public ArrayType addedTo(Type other) {
        ArrayType newArray = getCopy();
        newArray.elements.add(other);
        return newArray;
    }

    public ArrayType subtractedBy(Type other) throws RunTimeError {
        if (other instanceof IntegerType) {
            ArrayType newArray = getCopy();
            if (((IntegerType) other).value < newArray.elements.size()) {
                newArray.elements.remove(((IntegerType) other).value.intValue());
            } else {
                throw new RunTimeError(other.posStart, other.posEnd, "Index is out of bounds", context);
            }
            return newArray;
        } else throw illegalOperation(other);
    }

    public ArrayType multipliedBy(Type other) throws RunTimeError {
        if (other instanceof ArrayType) {
            ArrayType newArray = getCopy();
            newArray.elements.addAll(((ArrayType) other).elements);
            return newArray;
        } else throw illegalOperation(other);
    }

    public Type dividedBy(Type other) throws RunTimeError {
        if (other instanceof IntegerType) {
            if (((IntegerType) other).value < elements.size()) {
                return elements.get(((IntegerType) other).value);
            } else {
                throw new RunTimeError(other.posStart, other.posEnd, "Index is out of bounds", context);
            }
        } else throw illegalOperation(other);
    }

    public ArrayType getCopy() {
        ArrayType copy = new ArrayType(new ArrayList<>(elements));
        copy.setPos(posStart, posEnd);
        copy.setContext(context);
        return copy;
    }

    public String get() throws Exception {
        ArrayList<String> elStrings = new ArrayList<>();
        for (Type element : elements) {
            elStrings.add(element.get());
        }
        return String.format("[%s]", String.join(", ", elStrings));
    }
}
