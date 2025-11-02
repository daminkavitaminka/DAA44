package metrics;

import java.util.HashMap;
import java.util.Map;

public class Metrics {

    private final Map<String, Long> executionTimes;
    private  final Map<String, Long> operationCounts;

    public Metrics() {
        this.executionTimes = new HashMap<>();
        this.operationCounts = new HashMap<>();
    }

    public void setExecutionTime(String algorithmName, long timeNanos) {
        this.executionTimes.put(algorithmName, timeNanos);
    }

    public void recordTime(String algorithmName, long timeNanos) {
        this.setExecutionTime(algorithmName, timeNanos);
    }

    public void setOperationCount(String operationName, long count) {
        this.operationCounts.put(operationName, count);
    }

    public void incrementCounter(String counterName) {
        this.operationCounts.merge(counterName, 1L, Long::sum);
    }

    public void printSummary(String filename) {
        System.out.println("--- Метрики для " + filename + " ---");

        System.out.println("Время выполнения (нс):");
        executionTimes.forEach((name, time) ->
                System.out.printf("  %-20s : %d нс (%.4f мс)\n", name, time, time / 1_000_000.0));

        if (!operationCounts.isEmpty()) {
            System.out.println("Количество операций:");
            operationCounts.forEach((name, count) ->
                    System.out.printf("  %-20s : %d\n", name, count));
        }
        System.out.println("-------------------------------------");
    }
}
