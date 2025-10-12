package com.example.Algorithms;

import java.util.*;

import com.example.graph.Edge;
import com.example.graph.Graph;
import com.example.graph.Node;

public class Dijkstra {
    private final Graph g;
    public Dijkstra(Graph g) { this.g = g; }

    public List<Node> shortestPath(Node start, Node goal) {
        Map<Long, Double> dist = new HashMap<>();
        Map<Long, Node> prev = new HashMap<>();

        for (Node n : g.getNodes()) dist.put(n.id, Double.POSITIVE_INFINITY);
        dist.put(start.id, 0.0);

        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingDouble(n -> dist.get(n.id)));
        pq.add(start);

        while (!pq.isEmpty()) {
            Node u = pq.poll();
            if (u.equals(goal)) break;
            double du = dist.get(u.id);
            for (Edge e : g.edgesOf(u)) {
                Node v = e.to;
                double alt = du + e.weight;
                if (alt < dist.get(v.id)) {
                    dist.put(v.id, alt);
                    prev.put(v.id, u);
                    pq.remove(v);
                    pq.add(v);
                }
            }
        }
        return reconstruct(prev, start, goal);
    }

    private List<Node> reconstruct(Map<Long, Node> prev, Node start, Node goal) {
        LinkedList<Node> path = new LinkedList<>();
        Node cur = goal;
        if (!prev.containsKey(goal.id) && !start.equals(goal)) return path; // empty
        while (cur != null && !cur.equals(start)) {
            path.addFirst(cur);
            cur = prev.get(cur.id);
        }
        if (cur != null) path.addFirst(start);
        return path;
    }
}
