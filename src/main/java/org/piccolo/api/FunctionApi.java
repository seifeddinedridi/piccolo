package org.piccolo.api;

import org.piccolo.context.Cursor;
import org.piccolo.node.FunctionBodyNode;
import org.piccolo.node.FunctionNode;
import org.piccolo.node.FunctionSignatureNode;
import org.piccolo.node.ParameterListNode;
import org.piccolo.node.TokenNode;
import org.piccolo.node.TokenNodeFactory;
import org.piccolo.node.VariableType;

import java.util.ArrayList;
import java.util.List;

public class FunctionApi {

    private final Cursor cursor;
    private final TokenNodeFactory factory = TokenNodeFactory.getInstance();
    private final FunctionHeaderApi functionHeaderApi;
    private final FunctionBodyApi functionBodyApi;

    public FunctionApi() {
        this.cursor = new Cursor();
        this.functionHeaderApi = new FunctionHeaderApi();
        this.functionBodyApi = new FunctionBodyApi();
    }

    public FunctionHeaderApi header() {
        return functionHeaderApi;
    }

    public FunctionBodyApi body() {
        return functionBodyApi;
    }

    public FunctionNode build() {
        return new FunctionNode(functionHeaderApi.build(), functionBodyApi.build());
    }

    public class FunctionHeaderApi {

        private final List<TokenNode> parameterList;
        private VariableType returnType;
        private String functionName;

        private FunctionHeaderApi() {
            this.parameterList = new ArrayList<>();
        }

        public FunctionHeaderApi returnType(VariableType type) {
            this.returnType = type;
            return this;
        }

        public FunctionHeaderApi name(String name) {
            this.functionName = name;
            return this;
        }

        public FunctionHeaderApi parameter(String name, VariableType type) {
            this.parameterList.add(factory.createVariableDefinition(name, type, cursor));
            return this;
        }

        public FunctionBodyApi body() {
            return FunctionApi.this.body();
        }

        private FunctionSignatureNode build() {
            return new FunctionSignatureNode(factory.createVariableDefinition(functionName, returnType, cursor),
                    new ParameterListNode(parameterList));
        }
    }

    public class FunctionBodyApi {

        private final List<TokenNode> expressions;

        private FunctionBodyApi() {
            this.expressions = new ArrayList<>();
        }

        public FunctionBodyApi add(TokenNode expression) {
            expressions.add(expression);
            return this;
        }

        public FunctionBodyApi returnStatement() {
            add(factory.createReturnVoidAction(cursor));
            return this;
        }

        public FunctionBodyApi returnStatement(TokenNode node) {
            TokenNode returnAction = factory.createReturnAction(cursor);
            returnAction.addChild(node);
            add(returnAction);
            return this;
        }

        public FunctionHeaderApi header() {
            return FunctionApi.this.header();
        }

        private FunctionBodyNode build() {
            return new FunctionBodyNode(expressions);
        }

        public FunctionApi function() {
            return FunctionApi.this;
        }
    }
}
