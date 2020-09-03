package org.piccolo.node;

import static java.util.Arrays.asList;

public class FunctionNode extends TokenNode {

    public FunctionNode(FunctionSignatureNode functionSignature, FunctionBodyNode functionBody) {
        super(TokenType.FUNCTION_DEFINITION, "", 0, asList(functionSignature, functionBody), functionSignature.getTokenStartPosition());
    }

    public FunctionSignatureNode getFunctionSignature() {
        return (FunctionSignatureNode) children.get(0);
    }

    public FunctionBodyNode getFunctionBody() {
        return (FunctionBodyNode) children.get(1);
    }
}
