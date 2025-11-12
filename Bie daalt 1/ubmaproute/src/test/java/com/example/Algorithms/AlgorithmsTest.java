package com.example.Algorithms;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.example.graph.Graph;
import com.example.graph.Node;

public class AlgorithmsTest {

    private static class GFixture {
        final Graph g = new Graph();
        final Node A = g.addNode(1, 0, 0);
        final Node B = g.addNode(2, 0, 1);
        final Node C = g.addNode(3, 0, 2);
        final Node D = g.addNode(4, 1, 0);

        GFixture() {
            // A to C: A-B-C (1+1), A-D-C (1+10)
            g.addUndirectedEdge(A, B, 1.0);
            g.addUndirectedEdge(B, C, 1.0);
            g.addUndirectedEdge(A, D, 1.0);
            g.addUndirectedEdge(D, C, 10.0);
        }
    }

    @Test
    public void testBfsShortestByHops() {
        GFixture f = new GFixture();
        BFS bfs = new BFS(f.g);
        List<Node> path = bfs.shortestPath(f.A, f.C);
        assertNotNull(path);
        assertEquals(3, path.size());
        assertEquals(f.A, path.get(0));
        assertEquals(f.C, path.get(2));
        // With insertion order, BFS should go via B
        assertEquals(f.B, path.get(1));
    }

    @Test
    public void testDijkstraChoosesLowerTime() {
        GFixture f = new GFixture();
        Dijkstra dij = new Dijkstra(f.g);
        List<Node> path = dij.shortestPath(f.A, f.C);
        assertNotNull(path);
        assertEquals(3, path.size());
        assertEquals(f.B, path.get(1));
        double secs = f.g.pathWeightSeconds(path);
        assertEquals(2.0, secs, 1e-6);
    }

    @Test
    public void testDfsFindsAPath() {
        GFixture f = new GFixture();
        DFS dfs = new DFS(f.g);
        List<Node> path = dfs.path(f.A, f.C);
        assertNotNull(path);
        assertEquals(3, path.size());
        assertEquals(f.A, path.get(0));
        assertEquals(f.C, path.get(2));
    }

    @Test
    public void testOneWayRespected() {
        Graph g = new Graph();
        Node E = g.addNode(5, 2, 2);
        Node F = g.addNode(6, 2, 3);
        // One-way E -> F only
        g.addEdge(E, F, 1.0);

        BFS bfs = new BFS(g);
        List<Node> ef = bfs.shortestPath(E, F);
        assertEquals(2, ef.size());

        List<Node> fe = bfs.shortestPath(F, E);
        assertTrue(fe == null || fe.isEmpty());
    }
}
