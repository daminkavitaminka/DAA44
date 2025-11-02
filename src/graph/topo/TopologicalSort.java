package graph.topo;

import data.Graph;
import data.Edge;
import metrics.Metrics;

import java.util.*;

/** Computes a topological order of a DAG using DFS. */
public class TopologicalSort {
    private final Graph graph;
    private final Metrics metrics;
    private final Stack<Integer> stack;
    private final boolean[] visited;

    public TopologicalSort(Graph graph, Metrics metrics) {
        this.graph = graph; this.metrics = metrics;
        this.stack = new Stack<>();
        this.visited = new boolean[graph.getV()];
    }

    public List<Integer> sort() {
        long startTime = System.nanoTime();
        for (int i = 0; i < graph.getV(); i++) {
            if (!visited[i]) { dfs(i); }
        }
        metrics.recordTime("TopologicalSort_Time", System.nanoTime() - startTime);

        List<Integer> order = new ArrayList<>();
        while (!stack.isEmpty()) { order.add(stack.pop()); }
        return order;
    }

    private void dfs(int u) {
        metrics.incrementCounter("DFS_visits_Topo");
        visited[u] = true;

        for (Edge edge : graph.getAdj(u)) {
            metrics.incrementCounter("DFS_edges_Topo");
            int v = edge.getV();
            if (!visited[v]) { dfs(v); }
        }

        stack.push(u);
    }
}