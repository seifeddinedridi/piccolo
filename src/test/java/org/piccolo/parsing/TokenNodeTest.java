package org.piccolo.parsing;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.piccolo.node.TokenNode;
import org.piccolo.node.TokenNodeFactory;
import org.piccolo.node.TokenType;
import org.piccolo.parsing.context.CompilationErrorListener;
import org.piccolo.parsing.context.ParsingContext;
import org.piccolo.parsing.util.TokenNodeMatcher;

public class TokenNodeTest {

    private final ParsingContext context = new ParsingContext(new CompilationErrorListener());
    private final TokenNodeFactory factory = TokenNodeFactory.getInstance();

    @Test
    public void testMultiplicationHasHigherPrecedenceOverAddition() {
        TokenNode expression = factory.createExpression(context.getCursor());
        expression.addChild(factory.createLiteral("1", context.getCursor()));
        expression.addChild(factory.createOperator("+", context.getCursor()));
        expression.addChild(factory.createLiteral("2", context.getCursor()));
        expression.addChild(factory.createOperator("*", context.getCursor()));
        expression.addChild(factory.createLiteral("3", context.getCursor()));
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
        expression.addChild(factory.createLiteral("1", context.getCursor()));
        expression.addChild(factory.createOperator("+", context.getCursor()));
        expression.addChild(factory.createLiteral("2", context.getCursor()));
        expression.addChild(factory.createOperator("*", context.getCursor()));
        expression.addChild(factory.createLiteral("3", context.getCursor()));
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
