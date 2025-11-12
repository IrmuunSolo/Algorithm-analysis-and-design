package com.example.Algorithms;

import java.util.*;

import com.example.graph.Edge;
import com.example.graph.Graph;
import com.example.graph.Node;
import com.example.util.PathUtils;

public class Dijkstra {
    private final Graph g;

    public Dijkstra(Graph g) {
        this.g = g;
    }

    public List<Node> shortestPath(Node start, Node goal) {
        Map<Long, Double> dist = new HashMap<>();
        Map<Long, Node> prev = new HashMap<>();

        for (Node n : g.getNodes())
            dist.put(n.id, Double.POSITIVE_INFINITY);
        dist.put(start.id, 0.0);

        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingDouble(n -> dist.get(n.id)));
        pq.add(start);

        while (!pq.isEmpty()) {
            Node u = pq.poll();
            if (u.equals(goal))
                break;
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
        return PathUtils.reconstruct(prev, start, goal);
    }
}
