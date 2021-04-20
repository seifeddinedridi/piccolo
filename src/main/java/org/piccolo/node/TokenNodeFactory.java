package org.piccolo.node;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.piccolo.context.Cursor;

public class TokenNodeFactory {

    private static TokenNodeFactory instance;

    private TokenNodeFactory() {
    }

    public static synchronized TokenNodeFactory getInstance() {
        if (instance == null) {
            instance = new TokenNodeFactory();
        }
        return instance;
    }

    private int computePrecedence(String operator) {
        switch (operator) {
            case "=":
                return 1;
            case "+":
            case "-":
            case "%":
                return 2;
            case "*":
                return 3;
            case "/":
                return 4;
            default:
                return 0;
        }
    }

    public TokenNode createVariableDefinition(TokenNode variableName, TokenNode variableType, Cursor tokenStartPosition) {
        return new TokenNode(TokenType.VARIABLE_DEFINITION, variableName.getName(), 0,
                asList(variableName, variableType), tokenStartPosition);
    }

    public TokenNode createOperator(String operator, Cursor tokenStartPosition) {
        return new TokenNode(TokenType.OPERATOR, operator, computePrecedence(operator), new ArrayList<>(), tokenStartPosition);
    }

    public TokenNode createIdentifier(String name, Cursor tokenStartPosition) {
        return new TokenNode(TokenType.IDENTIFIER, name, 0, Collections.emptyList(), tokenStartPosition);
    }

    public TokenNode createLiteral(String value, VariableType variableType, Cursor tokenStartPosition) {
        return new TokenNode(value, variableType, tokenStartPosition);
    }

    public TokenNode createPrimitiveNode(String type, Cursor tokenStartPosition) {
        return new TokenNode(TokenType.VARIABLE_TYPE, VariableType.fromString(type), tokenStartPosition);
    }

    public TokenNode createReturnAction(Cursor tokenStartPosition) {
        return new TokenNode(TokenType.RETURN_ACTION, "return", 0, new ArrayList<>(), tokenStartPosition);
    }

    public TokenNode createReturnVoidAction(Cursor tokenStartPosition) {
        return new TokenNode(TokenType.RETURN_ACTION, "return",
                0, Collections.singletonList(createVoidToken(tokenStartPosition)), tokenStartPosition);
    }

    private TokenNode createVoidToken(Cursor tokenStartPosition) {
        return new TokenNode(TokenType.VARIABLE_TYPE, VariableType.VOID, tokenStartPosition);
    }

    public TokenNode createExpression(Cursor tokenStartPosition) {
        return new ExpressionNode("", tokenStartPosition);
    }

    public TokenNode createModule(TokenType tokenType, List<TokenNode> children) {
        return new TokenNode(tokenType, "", 0, children, children.isEmpty() ? null : children.get(0).getTokenStartPosition());
    }
}
