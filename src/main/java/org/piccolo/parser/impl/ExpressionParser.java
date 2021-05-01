package org.piccolo.parser.impl;

import java.util.Optional;
import org.piccolo.node.TokenNode;
import org.piccolo.node.TokenNodeFactory;
import org.piccolo.node.TokenType;
import org.piccolo.parser.Parser;
import org.piccolo.context.ParsingContext;
import org.piccolo.exception.ParsingException;
import org.piccolo.util.ParsingUtils;

public class ExpressionParser implements Parser<TokenNode> {

    private final TokenNodeFactory factory = TokenNodeFactory.getInstance();

    @Override
    public TokenNode parse(ParsingContext context, String codeStr) throws ParsingException {
        if (context.getCurrentColumn() >= codeStr.length()) {
            throw new IllegalArgumentException();
        }
        context.skipSpace(codeStr);
        context.clearBuffer();
        TokenNode expressionNode = factory.createExpression(context.getCursor());
        while (!matchEnd(context, codeStr) && !context.hasErrors()) {
            char currentChar = codeStr.charAt(context.getCurrentColumn());
            if (ParsingUtils.isOperator(currentChar) || ParsingUtils.isSeparator(currentChar)) {
                TokenNode node = context.createNodeForCurrentToken();
                if (node != null && node.isAssignableType()) {
                    expressionNode.addChild(node);
                }
                if (ParsingUtils.isOperator(currentChar)) {
                    consume(context, currentChar, codeStr);
                    TokenNode operatorNode = context.createNodeForCurrentToken();
                    expressionNode.addChild(operatorNode);
                    continue;
                }
            }
            consume(context, currentChar, codeStr);
        }
        context.nextColumn(codeStr);
        TokenNode node = context.createNodeForCurrentToken();
        if (node != null) {
            expressionNode.addChild(node);
        }
        if (expressionNode.getChildren().isEmpty()) {
            return null;
        }
        node = expressionNode.getChildren().get(0);
        if (node != null && node.getType() == TokenType.VARIABLE_DEFINITION) {
            context.registerVariableDefinition(node);
        } else if (node != null && node.getType() == TokenType.OPERATOR) {
            TokenNode leftChild = node.getChildren().get(0);
            if (leftChild.getType() == TokenType.VARIABLE_DEFINITION) {
                context.registerVariableDefinition(leftChild);
            }
        }
        return node;
    }

    private void consume(ParsingContext context, char currentChar, String codeStr) {
        TokenNode parentNode = Optional.ofNullable(context.getPreviousNode()).orElse(TokenNode.NULl_TOKEN);
        if (context.getCurrentColumn() == codeStr.length() - 1 && !ParsingUtils
            .isExpressionTerminator(codeStr.charAt(context.getCurrentColumn()))) {
            context.reportError("Expression must end with ';'");
        } else if (ParsingUtils.isOperator(currentChar) && !parentNode.isAssignableType()) {
            context.reportError("Expected a literal or an identifier but found " + parentNode.getType());
        } else if (currentChar == '(' && parentNode.getType() == TokenType.VARIABLE_DEFINITION) {
            context.reportError("Found unexpected function definition");
        } else if ((ParsingUtils.isOperator(currentChar) && currentChar != '=') && parentNode.getType() == TokenType.VARIABLE_DEFINITION) {
            context.reportError("Invalid variable definition");
        } else if (!ParsingUtils.isValidExpressionCharacter(currentChar)) {
            context.clearTokenStartPosition();
            context.reportError("Found unexpected character `" + currentChar + "`");
        }
        if (!ParsingUtils.canSkip(currentChar)) {
            context.appendChar(currentChar);
        }
        context.nextColumn(codeStr);
        context.skipNonParsableCharacter(codeStr);
    }

    @Override
    public boolean matchEnd(ParsingContext context, String codeStr) {
        return context.getCurrentColumn() >= codeStr.length()
            || ParsingUtils.isExpressionTerminator(codeStr.charAt(context.getCurrentColumn()));
    }

    @Override
    public boolean match(TokenNode previousNode, char currentChar) {
        return previousNode != null && previousNode.isAssignableType();
    }
}
