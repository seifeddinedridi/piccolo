package org.piccolo.parsing.impl;

import org.piccolo.node.FunctionBodyNode;
import org.piccolo.node.FunctionSignatureNode;
import org.piccolo.node.TokenNode;
import org.piccolo.node.TokenType;
import org.piccolo.parsing.Parser;
import org.piccolo.parsing.context.ParsingContext;
import org.piccolo.parsing.exception.ParsingException;
import org.piccolo.parsing.util.ParsingUtils;
import org.piccolo.node.FunctionNode;
import org.piccolo.node.ParameterListNode;

public class FunctionParser implements Parser<FunctionNode> {

    private final ParameterListParser parameterListParser;
    private final FunctionBodyParser functionBodyParser;

    public FunctionParser() {
        this.parameterListParser = new ParameterListParser();
        this.functionBodyParser = new FunctionBodyParser();
    }

    @Override
    public FunctionNode parse(ParsingContext parentContext, String codeStr) throws ParsingException {
        ParsingContext context = new ParsingContext(parentContext);
        FunctionNode functionNode = null;
        TokenNode previousNode = TokenNode.NULl_TOKEN;
        while (!matchEnd(context, codeStr) && !context.hasErrors() && functionNode == null) {
            char currentChar = codeStr.charAt(context.getCurrentColumn());
            if (ParsingUtils.isSeparator(currentChar) || ParsingUtils.isExpressionTerminator(currentChar)) {
                TokenNode node = context.createNodeForCurrentToken();
                if (node != null) {
                    previousNode = node;
                }
            } else {
                context.appendChar(currentChar);
            }
            if (match(previousNode, currentChar)) {
                // Function definition
                ParsingContext childContext = new ParsingContext(context);
                ParameterListNode functionParameters = parameterListParser.parse(childContext, codeStr);
                FunctionSignatureNode functionSignature = new FunctionSignatureNode(previousNode, functionParameters);
                if (context.isRegistered(functionSignature)) {
                    context.moveToCursor(previousNode.getTokenStartPosition());
                    context.reportError("Function with name '" + functionSignature.getFunctionName() + "' was already defined");
                }
                FunctionBodyNode functionBody = functionBodyParser.parse(childContext, codeStr);
                TokenNode returnNode = functionBody.getReturnNode();
                TokenNode functionReturnType = functionSignature.getFunctionReturnType();
                if (!functionReturnType.equals(returnNode)) {
                    childContext.moveToCursor(returnNode.getTokenStartPosition());
                    childContext.reportError("Return type '" + returnNode.getName()
                            + "' does not match function's return type '" + functionReturnType + "'");
                }
                functionNode = new FunctionNode(functionSignature, functionBody);
            } else {
                context.nextColumn(codeStr);
                context.skipNonParsableCharacter(codeStr);
            }
        }
        if (context.hasErrors() || context.getCurrentColumn() >= codeStr.length()) {
            return null;
        }
        if (functionNode != null) {
            parentContext.registerFunction(functionNode);
        }
        return functionNode;
    }

    @Override
    public boolean matchEnd(ParsingContext context, String codeStr) {
        return context.getCurrentColumn() >= codeStr.length();
    }

    @Override
    public boolean match(TokenNode previousNode, char currentChar) {
        return previousNode.getType() == TokenType.VARIABLE_DEFINITION && currentChar == '(';
    }
}
