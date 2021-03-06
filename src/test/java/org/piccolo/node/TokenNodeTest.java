package org.piccolo.node;

import org.junit.jupiter.api.Test;
import org.piccolo.context.ErrorListener;
import org.piccolo.context.ParsingContext;
import org.piccolo.util.TokenNodeMatcher;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TokenNodeTest {

    private final ParsingContext context = new ParsingContext(new ErrorListener());
    private final TokenNodeFactory factory = TokenNodeFactory.getInstance();

    @Test
    public void testMultiplicationHasHigherPrecedenceOverAddition() {
        TokenNode expression = factory.createExpression(context.getCursor());
        expression.addChild(factory.createInteger(1, context.getCursor()));
        expression.addChild(factory.createOperator("+", context.getCursor()));
        expression.addChild(factory.createInteger(2, context.getCursor()));
        expression.addChild(factory.createOperator("*", context.getCursor()));
        expression.addChild(factory.createInteger(3, context.getCursor()));
        TokenNode rootNode = expression.getChildren().get(0);
        assertThat(rootNode, new TokenNodeMatcher(TokenType.OPERATOR, "+"));
        assertNotNull(rootNode.getChildren());
        assertEquals(2, rootNode.getChildren().size());
        assertThat(rootNode.getChildren().get(0), new TokenNodeMatcher(TokenType.LITERAL, "1"));
        assertThat(rootNode.getChildren().get(1), new TokenNodeMatcher(TokenType.OPERATOR, "*"));
        assertEquals(2, rootNode.getChildren().get(1).getChildren().size());
        assertThat(rootNode.getChildren().get(1).getChildren().get(0), new TokenNodeMatcher(TokenType.LITERAL, "2"));
        assertThat(rootNode.getChildren().get(1).getChildren().get(1), new TokenNodeMatcher(TokenType.LITERAL, "3"));
    }

    @Test
    public void testSimpleReturnStatement() {
        TokenNode expression = factory.createExpression(context.getCursor());
        expression.addChild(factory.createReturnAction(context.getCursor()));
        expression.addChild(factory.createInteger(1, context.getCursor()));
        expression.addChild(factory.createOperator("+", context.getCursor()));
        expression.addChild(factory.createInteger(2, context.getCursor()));
        expression.addChild(factory.createOperator("*", context.getCursor()));
        expression.addChild(factory.createInteger(3, context.getCursor()));
        TokenNode rootNode = expression.getChildren().get(0);
        assertThat(rootNode, new TokenNodeMatcher(TokenType.RETURN_ACTION, "return"));
        assertNotNull(rootNode.getChildren());
        assertEquals(1, rootNode.getChildren().size());
        assertThat(rootNode.getChildren().get(0), new TokenNodeMatcher(TokenType.OPERATOR, "+"));
        assertEquals(2, rootNode.getChildren().get(0).getChildren().size());
        assertThat(rootNode.getChildren().get(0).getChildren().get(0), new TokenNodeMatcher(TokenType.LITERAL, "1"));
        assertThat(rootNode.getChildren().get(0).getChildren().get(1), new TokenNodeMatcher(TokenType.OPERATOR, "*"));
        assertEquals(2, rootNode.getChildren().get(0).getChildren().get(1).getChildren().size());
        assertThat(rootNode.getChildren().get(0).getChildren().get(1).getChildren().get(0), new TokenNodeMatcher(TokenType.LITERAL, "2"));
        assertThat(rootNode.getChildren().get(0).getChildren().get(1).getChildren().get(1), new TokenNodeMatcher(TokenType.LITERAL, "3"));
    }
}
