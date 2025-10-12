package com.example.Algorithms;

import java.util.*;

import com.example.graph.Edge;
import com.example.graph.Graph;
import com.example.graph.Node;

public class BFS {
    private final Graph g;

    public BFS(Graph g) {
        this.g = g;
    }

    public List<Node> shortestPath(Node start, Node goal) {
        Map<Long, Node> prev = new HashMap<>();
        Set<Long> seen = new HashSet<>();
        Deque<Node> q = new ArrayDeque<>();
        q.add(start);
        seen.add(start.id);

        while (!q.isEmpty()) {
            Node u = q.poll();
            if (u.equals(goal))
                break;
            for (Edge e : g.edgesOf(u)) {
                Node v = e.to;
                if (seen.add(v.id)) {
                    prev.put(v.id, u);
                    q.add(v);
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
