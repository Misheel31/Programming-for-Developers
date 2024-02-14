import java.util.*;

public class NetworkDevice {
    public static List<Integer> impactedDevices(int[][] edges, int target) {
        List<List<Integer>> graph = buildGraph(edges);
        List<Integer> visited = new ArrayList<>();
        List<Integer> impacted = new ArrayList<>();
        dfs(graph, target, visited, impacted, target);
        return impacted;
    }

    private static List<List<Integer>> buildGraph(int[][] edges) {
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < edges.length; i++) {
            int src = edges[i][0];
            int dest = edges[i][1];
            while (graph.size() <= src || graph.size() <= dest) {
                graph.add(new ArrayList<>());
            }
            graph.get(src).add(dest);
            graph.get(dest).add(src);
        }
        return graph;
    }

    private static void dfs(List<List<Integer>> graph, int src, List<Integer> visited, List<Integer> impacted, int target) {
        if (visited.contains(src)) {
            return;
        }
        visited.add(src);
    
        for (int neighbor : graph.get(src)) {
            dfs(graph, neighbor, visited, impacted, target);
        }
        if (src == target) {
            impacted.add(src);
        }
        
    }

    public static void main(String[] args) {
        int[][] edges = {{0, 1}, {0, 2}, {1, 3}, {1, 6}, {2, 4}, {4, 6}, {4, 5}, {5, 7}};
        int target = 4;
        List<Integer> result = impactedDevices(edges, target);
        System.out.println("Impacted device: " + result);
    }
}
