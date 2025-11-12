package com.example.Algorithms;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

import com.example.graph.Graph;
import com.example.graph.Node;

public class AlgorithmInvariantTest {

    // Туслах: зам дагуух ирмэгүүдийн жингийн нийлбэр
    private static double sumOfWeight(Graph g, List<Node> path) {
        if (path == null || path.size() < 2) return 0.0;
        double sum = 0.0;
        for (int i = 1; i < path.size(); i++) {
            Node a = path.get(i-1), b = path.get(i);
            boolean found = false;
            for (com.example.graph.Edge e : g.edgesOf(a)) {
                if (e.to.equals(b)) { sum += e.weight; found = true; break; }
            }
            assertTrue("Замын хоёр зангилааны хооронд ирмэг олдсонгүй", found);
        }
        return sum;
    }

    /**
     * BFS индукц (шулуун граф):
     * Суурь: n=2 үед замын урт 1. Алхам: нэг зангилаа нэмэгдэхэд hop 1-ээр өснө.
     */
    @Test
    public void bfsPathGraphInduction() {
        for (int n = 2; n <= 12; n++) {
            Graph g = new Graph();
            List<Node> nodes = new ArrayList<>();
            for (int i = 0; i < n; i++) nodes.add(g.addNode(i+1, 0, i));
            for (int i = 0; i < n-1; i++) g.addUndirectedEdge(nodes.get(i), nodes.get(i+1), 1.0);
            BFS bfs = new BFS(g);
            List<Node> path = bfs.shortestPath(nodes.get(0), nodes.get(n-1));
            assertEquals("BFS-ийн дамжих зангилааны тоо n-1 байх ёстой", n-1, path.size()-1);
            // Инвариант: BFS модон дотор зам дагуух түвшин 1-ээр өсдөг
            Map<Long,Integer> level = levelsFrom(g, nodes.get(0));
            for (int i = 1; i < path.size(); i++) {
                int dPrev = level.get(path.get(i-1).id);
                int dCur  = level.get(path.get(i).id);
                assertEquals(1, dCur - dPrev);
            }
        }
    }

    /**
     * Dijkstra индукц (нэгж жинтэй шулуун граф):
     * Суурь: n=2 → зай 1. Алхам: нэг зангилаа нэмэгдэхэд оновчтой зай 1-ээр өснө.
     */
    @Test
    public void dijkstraLineGraphInduction() {
        for (int n = 2; n <= 20; n++) {
            Graph g = new Graph();
            List<Node> nodes = new ArrayList<>();
            for (int i = 0; i < n; i++) nodes.add(g.addNode(i+1, 0, i));
            for (int i = 0; i < n-1; i++) g.addUndirectedEdge(nodes.get(i), nodes.get(i+1), 1.0);
            Dijkstra d = new Dijkstra(g);
            List<Node> path = d.shortestPath(nodes.get(0), nodes.get(n-1));
            assertEquals("Замын зангилааны тоо n байх ёстой", n, path.size());
            assertEquals("Нэгж жинтэй шулуун граф дээр оновчтой зай n-1", n-1.0, sumOfWeight(g, path), 1e-9);
        }
    }

    /**
     * DFS индукц (шулуун граф): n өсөх тусам буцаасан замын урт 1-ээр нэмэгдэнэ.
     */
    @Test
    public void dfsLineGraphInduction() {
        for (int n = 2; n <= 12; n++) {
            Graph g = new Graph();
            List<Node> nodes = new ArrayList<>();
            for (int i = 0; i < n; i++) nodes.add(g.addNode(i+1, 0, i));
            for (int i = 0; i < n-1; i++) g.addUndirectedEdge(nodes.get(i), nodes.get(i+1), 1.0);
            DFS dfs = new DFS(g);
            List<Node> path = dfs.path(nodes.get(0), nodes.get(n-1));
            assertEquals("DFS шулуун граф дээр n-1 урттай зам олно", n-1, path.size()-1);
        }
    }

    // Инвариант шалгахад ашиглах BFS түвшин (level) олголт
    private static Map<Long,Integer> levelsFrom(Graph g, Node s) {
        Map<Long,Integer> dist = new HashMap<>();
        Deque<Node> q = new ArrayDeque<>();
        dist.put(s.id, 0); q.add(s);
        while (!q.isEmpty()) {
            Node u = q.poll();
            int du = dist.get(u.id);
            for (com.example.graph.Edge e : g.edgesOf(u)) {
                Node v = e.to;
                if (!dist.containsKey(v.id)) { dist.put(v.id, du+1); q.add(v); }
            }
        }
        return dist;
    }
}

