package org.piccolo.exception;

import org.piccolo.context.ParsingContext;

public class ParsingException extends RuntimeException {

    private final ParsingContext context;

    public ParsingException(ParsingContext context) {
        this.context = context;
    }

    @Override
    public String getMessage() {
        return context.toString();
    }
}
