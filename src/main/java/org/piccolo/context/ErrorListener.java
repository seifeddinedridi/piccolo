package org.piccolo.context;

import java.util.LinkedList;
import java.util.Queue;
import java.util.stream.Collectors;
import org.piccolo.exception.CompilationException;

public class ErrorListener {

    private final Queue<CompilationException> compilationExceptions;

    public ErrorListener() {
        this.compilationExceptions = new LinkedList<>();
    }

    public boolean hasErrors() {
        return !compilationExceptions.isEmpty();
    }

    public void reportError(String message, int line, int column) {
        compilationExceptions.add(new CompilationException(message, line, column));
    }

    @Override
    public String toString() {
        return compilationExceptions.stream().map(CompilationException::toString).collect(Collectors.joining(System.lineSeparator()));
    }
}
