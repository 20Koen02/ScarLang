package nl.Koen02.ScarLang;

import nl.Koen02.ScarLang.Error.Error;
import nl.Koen02.ScarLang.Type.ArrayType;
import org.apache.commons.cli.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static nl.Koen02.ScarLang.Run.run;

public class Main {
    static String shellPrefix = "\n>>> ";
    static CommandLine cmd;

    public static void main(String[] args) throws Exception {
        Options options = new Options();

        options.addOption(new Option("c", true, "command"));
        options.addOption(new Option("t", false, "time"));

        CommandLineParser parser = new DefaultParser();

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException pe) {
            System.out.println("Error parsing command-line arguments!");
            System.out.println("Please, follow the instructions below:");
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("Log messages to sequence diagrams converter", options);
            System.exit(1);
        }

        int argsLengthEmpty = cmd.hasOption("t") ? 1 : 0;

        if (cmd.hasOption("c")) {
            runCode("<string>", cmd.getOptionValue("c"), false);
        } else {
            if (args.length > argsLengthEmpty) {
                runFile(args);
            } else {
                shell();
            }
        }
    }

    private static void runFile(String[] args) throws Exception {
        ArrayList<String> argsList = new ArrayList<String>(Arrays.asList(args));
        argsList.remove("-t");
        String fileName = String.join(" ", argsList);
        if (!fileName.endsWith(".scar"))
            throw new Exception(String.format("Failed to load script, file does not contain .scar extension: '%s'", fileName));
        String script;
        try {
            script = new Scanner(new File(fileName)).useDelimiter("\\Z").next();
            script = script.replaceAll("\r\n", "\n").replaceAll("\r", "\n");
        } catch (Exception e) {
            throw new Exception("An error occurred trying to load " + fileName);
        }

        runCode(fileName, script, false);
    }

    private static void runCode(String fn, String code, boolean printOutput) throws Exception {
        ArrayType result = null;
        long startTime = 0, endTime = 0;

        try {
            if (cmd.hasOption("t")) {
                startTime = System.nanoTime();
                result = (ArrayType) run(fn, code);
                endTime = System.nanoTime();
            } else {
                result = (ArrayType) run(fn, code);
            }
        } catch (Error e) {
            System.out.println(e.getError());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (printOutput) {
            if (result == null) return;
            String output = result.elements.size() == 1 ? result.elements.get(0).get() : result.get();
            System.out.println("<<< " + output);
        }

        if (cmd.hasOption("t")) {
            long timeElapsed = endTime - startTime;
            System.out.println("Execution time in milliseconds: " + timeElapsed / 1000000);
        }
    }

    public static void shell() throws Exception {
        Scanner stdin = new Scanner(System.in);
        while (true) {
            System.out.print(shellPrefix);
            String nextLine = stdin.nextLine();
            if (nextLine.strip().equals("")) continue;
            if (nextLine.equals("exit()")) break;

            runCode("<stdin>", nextLine, true);
        }
    }
}
