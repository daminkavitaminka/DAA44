package graph.scc;

import data.Graph;
import data.Edge;
import metrics.Metrics;

import java.util.*;

/** Finds Strongly Connected Components (SCCs) using Tarjan's algorithm. */
public class TarjanSCC {
    private final Graph graph;
    private final Metrics metrics;
    private int time;
    private final int[] disc; private final int[] low;
    private final Stack<Integer> stack; private final boolean[] onStack;
    private final List<List<Integer>> sccs; private final int[] componentId;

    public TarjanSCC(Graph graph, Metrics metrics) {
        this.graph = graph; this.metrics = metrics;
        int V = graph.getV();
        this.disc = new int[V]; this.low = new int[V];
        Arrays.fill(disc, -1);
        this.stack = new Stack<>(); this.onStack = new boolean[V];
        this.sccs = new ArrayList<>();
        this.componentId = new int[V]; Arrays.fill(componentId, -1);
    }

    public List<List<Integer>> findSccs() {
        long startTime = System.nanoTime();
        time = 0;
        for (int i = 0; i < graph.getV(); i++) {
            if (disc[i] == -1) { dfs(i); }
        }
        metrics.recordTime("TarjanSCC_Time", System.nanoTime() - startTime);
        return sccs;
    }

    private void dfs(int u) {
        metrics.incrementCounter("DFS_visits_SCC"); // Counter required
        disc[u] = low[u] = time++;
        stack.push(u); onStack[u] = true;

        for (Edge edge : graph.getAdj(u)) {
            metrics.incrementCounter("DFS_edges_SCC"); // Counter required
            int v = edge.getV();
            if (disc[v] == -1) {
                dfs(v);
                low[u] = Math.min(low[u], low[v]);
            } else if (onStack[v]) {
                low[u] = Math.min(low[u], disc[v]);
            }
        }

        if (low[u] == disc[u]) {
            List<Integer> scc = new ArrayList<>();
            int w;
            int sccIndex = sccs.size();
            do {
                w = stack.pop();
                onStack[w] = false;
                scc.add(w);
                componentId[w] = sccIndex;
            } while (w != u);
            sccs.add(scc);
        }
    }

    public int[] getComponentIds() { return componentId; }
}