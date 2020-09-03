package org.piccolo.node;

import static java.util.Arrays.asList;

public class FunctionSignatureNode extends TokenNode {

    public FunctionSignatureNode(TokenNode functionName, ParameterListNode parameterListNode) {
        super(TokenType.FUNCTION_SIGNATURE, "", 0, asList(functionName, parameterListNode), functionName.getTokenStartPosition());
    }

    public String getFunctionName() {
        return children.get(0).getChildren().get(0).getName();
    }

    public String getFunctionReturnType() {
        return children.get(0).getChildren().get(1).getName();
    }

    public ParameterListNode getParameterListNode() {
        return (ParameterListNode) children.get(1);
    }
}
