package org.piccolo.parsing.exception;

public class CompilationError {

    int line;
    int position;
    String errorMessage;

    public CompilationError(String errorMessage, int line, int position) {
        this.errorMessage = errorMessage;
        this.line = line;
        this.position = position;
    }

    @Override
    public String toString() {
        return errorMessage + ". Error at line=" + line + ", position=" + position;
    }
}
