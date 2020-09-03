package org.piccolo.parsing.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.LinkedList;
import java.util.Queue;
import org.piccolo.node.TokenNode;

public class TokenNodeUtil {

    public static TokenNode getNode(TokenNode tokenNode, int depth, int offset) {
        if (tokenNode == null ||depth < 0 || offset < 0) {
            return null;
        } else if (depth == 0) {
            return tokenNode;
        }
        else if (depth == 1 && offset < tokenNode.getChildren().size()) {
            return tokenNode.getChildren().get(offset);
        } else {
            Queue<TokenNode> queueL1 = new LinkedList<>();
            queueL1.add(tokenNode);
            int currentDepth = 1;
            while (!queueL1.isEmpty()) {
                int currentOffset = 0;
                Queue<TokenNode> queueL2 = new LinkedList<>();
                while (!queueL1.isEmpty()) {
                    queueL2.addAll(queueL1.poll().getChildren());
                }
                while (!queueL2.isEmpty()) {
                    TokenNode node = queueL2.poll();
                    if (currentDepth == depth && currentOffset == offset) {
                        return node;
                    }
                    queueL1.add(node);
                    currentOffset++;
                }
                currentDepth++;
            }
        }
        return null;
    }

    public static String toJson(TokenNode tokenNode) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        return gson.toJson(tokenNode);
    }
}
