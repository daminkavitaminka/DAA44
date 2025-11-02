package main;

import data.Graph;
import util.JsonGraphLoader;
import metrics.Metrics;
import graph.scc.TarjanSCC;
import graph.topo.TopologicalSort;
import graph.dagsp.DAGShortestPaths;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class SmartScheduler {

    private static final List<String> DATA_FILES = List.of(
            "small_sparse_dag.json", "small_dense_dag.json", "small_cyclic_scc.json",
            "medium_sparse_dag.json", "medium_dense_dag.json", "medium_multiple_sccs.json",
            "large_long_path_dag.json", "large_dense_cyclic.json", "large_mixed_structure.json"
    );

    public static void main(String[] args) {
        System.out.println("--- Запуск SmartScheduler: Анализ 9 наборов данных ---");

        for (String filename : DATA_FILES) {
            try {
                System.out.println("\n*** Анализ файла: " + filename + " ***");

                Graph graph = JsonGraphLoader.loadGraph("./src/data/" + filename);

                Metrics metrics = new Metrics();

                runTarjanSCC(graph, metrics);

                runTopologicalSort(graph, metrics);

                runDAGShortestPaths(graph, metrics);

                metrics.printSummary(filename);

            } catch (IOException e) {
                System.err.println("Ошибка при чтении файла " + filename + ": " + e.getMessage());
            } catch (Exception e) {
                System.err.println("Общая ошибка при обработке " + filename + ": " + e.getMessage());
                e.printStackTrace();
            }
        }
        System.out.println("\n--- Анализ завершен. ---");
    }

    private static void runTarjanSCC(Graph graph, Metrics metrics) {
        long startTime = System.nanoTime();
        TarjanSCC tarjan = new TarjanSCC(graph, metrics);
        List<List<Integer>> sccs = tarjan.findSccs();
        long endTime = System.nanoTime();

        metrics.setExecutionTime("TarjanSCC", endTime - startTime);
        System.out.printf("TarjanSCC: Найдено %d сильно связных компонент.\n", sccs.size());
    }

    private static void runTopologicalSort(Graph graph, Metrics metrics) {
        if (graph.isDirected()) {
            long startTime = System.nanoTime();
            TopologicalSort topo = new TopologicalSort(graph, metrics);
            List<Integer> order = topo.sort();
            long endTime = System.nanoTime();

            metrics.setExecutionTime("TopologicalSort", endTime - startTime);
            System.out.printf("TopologicalSort: Упорядочено %d узлов.\n", order.size());
        } else {
            System.out.println("TopologicalSort: Пропущен (граф не направленный).");
        }
    }

    private static void runDAGShortestPaths(Graph graph, Metrics metrics) {
        long startTime;
        long endTime;
        DAGShortestPaths dagsp = new DAGShortestPaths(graph, metrics);

        startTime = System.nanoTime();
        Map<String, Object> shortestResult = dagsp.getShortestPath();
        endTime = System.nanoTime();
        metrics.setExecutionTime("DAGShortestPath", endTime - startTime);

        startTime = System.nanoTime();
        Map<String, Object> longestResult = dagsp.getLongestPath();
        endTime = System.nanoTime();
        metrics.setExecutionTime("DAGLongestPath", endTime - startTime);

        System.out.printf("DAGShortestPaths: Кратчайший путь %d, Длиннейший путь %d.\n",
                (Integer) shortestResult.get("totalLength"),
                (Integer) longestResult.get("totalLength"));
    }
}