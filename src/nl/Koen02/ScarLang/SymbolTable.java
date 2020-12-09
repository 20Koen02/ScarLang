package nl.Koen02.ScarLang;

import nl.Koen02.ScarLang.Type.NumberType;
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
        set("null", new NumberType((double) 0));
        set("true", new NumberType((double) 1));
        set("false", new NumberType((double) 0));
    }
}