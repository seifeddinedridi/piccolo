package org.piccolo.util;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.piccolo.node.TokenNode;
import org.piccolo.node.TokenType;

public class TokenNodeMatcher extends BaseMatcher<TokenNode> {

    private final TokenNode expectedTokenNode;

    public TokenNodeMatcher(TokenType type, String name) {
        this.expectedTokenNode = new TokenNode(type, name, 0, null, null);
    }

    @Override
    public boolean matches(Object obj) {
        if (!(obj instanceof TokenNode)) {
            return false;
        }
        TokenNode tokenNode = (TokenNode) obj;
        return expectedTokenNode.getName().equals(tokenNode.getName()) && expectedTokenNode.getType().equals(tokenNode.getType());
    }

    @Override
    public void describeTo(Description description) {
        description
            .appendText("Matches TokenNode properties: name=" + expectedTokenNode.getName() + ", tokenType=" + expectedTokenNode.getType());
    }
}
