package org.piccolo.parsing;

import org.piccolo.parsing.context.ParsingContext;
import org.piccolo.parsing.exception.ParsingException;
import org.piccolo.node.TokenNode;
import org.piccolo.parsing.util.ParsingUtils;

public interface Parser<T extends TokenNode> {

    T parse(ParsingContext context, String codeStr) throws ParsingException;

    boolean matchEnd(ParsingContext context, String codeStr);

    default boolean match(TokenNode previousNode, char currentChar) {
        return false;
    }

    default void matchStartingCharacter(ParsingContext context, String codeStr, char expectedChar) {
        context.skipSpace(codeStr);
        if (ParsingUtils.isSkippeableCharacter(codeStr.charAt(context.getCurrentColumn()))) {
            context.skipNonParsableCharacter(codeStr);
            if (ParsingUtils.isSkippeableCharacter(codeStr.charAt(context.getCurrentColumn()))) {
                context.nextColumn(codeStr);
            }
        }
        if (context.getCurrentColumn() >= codeStr.length() || codeStr.charAt(context.getCurrentColumn()) != expectedChar) {
            context
                .reportError("Expected character '" + expectedChar + "'  but found '" + codeStr.charAt(context.getCurrentColumn()) + "'");
        }
        context.nextColumn(codeStr);
    }
}
