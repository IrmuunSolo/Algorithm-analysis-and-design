package com.example.util;

import java.util.*;

import com.example.graph.Node;

public final class PathUtils {
    private PathUtils() {
    }

    // эхлэх цагээс дуусах цаг хүртэлх замыг буцаах
    public static List<Node> reconstruct(Map<Long, Node> prev, Node start, Node goal) {
        LinkedList<Node> path = new LinkedList<>();
        if (start == null || goal == null)
            return path;
        Node cur = goal;
        if (!start.equals(goal) && (prev == null || !prev.containsKey(goal.id)))
            return path;
        while (cur != null && !cur.equals(start)) {
            path.addFirst(cur);
            cur = prev.get(cur.id);
        }
        if (cur != null)
            path.addFirst(start);
        return path;
    }
}
