package org.piccolo.parsing.context;

import java.util.LinkedList;
import java.util.Queue;
import java.util.stream.Collectors;
import org.piccolo.parsing.exception.CompilationError;

public class CompilationErrorListener {

    private final Queue<CompilationError> compilationErrors;

    public CompilationErrorListener() {
        this.compilationErrors = new LinkedList<>();
    }

    public boolean hasErrors() {
        return !compilationErrors.isEmpty();
    }

    public void reportError(String message, int line, int column) {
        compilationErrors.add(new CompilationError(message, line, column));
    }

    @Override
    public String toString() {
        return compilationErrors.stream().map(CompilationError::toString).collect(Collectors.joining(System.lineSeparator()));
    }
}
