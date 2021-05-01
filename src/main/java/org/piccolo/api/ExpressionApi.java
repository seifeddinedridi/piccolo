package org.piccolo.api;

import org.piccolo.context.Cursor;
import org.piccolo.node.ExpressionNode;
import org.piccolo.node.TokenNode;
import org.piccolo.node.TokenNodeFactory;
import org.piccolo.node.VariableType;

public class ExpressionApi {

    private final Cursor cursor;
    private final TokenNodeFactory factory = TokenNodeFactory.getInstance();
    private final ExpressionNode expressionNode;

    public ExpressionApi() {
        this.cursor = new Cursor();
        this.expressionNode = factory.createExpression(this.cursor);
    }

    public ExpressionApi intVariable(String varName) {
        this.expressionNode.addChild(factory.createIntVariable(varName, cursor));
        return this;
    }

    public ExpressionApi stringVariable(String varName) {
        this.expressionNode.addChild(factory.createStringVariable(varName, cursor));
        return this;
    }

    public ExpressionApi variable(String varName, VariableType varType) {
        this.expressionNode.addChild(factory.createVariableDefinition(varName, varType, cursor));
        return this;
    }


    public ExpressionApi addOperand(int value) {
        this.expressionNode.addChild(factory.createInteger(value, cursor));
        return this;
    }

    public ExpressionApi addOperand(String variableName) {
        this.expressionNode.addChild(factory.createIdentifier(variableName, cursor));
        return this;
    }

    public ExpressionApi applyOperation(String operatorId) {
        TokenNode node = factory.createOperator(operatorId, cursor);
        expressionNode.addChild(node);
        return this;
    }

    public ExpressionApi identifier(String name) {
        TokenNode node = factory.createIdentifier(name, cursor);
        expressionNode.addChild(node);
        return this;
    }

    public TokenNode build() {
        TokenNode node = expressionNode.getChildren().get(0);
        this.expressionNode.getChildren().clear();
        return node;
    }
}
