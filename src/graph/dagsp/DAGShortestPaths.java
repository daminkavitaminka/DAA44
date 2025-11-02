package graph.dagsp;

import data.Graph;
import data.Edge;
import metrics.Metrics;
import graph.topo.TopologicalSort;

import java.util.*;

/** Computes Single-Source Shortest and Longest Paths in a DAG. */
public class DAGShortestPaths {
    private final Graph graph;
    private final Metrics metrics;
    private final int source;
    public static final int INF = Integer.MAX_VALUE / 2;

    public DAGShortestPaths(Graph graph, Metrics metrics) {
        this.graph = graph; this.metrics = metrics;
        this.source = graph.getSource();
    }

    public Map<String, Object> getShortestPath() { return computePaths(false); }
    public Map<String, Object> getLongestPath() { return computePaths(true); }

    private Map<String, Object> computePaths(boolean isLongestPath) {
        long startTime = System.nanoTime();

        // 1. Get Topological Order
        TopologicalSort sorter = new TopologicalSort(graph, metrics);
        List<Integer> topoOrder = sorter.sort();

        int V = graph.getV();
        int[] dist = new int[V];
        int[] predecessor = new int[V];

        Arrays.fill(dist, isLongestPath ? -INF : INF);
        Arrays.fill(predecessor, -1);
        dist[source] = 0;

        // 2. Relaxation
        for (int u : topoOrder) {
            if (dist[u] == (isLongestPath ? -INF : INF)) { continue; }

            for (Edge edge : graph.getAdj(u)) {
                metrics.incrementCounter("Relaxations_DAGSP"); // Counter required
                int v = edge.getV();
                int weight = edge.getWeight();
                int newDist = dist[u] + weight;

                if (isLongestPath) {
                    // MAX-DP for Longest Path (Critical Path)
                    if (newDist > dist[v]) {
                        dist[v] = newDist;
                        predecessor[v] = u;
                    }
                } else {
                    // MIN-DP for Shortest Path
                    if (newDist < dist[v]) {
                        dist[v] = newDist;
                        predecessor[v] = u;
                    }
                }
            }
        }

        metrics.recordTime(isLongestPath ? "LongestPath_Time" : "ShortestPath_Time", System.nanoTime() - startTime);

        // 3. Find overall max distance and reconstruct path
        int totalLength = isLongestPath ? -INF : INF;
        int targetNode = -1;

        if (isLongestPath) {
            for(int i = 0; i < V; i++) {
                if (dist[i] > totalLength) {
                    totalLength = dist[i];
                    targetNode = i;
                }
            }
        }

        List<Integer> path = new LinkedList<>();
        if (isLongestPath && targetNode != -1 && totalLength != -INF) {
            int current = targetNode;
            while(current != -1 && current != source) {
                path.add(0, current);
                current = predecessor[current];
            }
            if (current == source) { path.add(0, source); }
            else { path = Collections.emptyList(); }
        }

        Map<String, Object> results = new HashMap<>();
        results.put("distances", dist);
        results.put("totalLength", totalLength);
        results.put("optimalPath", path);
        return results;
    }
}