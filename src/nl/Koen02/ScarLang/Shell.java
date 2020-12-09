package nl.Koen02.ScarLang;

import java.util.Scanner;

import static nl.Koen02.ScarLang.Run.run;

public class Shell {
    static String prefix = "\n-> \t";

    public static void main(String[] args) {
        Scanner stdin = new Scanner(System.in);
        while (true) {
            System.out.print(prefix);
            String nextLine = stdin.nextLine();
            if (nextLine.equals("exit()")) break;
            String result = run("<stdin>", nextLine);
            if (result != null) System.out.println(result);
        }
    }
}
