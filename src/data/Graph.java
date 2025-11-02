package data;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

public class Graph {

    private final int V;
    private final List<Edge> edges;
    private final int source;
    private final String weightModel;
    private final boolean directed;

    private final Map<Integer, List<Edge>> adj;

    public Graph(int V, List<Edge> edges, int source, String weightModel, boolean directed) {
        this.V = V;
        this.edges = edges;
        this.source = source;
        this.weightModel = weightModel;
        this.directed = directed;
        this.adj = new HashMap<>();

        for (int i = 0; i < V; i++) {
            adj.put(i, new ArrayList<>());
        }

        for (Edge edge : edges) {
            adj.get(edge.getU()).add(edge);
        }
    }

    public int getV() {
        return V;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public int getSource() {
        return source;
    }

    public Map<Integer, List<Edge>> getAdj() {
        return adj;
    }

    public List<Edge> getAdj(int u) {
        return adj.get(u);
    }

    public  boolean isDirected() {
        return directed;
    }

    public Map<Integer, List<Edge>> getReverseAdj() {
        Map<Integer, List<Edge>> reverseAdj = new HashMap<>();
        for (int i = 0; i < V; i++) {
            reverseAdj.put(i, new ArrayList<>());
        }
        for (Edge edge : edges) {
            reverseAdj.get(edge.getV()).add(new Edge(edge.getV(), edge.getU(), edge.getWeight()));
        }
        return reverseAdj;
    }
}
