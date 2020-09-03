package org.piccolo.node;

import java.util.Arrays;
import java.util.List;
import org.piccolo.parsing.context.Cursor;

public class TokenNode {

    protected final TokenType type;
    private final String name;
    protected final List<TokenNode> children;
    protected final int precedence;
    private final Cursor tokenStartPosition;

    public static final TokenNode NULl_TOKEN = new TokenNode(TokenType.NULL, null, 0, null, null);

    public TokenNode(TokenType type, String name, int precedence, List<TokenNode> children, Cursor tokenStartPosition) {
        this.type = type;
        this.name = name;
        this.precedence = precedence;
        this.children = children;
        this.tokenStartPosition = tokenStartPosition;
    }

    public void addChild(TokenNode node) {
        children.add(node);
    }

    public boolean isAssignableType() {
        return type == TokenType.IDENTIFIER
            || type == TokenType.LITERAL
            || type == TokenType.VARIABLE_DEFINITION
            || type == TokenType.OPERATOR
            || type == TokenType.RETURN_ACTION;
    }

    public boolean isOperand() {
        return type == TokenType.LITERAL || type == TokenType.IDENTIFIER
            || type == TokenType.VARIABLE_DEFINITION;
    }

    public boolean matchesTypes(TokenType... types) {
        return Arrays.asList(types).contains(type);
    }

    public TokenType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public List<TokenNode> getChildren() {
        return children;
    }

    public int getPrecedence() {
        return precedence;
    }

    @Override
    public String toString() {
        return "TokenNode{" +
            "type=" + type +
            ", name='" + name + '\'' +
            '}';
    }

    public Cursor getTokenStartPosition() {
        return tokenStartPosition;
    }
}
