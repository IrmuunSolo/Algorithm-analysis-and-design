package com.example.graph;

public class Edge {
    public final Node from;
    public final Node to;
    public final double weight; // meters

    public Edge(Node from, Node to, double weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }
}

