package org.piccolo.parsing.impl;

import java.util.ArrayList;
import java.util.List;
import org.piccolo.node.TokenNode;
import org.piccolo.node.TokenType;
import org.piccolo.parsing.Parser;
import org.piccolo.context.ParsingContext;
import org.piccolo.exception.ParsingException;
import org.piccolo.util.ParsingUtils;
import org.piccolo.node.ParameterListNode;

class ParameterListParser implements Parser<ParameterListNode> {

    @Override
    public ParameterListNode parse(ParsingContext context, String codeStr) throws ParsingException {
        matchStartingCharacter(context, codeStr, '(');
        List<TokenNode> children = new ArrayList<>();
        context.clearBuffer();
        while (!matchEnd(context, codeStr) && !context.hasErrors()) {
            char currentChar = codeStr.charAt(context.getCurrentColumn());
            if (currentChar == ' ' || currentChar == ',') {
                TokenNode node = context.createNodeForCurrentToken();
                if (node != null && node.getType() == TokenType.VARIABLE_DEFINITION) {
                    context.registerVariableDefinition(node);
                    children.add(node);
                }
            } else if (currentChar == '{' || ParsingUtils.isOperator(currentChar)) {
                context.clearBuffer();
                context.appendChar(currentChar);
                context.reportError("Unexpected character '" + currentChar + "'");
            } else {
                context.appendChar(currentChar);
            }
            context.nextColumn(codeStr);
            context.skipNonParsableCharacter(codeStr);
        }
        TokenNode node = context.createNodeForCurrentToken();
        if (node != null && node.getType() == TokenType.VARIABLE_DEFINITION) {
            context.registerVariableDefinition(node);
            children.add(node);
        }
        if (context.hasErrors() || context.getCurrentColumn() >= codeStr.length() || !matchEnd(context, codeStr)) {
            context.reportError("Missing ')'");
            throw new ParsingException(context);
        }
        context.nextColumn(codeStr);
        context.skipNonParsableCharacter(codeStr);
        // @TODO: this needs to be improved
        context.clearTokenStartPosition();
        return new ParameterListNode(children);
    }

    @Override
    public boolean matchEnd(ParsingContext context, String codeStr) {
        return context.getCurrentColumn() >= codeStr.length() || codeStr.charAt(context.getCurrentColumn()) == ')';
    }
}
