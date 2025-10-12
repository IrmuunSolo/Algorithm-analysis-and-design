package com.example.graph;

import java.util.*;

import com.example.util.GeoUtils;

public class Graph {
    private final Map<Long, Node> nodes = new HashMap<>();
    private final Map<Long, List<Edge>> adj = new HashMap<>();

    public Node addNode(long id, double lat, double lon) {
        return nodes.computeIfAbsent(id, k -> {
            Node n = new Node(id, lat, lon);
            adj.put(id, new ArrayList<>());
            return n;
        });
    }

    public void addUndirectedEdge(Node a, Node b) {
        double w = GeoUtils.haversineMeters(a.lat, a.lon, b.lat, b.lon);
        addEdge(a, b, w);
        addEdge(b, a, w);
    }

    public void addUndirectedEdge(Node a, Node b, double weight) {
        addEdge(a, b, weight);
        addEdge(b, a, weight);
    }

    public void addEdge(Node from, Node to, double weight) {
        adj.get(from.id).add(new Edge(from, to, weight));
    }

    public Collection<Node> getNodes() { return nodes.values(); }
    public List<Edge> edgesOf(Node n) { return adj.getOrDefault(n.id, Collections.emptyList()); }
    public int getNodeCount() { return nodes.size(); }
    public int getEdgeCount() { return adj.values().stream().mapToInt(List::size).sum(); }

    public Node findNearestNode(double lat, double lon) {
        Node best = null;
        double bestD = Double.POSITIVE_INFINITY;
        for (Node n : nodes.values()) {
            double d = GeoUtils.haversineMeters(lat, lon, n.lat, n.lon);
            if (d < bestD) { bestD = d; best = n; }
        }
        return best;
    }

    public double pathWeightSeconds(List<Node> path) {
        if (path == null || path.size() < 2) return 0.0;
        double sum = 0.0;
        for (int i = 1; i < path.size(); i++) {
            Node a = path.get(i-1), b = path.get(i);
            double w = Double.NaN;
            for (Edge e : edgesOf(a)) {
                if (e.to.equals(b)) { w = e.weight; break; }
            }
            if (!Double.isNaN(w)) sum += w; // ignore missing edge
        }
        return sum;
    }
}
