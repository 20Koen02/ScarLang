package nl.Koen02.ScarLang;

import java.util.HashMap;

public class SymbolTable {
    HashMap<String, Number> symbols;
    SymbolTable parent;

    public SymbolTable() {
        symbols = new HashMap<>();
        parent = null;
    }

    public Number get(String name) {
        Number value = symbols.get(name);
        if (value == null && parent != null) {
            return parent.get(name);
        }
        return value;
    }

    public void set(String name, Number value) {
       symbols.put(name, value);
    }

    public void remove(String name) {
        symbols.remove(name);
    }

    public void addDefaultSymbols() {
        set("null", new Number((double) 0));
        set("true", new Number((double) 1));
        set("false", new Number((double) 0));
    }
}