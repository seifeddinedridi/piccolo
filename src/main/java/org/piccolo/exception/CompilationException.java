package org.piccolo.exception;

public class CompilationException {

    int line;
    int position;
    String errorMessage;

    public CompilationException(String errorMessage, int line, int position) {
        this.errorMessage = errorMessage;
        this.line = line;
        this.position = position;
    }

    @Override
    public String toString() {
        return errorMessage + ". Error at line=" + line + ", position=" + position;
    }
}
