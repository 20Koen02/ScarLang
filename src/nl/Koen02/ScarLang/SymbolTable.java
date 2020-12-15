package nl.Koen02.ScarLang;

import nl.Koen02.ScarLang.Type.FloatType;
import nl.Koen02.ScarLang.Type.Function.Stdlib.*;
import nl.Koen02.ScarLang.Type.IntegerType;
import nl.Koen02.ScarLang.Type.Type;

import java.util.HashMap;

public class SymbolTable {
    HashMap<String, Type> symbols;
    SymbolTable parent;

    public SymbolTable(SymbolTable parent) {
        symbols = new HashMap<>();
        this.parent = parent;
    }

    public Type get(String name) {
        Type value = symbols.get(name);
        if (value == null && parent != null) {
            return parent.get(name);
        }
        return value;
    }

    public void set(String name, Type value) {
       symbols.put(name, value);
    }

    public void remove(String name) {
        symbols.remove(name);
    }

    public void addDefaultSymbols() {
        set("null", IntegerType.zero);
        set("true", IntegerType.one);
        set("false", IntegerType.zero);
        set("mathPi", FloatType.mathPi);
        set("print", PrintFunction.getInstance());
        set("input", InputFunction.getInstance());
        set("clear", ClearFunction.getInstance());
        set("isInteger", IsIntegerFunction.getInstance());
        set("isFloat", IsFloatFunction.getInstance());
        set("isString", IsStringFunction.getInstance());
        set("isArray", IsArrayFunction.getInstance());
        set("isFunction", IsFunctionFunction.getInstance());
        set("append", AppendFunction.getInstance());
        set("extend", ExtendFunction.getInstance());
        set("pop", PopFunction.getInstance());
    }
}