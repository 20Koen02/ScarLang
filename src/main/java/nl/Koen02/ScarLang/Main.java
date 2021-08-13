package nl.Koen02.ScarLang;

import nl.Koen02.ScarLang.Error.Error;
import nl.Koen02.ScarLang.Type.ArrayType;
import org.apache.commons.cli.*;

import java.io.File;
import java.util.Scanner;

import static nl.Koen02.ScarLang.Run.run;

public class Main {
    static String shellPrefix = "\n>>> ";

    public static void main(String[] args) throws Exception {
        Options options = new Options();

        options.addOption(new Option("c", true, "command"));

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException pe) {
            System.out.println("Error parsing command-line arguments!");
            System.out.println("Please, follow the instructions below:");
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("Log messages to sequence diagrams converter", options);
            System.exit(1);
        }

        if (cmd.hasOption("c")) {
            runCode("<string>", cmd.getOptionValue("c"));
        } else {
            if (args.length > 0) {
                String fileName = String.join(" ", args);
                if (!fileName.endsWith(".scar"))
                    throw new Exception(String.format("Failed to load script, file does not contain .scar extension: '%s'", fileName));
                String script;
                try {
                    script = new Scanner(new File(fileName)).useDelimiter("\\Z").next();
                    script = script.replaceAll("\r\n", "\n").replaceAll("\r", "\n");
                } catch (Exception e) {
                    throw new Exception("An error occurred trying to load " + fileName);
                }

                runCode(fileName, script);
            } else {
                shell();
            }
        }
    }

    private static String runCode(String fn, String code) throws Exception {
        ArrayType result = null;
        try {
            result = (ArrayType) run(fn, code);
        } catch (Error e) {
            System.out.println(e.getError());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (result == null) return "";
        if (result.elements.size() == 1) {
            return result.elements.get(0).get();
        } else {
            return result.get();
        }
    }

    public static void shell() throws Exception {
        Scanner stdin = new Scanner(System.in);
        while (true) {
            System.out.print(shellPrefix);
            String nextLine = stdin.nextLine();
            if (nextLine.strip().equals("")) continue;
            if (nextLine.equals("exit()")) break;

            System.out.println(runCode("<stdin>", nextLine));
        }
    }
}
