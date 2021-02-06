package nl.Koen02.ScarLang;

import nl.Koen02.ScarLang.Error.Error;
import nl.Koen02.ScarLang.Node.Node;

public class ParseResult {
    Error error = null;
    Error statementError = null;
    Node node = null;
    Integer advanceCount = 0;
    Integer toReverseCount = 0;

    public void regAdvancement() {
        advanceCount += 1;
    }

    public Node register(ParseResult res) {
        advanceCount += res.advanceCount;
        if (res.error != null) error = res.error;
        return res.node;
    }

    public Node tryRegister(ParseResult res) {
        if (res.error != null) {
            toReverseCount = res.advanceCount;
            return null;
        }
        return register(res);
    }

    public ParseResult success(Node node) {
        this.node = node;
        return this;
    }

    public ParseResult failure(Error error) {
        if (this.error == null || advanceCount == 0) this.error = error;
        return this;
    }
}
