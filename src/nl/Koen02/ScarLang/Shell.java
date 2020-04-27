package nl.Koen02.ScarLang;

import java.util.ArrayList;
import java.util.Scanner;

public class Shell {
    static String prefix = "\n-> \t";

    public static void main(String[] args) {
        //run("1.2 / (2 + 1)");

        Scanner myObj = new Scanner(System.in);
        while (true) {
            System.out.print(prefix);
            run("<stdin>", myObj.nextLine());
        }
    }

    private static void run(String fn, String code) {
        Lexer lexer = new Lexer(fn, code);
        ArrayList<String> tokens = lexer.makeTokens();
        if (tokens.get(0).equals(Lexer.ERR)) {
            System.out.println("<- \t" + tokens.get(1));
        } else {
            System.out.println("<- \t" + tokens);
        }
    }
}
