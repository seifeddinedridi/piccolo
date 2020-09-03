package org.piccolo.node;

import java.util.List;

public class FunctionBodyNode extends TokenNode {

    public FunctionBodyNode(List<TokenNode> children) {
        super(TokenType.FUNCTION_BODY, "", 0, children, null);
    }
}
