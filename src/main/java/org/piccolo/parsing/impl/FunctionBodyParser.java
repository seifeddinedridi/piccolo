package org.piccolo.parsing.impl;

import java.util.ArrayList;
import java.util.List;
import org.piccolo.node.FunctionBodyNode;
import org.piccolo.node.TokenType;
import org.piccolo.parsing.Parser;
import org.piccolo.parsing.context.ParsingContext;
import org.piccolo.parsing.exception.ParsingException;
import org.piccolo.node.TokenNode;
import org.piccolo.parsing.util.ParsingUtils;

class FunctionBodyParser implements Parser<FunctionBodyNode> {

    private final ExpressionParser expressionParser;

    public FunctionBodyParser() {
        this.expressionParser = new ExpressionParser();
    }

    public FunctionBodyNode parse(ParsingContext context, String codeStr) throws ParsingException {
        matchStartingCharacter(context, codeStr, '{');
        List<TokenNode> expressionList = new ArrayList<>();
        TokenNode returnNode = null;
        while (!matchEnd(context, codeStr) && !context.hasErrors()) {
            TokenNode expressionNode = expressionParser.parse(context, codeStr);
            if (expressionNode != null) {
                if (expressionNode.getType() == TokenType.RETURN_ACTION) {
                    if (returnNode != null) {
                        context.reportError("Found multiple return expressions");
                    }
                    returnNode = expressionNode;
                }
                expressionList.add(expressionNode);
            }
        }
        if (context.hasErrors() || context.getCurrentColumn() >= codeStr.length()) {
            context.reportError("Function must end with '}'");
        }
        return new FunctionBodyNode(expressionList);
    }

    @Override
    public boolean matchEnd(ParsingContext context, String codeStr) {
        if (ParsingUtils.isSkippeableCharacter(codeStr.charAt(context.getCurrentColumn()))) {
            context.skipNonParsableCharacter(codeStr);
            context.nextColumn(codeStr);
        }
        return context.getCurrentColumn() >= codeStr.length() || codeStr.charAt(context.getCurrentColumn()) == '}';
    }
}
