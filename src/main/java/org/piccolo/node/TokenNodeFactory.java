package org.piccolo.node;

import org.piccolo.context.Cursor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;

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

    private int getPrecedence(String operator) {
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

    public TokenNode createIntVariable(String variableName, Cursor tokenStartPosition) {
        return createVariableDefinition(createIdentifier(variableName, tokenStartPosition),
                createPrimitiveNode(VariableType.INTEGER, tokenStartPosition),
                tokenStartPosition);
    }

    public TokenNode createStringVariable(String variableName, Cursor tokenStartPosition) {
        return createVariableDefinition(createIdentifier(variableName, tokenStartPosition),
                createPrimitiveNode(VariableType.STRING, tokenStartPosition),
                tokenStartPosition);
    }

    public TokenNode createVariableDefinition(String variableName, VariableType variableType, Cursor tokenStartPosition) {
        return createVariableDefinition(createIdentifier(variableName, tokenStartPosition),
                createPrimitiveNode(variableType, tokenStartPosition),
                tokenStartPosition);
    }

    public TokenNode createVariableDefinition(TokenNode variableName, TokenNode variableType, Cursor tokenStartPosition) {
        return new TokenNode(TokenType.VARIABLE_DEFINITION, variableName.getName(), 0,
                asList(variableName, variableType), tokenStartPosition);
    }

    public TokenNode createOperator(String operator, Cursor tokenStartPosition) {
        return new TokenNode(TokenType.OPERATOR, operator, getPrecedence(operator), new ArrayList<>(), tokenStartPosition);
    }

    public TokenNode createIdentifier(String name, Cursor tokenStartPosition) {
        return new TokenNode(TokenType.IDENTIFIER, name, 0, Collections.emptyList(), tokenStartPosition);
    }

    public TokenNode createString(String value, Cursor tokenStartPosition) {
        return createLiteral(value, VariableType.STRING, tokenStartPosition);
    }

    public TokenNode createInteger(int value, Cursor tokenStartPosition) {
        // TODO Default initialization should not always be required
        return createLiteral(String.valueOf(value), VariableType.INTEGER, tokenStartPosition);
    }

    private TokenNode createLiteral(String value, VariableType variableType, Cursor tokenStartPosition) {
        return new TokenNode(value, variableType, tokenStartPosition);
    }

    public TokenNode createPrimitiveNode(VariableType type, Cursor tokenStartPosition) {
        return new TokenNode(TokenType.VARIABLE_TYPE, type, tokenStartPosition);
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

    public ExpressionNode createExpression(Cursor tokenStartPosition) {
        return new ExpressionNode(tokenStartPosition);
    }

    public TokenNode createModule(TokenType tokenType, List<TokenNode> children) {
        return new TokenNode(tokenType, "", 0, children, children.isEmpty() ? null : children.get(0).getTokenStartPosition());
    }
}
