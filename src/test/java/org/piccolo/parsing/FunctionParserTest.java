package org.piccolo.parsing;

import static com.jayway.jsonassert.JsonAssert.with;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.piccolo.node.FunctionNode;
import org.piccolo.node.TokenType;
import org.piccolo.context.ErrorListener;
import org.piccolo.context.ParsingContext;
import org.piccolo.exception.ParsingException;
import org.piccolo.parsing.impl.FunctionParser;
import org.piccolo.util.TokenNodeUtil;
import org.piccolo.util.TokenJsonNodeMatcher;

public class FunctionParserTest {

    private final ParsingContext context = new ParsingContext(new ErrorListener());
    private final FunctionParser functionParser = new FunctionParser();

    @Test
    public void parseFunction_ReturnsTokenNode_WhenFunctionHasEmptyBody() {
        FunctionNode functionDefinition = functionParser.parse(context, "void helloWorld() {}");
        assertNotNull(functionDefinition);

        String json = TokenNodeUtil.toJson(functionDefinition);
        with(json)
            .assertThat("$", new TokenJsonNodeMatcher(TokenType.FUNCTION_DEFINITION, ""))
            .assertThat("$.children", hasSize(2))
            .assertThat("$.children[0]", new TokenJsonNodeMatcher(TokenType.FUNCTION_SIGNATURE, ""))
            .assertThat("$.children[0].children", hasSize(2))
            .assertThat("$.children[0].children[0]", new TokenJsonNodeMatcher(TokenType.VARIABLE_DEFINITION, "helloWorld"))
            .assertThat("$.children[0].children[0].children", hasSize(2))
            .assertThat("$.children[0].children[0].children[0]", new TokenJsonNodeMatcher(TokenType.IDENTIFIER, "helloWorld"))
            .assertThat("$.children[0].children[0].children[1]", new TokenJsonNodeMatcher(TokenType.VARIABLE_TYPE, "void"))
            .assertThat("$.children[0].children[1]", new TokenJsonNodeMatcher(TokenType.PARAMETER_LIST, ""))
            .assertThat("$.children[1]", new TokenJsonNodeMatcher(TokenType.FUNCTION_BODY, ""))
            .assertThat("$.children[1].children", hasSize(1))
            .assertThat("$.children[1].children[0]", new TokenJsonNodeMatcher(TokenType.RETURN_ACTION, "return"));
    }

    @Test
    public void parseFunction_ReturnsTokenNode_WhenFunctionHasNoParameters() {
        FunctionNode functionDefinition = functionParser.parse(context, "int helloWorld() { int x = 1 + 2; return x;}");
        assertNotNull(functionDefinition);
        String json = TokenNodeUtil.toJson(functionDefinition);
        with(json)
            .assertThat("$", new TokenJsonNodeMatcher(TokenType.FUNCTION_DEFINITION, ""))
            .assertThat("$.children", hasSize(2))
            .assertThat("$.children[0]", new TokenJsonNodeMatcher(TokenType.FUNCTION_SIGNATURE, ""))
            .assertThat("$.children[0].children", hasSize(2))
            .assertThat("$.children[0].children[0]", new TokenJsonNodeMatcher(TokenType.VARIABLE_DEFINITION, "helloWorld"))
            .assertThat("$.children[0].children[0].children", hasSize(2))
            .assertThat("$.children[0].children[0].children[0]", new TokenJsonNodeMatcher(TokenType.IDENTIFIER, "helloWorld"))
            .assertThat("$.children[0].children[0].children[1]", new TokenJsonNodeMatcher(TokenType.VARIABLE_TYPE, "int"))
            .assertThat("$.children[0].children[1]", new TokenJsonNodeMatcher(TokenType.PARAMETER_LIST, ""))
            .assertThat("$.children[1]", new TokenJsonNodeMatcher(TokenType.FUNCTION_BODY, ""))
            .assertThat("$.children[1].children", hasSize(2))
            .assertThat("$.children[1].children[0]", new TokenJsonNodeMatcher(TokenType.OPERATOR, "="))
            .assertThat("$.children[1].children[0].children", hasSize(2))
            .assertThat("$.children[1].children[0].children[0]", new TokenJsonNodeMatcher(TokenType.VARIABLE_DEFINITION, "x"))
            .assertThat("$.children[1].children[0].children[0].children", hasSize(2))
            .assertThat("$.children[1].children[0].children[0].children[0]", new TokenJsonNodeMatcher(TokenType.IDENTIFIER, "x"))
            .assertThat("$.children[1].children[0].children[0].children[1]", new TokenJsonNodeMatcher(TokenType.VARIABLE_TYPE, "int"))
            .assertThat("$.children[1].children[0].children[1]", new TokenJsonNodeMatcher(TokenType.OPERATOR, "+"))
            .assertThat("$.children[1].children[0].children[1].children", hasSize(2))
            .assertThat("$.children[1].children[0].children[1].children[0]", new TokenJsonNodeMatcher(TokenType.LITERAL, "1"))
            .assertThat("$.children[1].children[0].children[1].children[1]", new TokenJsonNodeMatcher(TokenType.LITERAL, "2"))
            .assertThat("$.children[1].children[1]", new TokenJsonNodeMatcher(TokenType.RETURN_ACTION, "return"));
    }

    @Test
    public void parseFunction_ReturnsTokenNode_WhenFunctionHasParameters() {
        FunctionNode functionDefinition = functionParser.parse(context, "int helloWorld(int x, int y) { int z = x + y; return z;}");
        assertNotNull(functionDefinition);
        String json = TokenNodeUtil.toJson(functionDefinition);
        with(json)
            .assertThat("$", new TokenJsonNodeMatcher(TokenType.FUNCTION_DEFINITION, ""))
            .assertThat("$.children", hasSize(2))
            .assertThat("$.children[0]", new TokenJsonNodeMatcher(TokenType.FUNCTION_SIGNATURE, ""))
            .assertThat("$.children[0].children", hasSize(2))
            .assertThat("$.children[0].children[0]", new TokenJsonNodeMatcher(TokenType.VARIABLE_DEFINITION, "helloWorld"))
            .assertThat("$.children[0].children[0].children", hasSize(2))
            .assertThat("$.children[0].children[0].children[0]", new TokenJsonNodeMatcher(TokenType.IDENTIFIER, "helloWorld"))
            .assertThat("$.children[0].children[0].children[1]", new TokenJsonNodeMatcher(TokenType.VARIABLE_TYPE, "int"))
            .assertThat("$.children[0].children[1]", new TokenJsonNodeMatcher(TokenType.PARAMETER_LIST, ""))
            .assertThat("$.children[0].children[1].children", hasSize(2))
            .assertThat("$.children[0].children[1].children[0]", new TokenJsonNodeMatcher(TokenType.VARIABLE_DEFINITION, "x"))
            .assertThat("$.children[0].children[1].children[0].children[0]", new TokenJsonNodeMatcher(TokenType.IDENTIFIER, "x"))
            .assertThat("$.children[0].children[1].children[0].children[1]", new TokenJsonNodeMatcher(TokenType.VARIABLE_TYPE, "int"))
            .assertThat("$.children[0].children[1].children[1]", new TokenJsonNodeMatcher(TokenType.VARIABLE_DEFINITION, "y"))
            .assertThat("$.children[0].children[1].children[1].children[0]", new TokenJsonNodeMatcher(TokenType.IDENTIFIER, "y"))
            .assertThat("$.children[0].children[1].children[1].children[1]", new TokenJsonNodeMatcher(TokenType.VARIABLE_TYPE, "int"))
            .assertThat("$.children[1]", new TokenJsonNodeMatcher(TokenType.FUNCTION_BODY, ""))
            .assertThat("$.children[1].children", hasSize(2))
            .assertThat("$.children[1].children[0]", new TokenJsonNodeMatcher(TokenType.OPERATOR, "="))
            .assertThat("$.children[1].children[0].children", hasSize(2))
            .assertThat("$.children[1].children[0].children[0]", new TokenJsonNodeMatcher(TokenType.VARIABLE_DEFINITION, "z"))
            .assertThat("$.children[1].children[0].children[0].children", hasSize(2))
            .assertThat("$.children[1].children[0].children[0].children[0]", new TokenJsonNodeMatcher(TokenType.IDENTIFIER, "z"))
            .assertThat("$.children[1].children[0].children[0].children[1]", new TokenJsonNodeMatcher(TokenType.VARIABLE_TYPE, "int"))
            .assertThat("$.children[1].children[0].children[1]", new TokenJsonNodeMatcher(TokenType.OPERATOR, "+"))
            .assertThat("$.children[1].children[0].children[1].children", hasSize(2))
            .assertThat("$.children[1].children[0].children[1].children[0]", new TokenJsonNodeMatcher(TokenType.IDENTIFIER, "x"))
            .assertThat("$.children[1].children[0].children[1].children[1]", new TokenJsonNodeMatcher(TokenType.IDENTIFIER, "y"))
            .assertThat("$.children[1].children[1]", new TokenJsonNodeMatcher(TokenType.RETURN_ACTION, "return"));
    }

    @Test
    public void parseFunction_ThrowsParsingException_WhenClosingParenthesisIsMissing() {
        ParsingException ex = assertThrows(ParsingException.class, () -> functionParser.parse(context, "int helloWorld( {}"));
        assertEquals("Unexpected character '{'. Error at line=1, position=16", ex.getMessage());
    }
}
