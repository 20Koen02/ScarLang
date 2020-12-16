package nl.Koen02.ScarLang;

import nl.Koen02.ScarLang.Error.Error;
import nl.Koen02.ScarLang.Type.ArrayType;
import java.util.Scanner;

import static nl.Koen02.ScarLang.Run.run;

public class Shell {
    static String prefix = "\n-> \t";

    public static void main(String[] args) throws Exception {
        Scanner stdin = new Scanner(System.in);
        while (true) {
            System.out.print(prefix);
            String nextLine = stdin.nextLine();
            if (nextLine.strip().equals("")) continue;
            if (nextLine.equals("exit()")) break;

            ArrayType result = null;
            try {
                result = (ArrayType) run("<stdin>", nextLine);
            } catch (Error e) {
                System.out.println(e.getError());
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (result == null) continue;
            if (result.elements.size() == 1) {
                System.out.println(result.elements.get(0).get());
            } else {
                System.out.println(result.get());
            }
        }
    }
}
