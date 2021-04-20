package org.piccolo.node;

import java.util.ArrayList;
import java.util.Stack;
import org.piccolo.context.Cursor;

public class ExpressionNode extends TokenNode {

    public ExpressionNode(String name, Cursor tokenStartPosition) {
        super(TokenType.EXPRESSION, name, 0, new ArrayList<>(), tokenStartPosition);
    }

    public void addChild(TokenNode node) {
        Stack<TokenNode> nodeStack = new Stack<>();
        if (type == TokenType.EXPRESSION) {
            if (!children.isEmpty()) {
                nodeStack.push(children.get(0));
                nodeStack.push(this);
            } else {
                children.add(node);
            }
        }
        while (!nodeStack.isEmpty()) {
            TokenNode parentOfCurrentNode = nodeStack.pop();
            TokenNode currentNode = nodeStack.pop();
            if (currentNode.type == TokenType.OPERATOR && currentNode.children.size() == 2) {
                if (currentNode.children.get(1).type == TokenType.OPERATOR) {
                    nodeStack.push(currentNode.children.get(1));
                    nodeStack.push(currentNode);
                } else {
                    // Replace right child if its precedence is lower than node's precedence
                    if (currentNode.precedence < node.precedence) {
                        node.children.add(currentNode.children.get(1));
                        currentNode.children.set(1, node);
                    } else {
                        node.children.add(currentNode);
                        // parent of 'currentNode' should have 'node' as left child
                        if (parentOfCurrentNode != null) {
                            parentOfCurrentNode.children.set(1, node);
                        }
                    }
                }
            } else if (currentNode.type == TokenType.RETURN_ACTION) {
                // Unary operator?
                if (currentNode.children.isEmpty()) {
                    currentNode.children.add(node);
                } else {
                    if (currentNode.children.get(0).type == TokenType.OPERATOR) {
                        nodeStack.push(currentNode.children.get(0));
                        nodeStack.push(currentNode);
                    } else {
                        node.children.add(currentNode.children.get(0));
                        currentNode.children.set(0, node);
                    }
                }
            } else if (currentNode.type == TokenType.LITERAL || currentNode.type == TokenType.VARIABLE_DEFINITION) {
                if (node.getType() == TokenType.LITERAL) {
                    throw new IllegalArgumentException("Cannot have two consecutive literal nodes");
                }
                node.children.add(currentNode);
                if (parentOfCurrentNode != null) {
                    parentOfCurrentNode.children.set(0, node);
                }

            } else if (currentNode.type == TokenType.OPERATOR && node.getType() == TokenType.OPERATOR) {
                // Unary operator?
                throw new IllegalArgumentException("Unary operators are not supported yet!!");
            } else if (currentNode.type == TokenType.OPERATOR) {
                currentNode.children.add(node);
            }
        }
    }
}
