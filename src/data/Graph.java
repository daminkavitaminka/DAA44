package data;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

public class Graph {

    private final int V; // Количество вершин
    private final List<Edge> edges; // Список всех ребер
    private final int source; // Исходная вершина
    private final String weightModel; // Модель веса ("edge" или другая)
    private final boolean directed; // Направленный ли граф

    // Структура для представления графа (списки смежности)
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

    public boolean isDirected() {
        return directed;
    }

    // ... другие методы ...
}