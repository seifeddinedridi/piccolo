package org.piccolo.parsing;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.piccolo.node.TokenNode;
import org.piccolo.node.TokenType;
import org.piccolo.parsing.context.CompilationErrorListener;
import org.piccolo.parsing.context.ParsingContext;
import org.piccolo.parsing.exception.ParsingException;
import org.piccolo.parsing.impl.ExpressionParser;
import org.piccolo.parsing.util.TokenNodeMatcher;

public class ExpressionParserTest {

    private final ParsingContext context = new ParsingContext(new CompilationErrorListener());
    private final ExpressionParser expressionParser = new ExpressionParser();

    @Test
    public void testAdditionOperator() {
        TokenNode node = expressionParser.parse(context, "1+2;");
        assertThat(node, new TokenNodeMatcher(TokenType.OPERATOR, "+"));
        assertNotNull(node.getChildren());
        assertEquals(2, node.getChildren().size());
        assertThat(node.getChildren().get(0), new TokenNodeMatcher(TokenType.LITERAL, "1"));
        assertThat(node.getChildren().get(1), new TokenNodeMatcher(TokenType.LITERAL, "2"));
    }

    @Test
    public void testMultiplicationOperator() {
        TokenNode node = expressionParser.parse(context, "1*2;");
        assertThat(node, new TokenNodeMatcher(TokenType.OPERATOR, "*"));
        assertNotNull(node.getChildren());
        assertEquals(2, node.getChildren().size());
        assertThat(node.getChildren().get(0), new TokenNodeMatcher(TokenType.LITERAL, "1"));
        assertThat(node.getChildren().get(1), new TokenNodeMatcher(TokenType.LITERAL, "2"));
    }

    @Test
    public void testOperatorPrecedence() {
        TokenNode node = expressionParser.parse(context, "1+2*3;");
        assertThat(node, new TokenNodeMatcher(TokenType.OPERATOR, "+"));
        assertNotNull(node.getChildren());
        assertEquals(2, node.getChildren().size());
        assertThat(node.getChildren().get(0), new TokenNodeMatcher(TokenType.LITERAL, "1"));
        assertThat(node.getChildren().get(1), new TokenNodeMatcher(TokenType.OPERATOR, "*"));
        assertEquals(2, node.getChildren().get(1).getChildren().size());
        assertThat(node.getChildren().get(1).getChildren().get(0), new TokenNodeMatcher(TokenType.LITERAL, "2"));
        assertThat(node.getChildren().get(1).getChildren().get(1), new TokenNodeMatcher(TokenType.LITERAL, "3"));
    }

    @Test
    public void testAdditionOperatorUsingIdentifiers() {
        String codeStr = "int x; int y; int z = x + y;";
        TokenNode xNode = expressionParser.parse(context, codeStr);
        assertThat(xNode, new TokenNodeMatcher(TokenType.VARIABLE_DEFINITION, "x"));

        TokenNode yNode = expressionParser.parse(context, codeStr);
        assertThat(yNode, new TokenNodeMatcher(TokenType.VARIABLE_DEFINITION, "y"));

        TokenNode node = expressionParser.parse(context, codeStr);
        assertThat(node, new TokenNodeMatcher(TokenType.OPERATOR, "="));
        assertNotNull(node.getChildren());
        assertEquals(2, node.getChildren().size());
        assertThat(node.getChildren().get(0), new TokenNodeMatcher(TokenType.VARIABLE_DEFINITION, "z"));
        assertThat(node.getChildren().get(1), new TokenNodeMatcher(TokenType.OPERATOR, "+"));
        assertEquals(2, node.getChildren().get(1).getChildren().size());
        assertThat(node.getChildren().get(1).getChildren().get(0), new TokenNodeMatcher(TokenType.IDENTIFIER, "x"));
        assertThat(node.getChildren().get(1).getChildren().get(1), new TokenNodeMatcher(TokenType.IDENTIFIER, "y"));
    }

    @Test
    public void testInvalidExpression() {
        assertThrows(ParsingException.class, () -> expressionParser.parse(context, "1+2"));
    }

    @Test
    public void testParsingFunctionAsAnExpression() {
        assertThrows(ParsingException.class, () -> expressionParser.parse(context, "int hello() {}"));
    }

    @Test
    public void testInvalidOperationUnaryOperator() {
        assertThrows(ParsingException.class, () -> expressionParser.parse(context, "int x++"));
    }

    @Test
    public void testInvalidOperationOperatorNotPrecededByAnAssignableToken() {
        ParsingException ex = assertThrows(ParsingException.class, () -> expressionParser.parse(context, "=;"));
        assertEquals("Expected a literal or an identifier but found NULL. Error at line=1, position=0", ex.getMessage());
    }

    @Test
    public void testInvalidOperationUnexpectedFunctionDefinition() {
        ParsingException ex = assertThrows(ParsingException.class, () -> expressionParser.parse(context, "int f()"));
        assertEquals("Found unexpected function definition. Error at line=1, position=4", ex.getMessage());
    }

    @Test
    public void testInvalidOperationInvalidVariableDefinition() {
        ParsingException ex = assertThrows(ParsingException.class, () -> expressionParser.parse(context, "int x+;"));
        assertEquals("Invalid variable definition. Error at line=1, position=4", ex.getMessage());
    }

    @Test
    public void testInvalidOperationInvalidCharacter() {
        ParsingException ex = assertThrows(ParsingException.class, () -> expressionParser.parse(context, "int x{};"));
        assertEquals("Found unexpected character `{`. Error at line=1, position=5", ex.getMessage());
    }
}
