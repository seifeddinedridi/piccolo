package org.piccolo.context;

import org.piccolo.node.FunctionNode;
import org.piccolo.node.FunctionSignatureNode;
import org.piccolo.node.TokenNode;
import org.piccolo.node.TokenNodeFactory;
import org.piccolo.node.TokenType;
import org.piccolo.exception.ParsingException;
import org.piccolo.util.ParsingUtils;

import java.util.HashMap;
import java.util.Map;

public class ParsingContext {

    private final ErrorListener errorListener;
    private final StringBuilder tokenBuffer;
    private final Map<String, TokenNode> variableDefinitions;
    private final Map<String, TokenNode> functionDefinitions;
    private final TokenNodeFactory nodeFactory;
    private Cursor cursor;
    private ParsingContext parentContext;
    private TokenNode previousNode = TokenNode.NULl_TOKEN;
    private Cursor tokenStartPosition;

    public ParsingContext(ErrorListener errorListener) {
        this.cursor = new Cursor();
        this.nodeFactory = TokenNodeFactory.getInstance();
        this.variableDefinitions = new HashMap<>();
        this.functionDefinitions = new HashMap<>();
        this.tokenBuffer = new StringBuilder();
        this.errorListener = errorListener;
    }

    public ParsingContext(ParsingContext parentContext) {
        this(parentContext.errorListener);
        this.cursor = parentContext.cursor;
        this.parentContext = parentContext;
    }

    public void skipSpace(String codeStr) {
        while (cursor.getCurrentColumn() < codeStr.length() && codeStr.charAt(cursor.getCurrentColumn()) == ' ') {
            cursor.nextColumn(codeStr.charAt(cursor.getCurrentColumn()));
        }
    }

    public void skipNonParsableCharacter(String codeStr) {
        int skippedChars = 0;
        while (cursor.getCurrentColumn() < codeStr.length() && ParsingUtils
                .canSkip(codeStr.charAt(cursor.getCurrentColumn()))) {
            cursor.nextColumn(codeStr.charAt(cursor.getCurrentColumn()));
            skippedChars++;
        }
        if (skippedChars > 0) {
            // Previous character is important to detect the boundaries between the tokens
            cursor.previousColumn();
        }
    }

    public void nextColumn(String codeStr) {
        if (cursor.getCurrentColumn() < codeStr.length()) {
            cursor.nextColumn(codeStr.charAt(cursor.getCurrentColumn()));
        }
    }

    public int getCurrentColumn() {
        return cursor.getCurrentColumn();
    }

    public void clearBuffer() {
        tokenBuffer.delete(0, tokenBuffer.length());
    }

    public TokenNode createNodeForCurrentToken() {
        String token = tokenBuffer.toString();
        clearBuffer();
        TokenNode node = null;
        if (!token.isEmpty()) {
            if (ParsingUtils.isType(token)) {
                node = nodeFactory.createPrimitiveType(token, tokenStartPosition);
            } else if (ParsingUtils.isIdentifier(token)) {
                node = createIdentifier(token);
            } else if (ParsingUtils.isNumber(token)) {
                node = nodeFactory.createLiteral(token, tokenStartPosition);
            } else if (ParsingUtils.isOperator(token.charAt(0))) {
                node = nodeFactory.createOperator(token, tokenStartPosition);
            }
        }
        validateStructure(previousNode, node);
        if (node != null) {
            previousNode = node;
        }
        return node;
    }

    public TokenNode createReturnVoidAction() {
        return nodeFactory.createReturnVoidAction(tokenStartPosition);
    }

    private void validateStructure(TokenNode previousNode, TokenNode currentNode) {
        if (previousNode.isOperand() && currentNode != null && currentNode.isOperand()) {
            // Two successive literal nodes are not allowed
            reportError("Expected operator but found " + currentNode.getType());
        } else if (previousNode.getType() == TokenType.OPERATOR && currentNode != null && currentNode.getType() == TokenType.OPERATOR) {
            // Two consecutive operator nodes are not allowed
            reportError("Expected literal or identifier but found " + currentNode.getType());
        }
    }

    private TokenNode createIdentifier(String token) {
        TokenNode node;
        if (previousNode.getType() == TokenType.VARIABLE_TYPE) {
            if (containsLocalVariable(token)) {
                reportError("Variable `" + token + "` was already defined");
            }
            if (ParsingUtils.isReservedKeyword(token)) {
                reportError("Keyword `" + token + "` cannot be used as a variable name");
            }
            TokenNode identifier = nodeFactory.createIdentifier(token, tokenStartPosition);
            node = nodeFactory.createVariableDefinition(identifier, previousNode, previousNode.getTokenStartPosition());
        } else if (token.equals("return")) {
            return nodeFactory.createReturnAction(previousNode.getTokenStartPosition());
        } else {
            if (!containsVariable(token)) {
                reportError("Usage of undefined variable `" + token + "`");
            }
            node = nodeFactory.createIdentifier(token, tokenStartPosition);
        }
        return node;
    }

    public void appendChar(char c) {
        if (tokenBuffer.length() == 0) {
            tokenStartPosition = cursor.copy();
        }
        tokenBuffer.append(c);
    }

    public boolean hasErrors() {
        return errorListener.hasErrors();
    }

    public void reportError(String errorMessage) {
        if (tokenStartPosition != null) {
            errorListener.reportError(errorMessage, tokenStartPosition.getCurrentLine(), tokenStartPosition.getColumnOffset());
        } else {
            errorListener.reportError(errorMessage, cursor.getCurrentLine(), cursor.getColumnOffset());
        }
        throw new ParsingException(this);
    }

    public boolean containsLocalVariable(String varName) {
        return variableDefinitions.containsKey(varName);
    }

    public boolean containsVariable(String varName) {
        return variableDefinitions.containsKey(varName) || (parentContext != null && parentContext.containsVariable(varName));
    }

    public void registerVariableDefinition(TokenNode variableDefinition) {
        if (containsLocalVariable(variableDefinition.getName())) {
            reportError("Variable with name '" + variableDefinition.getName() + "' was already defined");
        }
        variableDefinitions.put(variableDefinition.getName(), variableDefinition);
    }

    public void registerFunction(FunctionNode functionNode) {
        String functionName = functionNode.getFunctionSignature().getFunctionName();
        if (functionDefinitions.containsKey(functionName)) {
            reportError("Function with name '" + functionName + "' was already defined");
        }
        functionDefinitions.put(functionName, functionNode);
    }

    @Override
    public String toString() {
        return errorListener.toString();
    }

    public void moveToCursor(Cursor tokenStartPosition) {
        this.cursor.copyFrom(tokenStartPosition);
        this.previousNode = TokenNode.NULl_TOKEN;
        this.tokenStartPosition = null;
    }

    public Cursor getCursor() {
        return cursor;
    }

    public TokenNode getPreviousNode() {
        return previousNode;
    }

    public void clearTokenStartPosition() {
        this.tokenStartPosition = null;
    }

    public boolean isRegistered(FunctionSignatureNode functionSignature) {
        if (parentContext != null) {
            return parentContext.isRegistered(functionSignature);
        }
        return functionDefinitions.containsKey(functionSignature.getFunctionName());
    }

    public TokenNode getVariableRef(String name) {
        if (!containsVariable(name)) {
            return null;
        }
        return variableDefinitions.get(name);
    }
}
