package org.suai.model;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public class Node implements Serializable {
    int x, y;
    int priority;
    int costSoFar;
    Node cameFrom;

    public Node(int x, int y, int priority, int costSoFar, Node cameFrom) {
        this.x = x;
        this.y = y;
        this.priority = priority;
        this.costSoFar = costSoFar;
        this.cameFrom = cameFrom;
    }

    public static int compare(Node n1, Node n2) {
        return Integer.compare(n1.costSoFar, n2.costSoFar);
    }
}
