package org.piccolo.parser.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.piccolo.node.TokenNode;
import org.piccolo.node.TokenNodeFactory;
import org.piccolo.node.TokenType;
import org.piccolo.parser.Parser;
import org.piccolo.context.ParsingContext;
import org.piccolo.exception.ParsingException;
import org.piccolo.util.ParsingUtils;

public class LanguageParser implements Parser {

    private final ArrayList<Parser> parsers;
    private static final TokenNodeFactory nodeFactory = TokenNodeFactory.getInstance();

    public LanguageParser() {
        this.parsers = new ArrayList<>();
        this.parsers.add(new FunctionParser());
        this.parsers.add(new ExpressionParser());
    }

    public TokenNode parse(ParsingContext context, String codeStr) throws ParsingException {
        List<TokenNode> astNodes = new ArrayList<>();
        TokenNode previousNode = TokenNode.NULl_TOKEN;
        while (!matchEnd(context, codeStr) && !context.hasErrors()) {
            char currentChar = codeStr.charAt(context.getCurrentColumn());
            if (ParsingUtils.isOperator(currentChar) || ParsingUtils.isSeparator(currentChar)) {
                TokenNode node = context.createNodeForCurrentToken();
                if (node != null) {
                    previousNode = node;
                }
            }
            Iterator<Parser> iterator = parsers.iterator();
            Parser parser = null;
            while (iterator.hasNext()) {
                Parser candidateParser = iterator.next();
                if (candidateParser.match(previousNode, currentChar)) {
                    parser = candidateParser;
                    break;
                }
            }
            if (parser != null) {
                context.moveToCursor(previousNode.getTokenStartPosition());
                TokenNode tokenNode = parser.parse(context, codeStr);
                astNodes.add(tokenNode);
                previousNode = TokenNode.NULl_TOKEN;
            } else if (!ParsingUtils.canSkip(currentChar)) {
                context.appendChar(currentChar);
            }
            context.nextColumn(codeStr);
            context.skipNonParsableCharacter(codeStr);
        }
        if (context.hasErrors()) {
            return null;
        }
        return nodeFactory.createModule(TokenType.MODULE, astNodes);
    }

    @Override
    public boolean matchEnd(ParsingContext context, String codeStr) {
        return context.getCurrentColumn() >= codeStr.length();
    }
}