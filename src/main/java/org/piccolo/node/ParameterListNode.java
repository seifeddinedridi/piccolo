package org.piccolo.node;

import java.util.List;

public class ParameterListNode extends TokenNode {

    public ParameterListNode(List<TokenNode> children) {
        super(TokenType.PARAMETER_LIST, "", 0, children, children.isEmpty() ? null : children.get(0).getTokenStartPosition());
    }

    public TokenNode getParameter(int index) {
        return children.get(index);
    }
}
