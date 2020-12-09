package nl.Koen02.ScarLang;

import nl.Koen02.ScarLang.Error.*;
import nl.Koen02.ScarLang.Error.Error;
import nl.Koen02.ScarLang.Node.Node;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class Run {
    public static Context context = new Context("<program>");

    static String run(String fn, String code) {
        try {
            // Generate Tokens
            Lexer lexer = new Lexer(fn, code);
            ArrayList<Token> tokens = lexer.makeTokens();

            // Generate Abstract Syntax Tree
            Parser parser = new Parser(tokens);
            ParseResult ast = parser.parse();
            if (ast.error != null) throw ast.error;

            // Global Symbol Table
            SymbolTable globalSymbolTable = new SymbolTable();
            globalSymbolTable.set("null", new Number((double) 0));

            // Run Program
            Interpreter interpreter = new Interpreter();
            if (context.symbolTable == null) context.symbolTable = globalSymbolTable;
            Number result = interpreter.visit(ast.node, context);

            // Log Output
            return result.get();
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        } catch (Error e) {
            System.out.println(e.getError());
        }
        return "";
    }
}
