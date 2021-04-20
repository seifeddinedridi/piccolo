package org.piccolo.node;

import org.piccolo.context.Cursor;
import org.piccolo.context.ParsingContext;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TokenNode {

    public static final TokenNode NULl_TOKEN = new TokenNode(TokenType.NULL, null, 0, null, null);

    protected final TokenType type;
    protected VariableType variableType;
    protected final List<TokenNode> children;
    protected final int precedence;
    private final String name;
    private final Cursor tokenStartPosition;

    public TokenNode(TokenType type, VariableType variableType, Cursor tokenStartPosition) {
        this(type, "", 0, Collections.emptyList(), tokenStartPosition);
        this.variableType = variableType;
    }

    public TokenNode(String value, VariableType variableType, Cursor tokenStartPosition) {
        this(TokenType.LITERAL, value, 0, Collections.emptyList(), tokenStartPosition);
        this.variableType = variableType;
    }

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

    public String getNodeReturnType(ParsingContext context) {
        switch (type) {
            case VARIABLE_DEFINITION:
                return children.get(0).getName();
            case LITERAL:
                return getVariableType();
            case IDENTIFIER:
                return context.getVariableRef(name).getChildren().get(1).getName();
            case OPERATOR:
                if (children.size() == 1) {
                    if (children.get(0).getType() == TokenType.IDENTIFIER) {
                        return context.getVariableRef(children.get(1).getName()).getChildren().get(0).getName();
                    } else if (children.get(0).getType() == TokenType.LITERAL) {
                        return children.get(0).getVariableType();
                    } else if (children.get(0).getType() == TokenType.OPERATOR) {
                        return getNodeReturnType(context);
                    }
                } else if (children.size() == 2) {
                    String leftSideType = children.get(0).getNodeReturnType(context);
//                    String rightSideType = children.get(1).getNodeReturnType(context);
                    return leftSideType;
                }
        }
        return "";
    }

    public String getVariableType() {
        return variableType.toString();
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
