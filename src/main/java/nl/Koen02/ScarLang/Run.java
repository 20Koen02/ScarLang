package nl.Koen02.ScarLang;

import nl.Koen02.ScarLang.Type.Type;

import java.util.ArrayList;

public class Run {
    public static Context context = new Context("<program>");

    public static Type run(String fn, String code) throws Exception {
        // Generate Tokens
        Lexer lexer = new Lexer(fn, code);
        ArrayList<Token> tokens = lexer.scanTokens();

        // Generate Abstract Syntax Tree
        Parser parser = new Parser(tokens);
        ParseResult ast = parser.parse();
        if (ast.error != null) throw ast.error;

        // Global Symbol Table
        SymbolTable globalSymbolTable = new SymbolTable(null);
        globalSymbolTable.addDefaultSymbols();
        if (context.symbolTable == null) context.symbolTable = globalSymbolTable;

        // Start interpreter by visiting first node
        RunTimeResult res = ast.node.visit(context);
        if (res.error != null) throw res.error;
        return res.value;
    }
}
