package nl.Koen02.ScarLang;

import nl.Koen02.ScarLang.Error.Error;
import nl.Koen02.ScarLang.Type.Type;

public class RunTimeResult {
    public Error error;
    public Type value;
    public Type funcReturnValue;
    public boolean loopShouldContinue;
    public boolean loopShouldBreak;

    public RunTimeResult() {
        reset();
    }

    public void reset() {
        error = null;
        value = null;
        funcReturnValue = null;
        loopShouldContinue = false;
        loopShouldBreak = false;
    }

    public Type register(RunTimeResult res) {
        error = res.error;
        funcReturnValue = res.funcReturnValue;
        loopShouldContinue = res.loopShouldContinue;
        loopShouldBreak = res.loopShouldBreak;
        return res.value;
    }

    public RunTimeResult success(Type value) {
        reset();
        this.value = value;
        return this;
    }

    public RunTimeResult successReturn(Type value) {
        reset();
        this.funcReturnValue = value;
        return this;
    }

    public RunTimeResult successContinue() {
        reset();
        this.loopShouldContinue = true;
        return this;
    }

    public RunTimeResult successBreak() {
        reset();
        this.loopShouldBreak = true;
        return this;
    }

    public RunTimeResult failure(Error error) {
        reset();
        this.error = error;
        return this;
    }

    public boolean shouldReturn() {
        if (error != null) return true;
        if (funcReturnValue != null) return true;
        if (loopShouldBreak) return true;
        return loopShouldContinue;
    }
}
