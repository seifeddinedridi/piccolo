package org.piccolo.parser;

import org.junit.jupiter.api.Test;
import org.piccolo.api.ExpressionApi;
import org.piccolo.context.ErrorListener;
import org.piccolo.context.ParsingContext;
import org.piccolo.exception.ParsingException;
import org.piccolo.node.TokenNode;
import org.piccolo.parser.impl.ExpressionParser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ExpressionParserTest {

    private final ParsingContext context = new ParsingContext(new ErrorListener());
    private final ExpressionParser expressionParser = new ExpressionParser();
    private final ExpressionApi api = new ExpressionApi();

    @Test
    public void testAdditionOperator() {
        TokenNode expectedNode = api.addOperand(1).applyOperation("+").addOperand(2).build();
        TokenNode node = expressionParser.parse(context, "1+2;");
        assertEquals(expectedNode, node);
    }

    @Test
    public void testMultiplicationOperator() {
        TokenNode expectedNode = api.addOperand(1).applyOperation("*").addOperand(2).build();
        TokenNode node = expressionParser.parse(context, "1*2;");
        assertEquals(expectedNode, node);
    }

    @Test
    public void testOperatorPrecedence() {
        TokenNode expectedNode = api.addOperand(1).applyOperation("+")
                .addOperand(2).applyOperation("*").addOperand(3).build();
        TokenNode node = expressionParser.parse(context, "1+2*3;");
        assertEquals(expectedNode, node);
    }

    @Test
    public void testAdditionOperatorUsingIdentifiers() {
        String codeStr = "int x; int y; int z = x + y;";
        TokenNode node = expressionParser.parse(context, codeStr);
        assertEquals(api.intVariable("x").build(), node);

        node = expressionParser.parse(context, codeStr);
        assertEquals(api.intVariable("y").build(), node);

        node = expressionParser.parse(context, codeStr);
        TokenNode expectedNode = api.intVariable("z")
                .applyOperation("=")
                .addOperand("x")
                .applyOperation("+")
                .addOperand("y")
                .build();
        assertEquals(expectedNode, node);
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
