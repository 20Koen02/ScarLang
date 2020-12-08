package nl.Koen02.ScarLang;

import nl.Koen02.ScarLang.Error.Error;
import nl.Koen02.ScarLang.Error.IllegalCharError;
import nl.Koen02.ScarLang.Node.Node;

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
            Lexer lexer = new Lexer(fn, code);
            ArrayList<Token> tokens = lexer.makeTokens();

            Parser parser = new Parser(tokens);
            Node ast = parser.parse();

            System.out.println(ast.get());
        } catch (Error e) {
            System.out.println(e.getError());
        }
    }
}
