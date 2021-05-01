package org.piccolo.parser;

import org.junit.jupiter.api.Test;
import org.piccolo.api.ExpressionApi;
import org.piccolo.api.FunctionApi;
import org.piccolo.context.ErrorListener;
import org.piccolo.context.ParsingContext;
import org.piccolo.exception.ParsingException;
import org.piccolo.node.FunctionNode;
import org.piccolo.node.VariableType;
import org.piccolo.parser.impl.FunctionParser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FunctionParserTest {

    private final ParsingContext context = new ParsingContext(new ErrorListener());
    private final FunctionParser functionParser = new FunctionParser();
    private final FunctionApi functionApi = new FunctionApi();

    @Test
    public void parseFunction_ReturnsTokenNode_WhenFunctionHasEmptyBody() {
        FunctionNode functionDefinition = functionParser.parse(context, "void helloWorld() {}");
        assertNotNull(functionDefinition);
        FunctionNode expected = functionApi
                .header()
                .name("helloWorld").returnType(VariableType.VOID)
                .body()
                .returnStatement()
                .function().build();
        assertEquals(expected, functionDefinition);
    }

    @Test
    public void parseFunction_ReturnsTokenNode_WhenFunctionHasNoParameters() {
        FunctionNode functionDefinition = functionParser.parse(context, "int helloWorld() { int x = 1 + 2; return x;}");
        assertNotNull(functionDefinition);
        FunctionNode expected = functionApi.header().returnType(VariableType.INTEGER).name("helloWorld")
                .body()
                .add(new ExpressionApi().variable("x", VariableType.INTEGER)
                        .applyOperation("=")
                        .addOperand(1)
                        .applyOperation("+")
                        .addOperand(2)
                        .build())
                .returnStatement(new ExpressionApi().identifier("x").build())
                .function().build();
        assertEquals(expected, functionDefinition);
    }

    @Test
    public void parseFunction_ReturnsTokenNode_WhenFunctionHasParameters() {
        FunctionNode functionDefinition = functionParser.parse(context, "int helloWorld(int x, int y) { int z = x + y; return z;}");
        assertNotNull(functionDefinition);
        FunctionNode expected = functionApi.header().returnType(VariableType.INTEGER).name("helloWorld")
                .parameter("x", VariableType.INTEGER)
                .parameter("y", VariableType.INTEGER)
                .body()
                .add(new ExpressionApi().variable("z", VariableType.INTEGER)
                        .applyOperation("=")
                        .addOperand("x")
                        .applyOperation("+")
                        .addOperand("y")
                        .build())
                .returnStatement(new ExpressionApi().identifier("z").build())
                .function().build();
        assertEquals(expected, functionDefinition);
    }

    @Test
    public void parseFunction_ThrowsParsingException_WhenClosingParenthesisIsMissing() {
        ParsingException ex = assertThrows(ParsingException.class, () -> functionParser.parse(context, "int helloWorld( {}"));
        assertEquals("Unexpected character '{'. Error at line=1, position=16", ex.getMessage());
    }
}
