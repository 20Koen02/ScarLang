package nl.Koen02.ScarLang;

import nl.Koen02.ScarLang.Error.Error;
import nl.Koen02.ScarLang.Node.Node;
import nl.Koen02.ScarLang.Node.NumberNode;

import java.util.ArrayList;
import java.util.Scanner;

public class Shell {
    static String prefix = "\n-> \t";

    public static void main(String[] args) {
        Scanner stdin = new Scanner(System.in);
        while (true) {
            System.out.print(prefix);
            String nextLine = stdin.nextLine();
            if (nextLine.equals("exit()")) break;
            run("<stdin>", nextLine);
        }
    }

    private static void run(String fn, String code) {
        try {
            // Generate Tokens
            Lexer lexer = new Lexer(fn, code);
            ArrayList<Token> tokens = lexer.makeTokens();

            // Generate Abstract Syntax Tree
            Parser parser = new Parser(tokens);
            Node ast = parser.parse();

            System.out.println(ast.get());

            // Run Program
            Interpreter interpreter = new Interpreter();
            Number result = interpreter.visit(ast);

            System.out.println(result.get());
        } catch (Error e) {
            System.out.println(e.getError());
        }
    }
}
