package org.piccolo.node;

import java.util.List;

public class FunctionBodyNode extends TokenNode {

    public FunctionBodyNode(List<TokenNode> children) {
        super(TokenType.FUNCTION_BODY, "", 0, children, null);
    }

    public TokenNode getReturnNode() {
        return children.size() > 0 ? children.get(children.size() - 1).children.get(0) : NULl_TOKEN;
    }
}
