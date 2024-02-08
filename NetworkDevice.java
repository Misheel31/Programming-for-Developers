import java.util.*;

public class NetworkDevice {
    public static List<Integer> findImpactedDevices(int[][] edges, int targetDevice) {
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < edges.length; i++) {
            graph.add(new ArrayList<>());
        }
        for (int[] edge : edges) {
            graph.get(edge[0]).add(edge[1]);
            graph.get(edge[1]).add(edge[0]);
        }
        List<Integer> impactedDevices = new ArrayList<>();
        Set<Integer> path = new HashSet<>();
        for (int i = 0; i < 2; i++) { // Perform two DFS traversals
            boolean[] visited = new boolean[edges.length];
            dfs(graph, targetDevice, targetDevice, visited, impactedDevices, path);
            path.clear();
            for (int j = 0; j < visited.length; j++) {
                if (!visited[j] && j != targetDevice) {
                    dfs(graph, targetDevice, j, visited, impactedDevices, path);
                }
            }
        }
        return impactedDevices;
    }

    private static void dfs(List<List<Integer>> graph, int targetDevice, int currentDevice, boolean[] visited, List<Integer> impactedDevices, Set<Integer> path) {
        if (visited[currentDevice]) {
            return;
        }
        visited[currentDevice] = true;
        path.add(currentDevice);
        for (int neighbor : graph.get(currentDevice)) {
            if (neighbor != targetDevice && !path.contains(neighbor)) {
                dfs(graph, targetDevice, neighbor, visited, impactedDevices, path);
            }
        }
        if (currentDevice != targetDevice && !visited[targetDevice]) {
            impactedDevices.add(currentDevice);
        }
        path.remove(currentDevice);
    }

    public static void main(String[] args) {
        int[][] edges = {{0,1},{0,2},{1,3},{1,6},{2,4},{4,6},{4,5},{5,7}};
        int targetDevice = 4;
        List<Integer> impactedDevices = findImpactedDevices(edges, targetDevice);
        HashSet<Integer> impactedSet = new HashSet<>(impactedDevices);
        System.out.println("Impacted Device Set = {" + impactedSet.toString().replaceAll("[\\[\\]]", "") + "}");
    }
}