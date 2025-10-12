package com.example.Algorithms;

import java.util.*;

import com.example.graph.Edge;
import com.example.graph.Graph;
import com.example.graph.Node;

public class DFS {
    private final Graph g;

    public DFS(Graph g) {
        this.g = g;
    }

    // Depth-first search returning one path (not guaranteed shortest)
    public List<Node> path(Node start, Node goal) {
        Map<Long, Node> prev = new HashMap<>();
        Set<Long> seen = new HashSet<>();
        Deque<Node> stack = new ArrayDeque<>();
        stack.push(start);
        seen.add(start.id);

        while (!stack.isEmpty()) {
            Node u = stack.pop();
            if (u.equals(goal))
                break;
            for (Edge e : g.edgesOf(u)) {
                Node v = e.to;
                if (seen.add(v.id)) {
                    prev.put(v.id, u);
                    stack.push(v);
                }
            }
        }
        return reconstruct(prev, start, goal);
    }

    private List<Node> reconstruct(Map<Long, Node> prev, Node start, Node goal) {
        LinkedList<Node> path = new LinkedList<>();
        Node cur = goal;
        if (!prev.containsKey(goal.id) && !start.equals(goal))
            return path; // empty
        while (cur != null && !cur.equals(start)) {
            path.addFirst(cur);
            cur = prev.get(cur.id);
        }
        if (cur != null)
            path.addFirst(start);
        return path;
    }
}
