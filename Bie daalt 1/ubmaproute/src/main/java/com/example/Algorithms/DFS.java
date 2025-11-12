package com.example.Algorithms;

import java.util.*;

import com.example.graph.Edge;
import com.example.graph.Graph;
import com.example.graph.Node;
import com.example.util.PathUtils;

public class DFS {
    private final Graph g;

    public DFS(Graph g) {
        this.g = g;
    }

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
        return PathUtils.reconstruct(prev, start, goal);
    }
}
