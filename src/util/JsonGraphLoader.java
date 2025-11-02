package util;

import data.Graph;
import data.Edge;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class JsonGraphLoader {

    public static Graph loadGraph(String filePath) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(filePath)));

        JSONObject jsonObject = new JSONObject(content);

        boolean directed = jsonObject.getBoolean("directed");
        int n = jsonObject.getInt("n");
        String weightModel = jsonObject.getString("weight_model");
        int source = jsonObject.getInt("source");

        JSONArray edgesArray = jsonObject.getJSONArray("edges");
        List<Edge> edges = new ArrayList<>();

        for (int i = 0; i < edgesArray.length(); i++) {
            JSONObject edgeObj = edgesArray.getJSONObject(i);
            int u = edgeObj.getInt("u");
            int v = edgeObj.getInt("v");
            int w = edgeObj.getInt("w");
            edges.add(new Edge(u, v, w));
        }

        if (!directed) {
            List<Edge> undirectedEdges = new ArrayList<>(edges);
            for (Edge edge : edges) {
                undirectedEdges.add(new Edge(edge.getV(), edge.getU(), edge.getWeight()));
            }
            edges = undirectedEdges;
        }

        return new Graph(n, edges, source, weightModel, directed);
    }
}