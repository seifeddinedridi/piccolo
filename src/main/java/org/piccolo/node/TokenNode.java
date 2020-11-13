package org.piccolo.node;

import org.piccolo.parsing.context.Cursor;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class TokenNode {

    public static final TokenNode NULl_TOKEN = new TokenNode(TokenType.NULL, null, 0, null, null);
    protected final TokenType type;
    protected final List<TokenNode> children;
    protected final int precedence;
    private final String name;
    private final Cursor tokenStartPosition;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TokenNode tokenNode = (TokenNode) o;

        if (type != tokenNode.type) return false;
        return true;
//        if (!Objects.equals(name, tokenNode.name)) return false;
//        return Objects.equals(children, tokenNode.children);
    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (children != null ? children.hashCode() : 0);
        return result;
    }
}
