package nl.Koen02.ScarLang;

import nl.Koen02.ScarLang.Error.Error;
import nl.Koen02.ScarLang.Type.Type;

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
            SymbolTable globalSymbolTable = new SymbolTable(null);
            globalSymbolTable.addDefaultSymbols();

            // Run Program
            Interpreter interpreter = new Interpreter();
            if (context.symbolTable == null) context.symbolTable = globalSymbolTable;
            Type result = interpreter.visit(ast.node, context);

            // Log Output
            if (result == null) return null;
            return result.get();
        } catch (Error e) {
            System.out.println(e.getError());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
