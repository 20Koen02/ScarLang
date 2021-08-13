package nl.Koen02.ScarLang.Error;

import nl.Koen02.ScarLang.Position;

public class Error extends Exception {
    String errorName, details;
    Position posStart, posEnd;

    public Error(Position posStart, Position posEnd, String errorName, String details) {
        this.posStart = posStart;
        this.posEnd = posEnd;
        this.errorName = errorName;
        this.details = details;
    }

    public String getError() {
        String result = String.format("%s: %s", errorName, details);
        result += String.format("\n\tFile %s, line %s, column %s", posStart.fn, posStart.ln + 1, posStart.col + 1);
        result += "\n\n" + Error.stringWithArrows(posStart.ftxt, posStart, posEnd);
        return result;
    }

    public static String stringWithArrows(String text, Position posStart, Position posEnd) {
        StringBuilder result = new StringBuilder();

        int idxStart = Math.max(text.substring(0, posStart.idx).lastIndexOf("\n"), 0);
        int idxEnd = text.indexOf("\n", idxStart + 1);
        if (idxEnd < 0) idxEnd = text.length();

        int lineCount = posEnd.ln - posStart.ln + 1;
        for (int i = 0; i < lineCount; i++) {
            String line = text.substring(idxStart, idxEnd);
            int colStart = i == 0 ? posStart.col : 0;
            int colEnd = i == lineCount - 1 ? posEnd.col : line.length() - 1;

            result.append(line).append("\n").append(" ".repeat(colStart)).append("^".repeat(colEnd - colStart));

            idxStart = idxEnd;
            idxEnd = text.indexOf("\n", idxStart + 1);
            if (idxEnd < 0) idxEnd = text.length();
        }
        return result.toString().replace("\t", "");
    }
}
