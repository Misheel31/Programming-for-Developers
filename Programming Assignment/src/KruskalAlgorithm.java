import java.util.*;

class Edge implements Comparable<Edge> {
    int src, dest, weight;

    public Edge(int src, int dest, int weight) {
        this.src = src;
        this.dest = dest;
        this.weight = weight;
    }

    @Override
    public int compareTo(Edge other) {
        return Integer.compare(this.weight, other.weight);
    }
}

class DisjointSet {
    private int[] parent, rank;

    public DisjointSet(int n) {
        parent = new int[n];
        rank = new int[n];

        for (int i = 0; i < n; i++) {
            parent[i] = i;
            rank[i] = 0;
        }
    }

    public int find(int x) {
        if (parent[x] != x) {
            parent[x] = find(parent[x]);
        }
        return parent[x];
    }

    public void union(int x, int y) {
        int rootX = find(x);
        int rootY = find(y);

        if (rootX != rootY) {
            if (rank[rootX] < rank[rootY]) {
                parent[rootX] = rootY;
            } else if (rank[rootX] > rank[rootY]) {
                parent[rootY] = rootX;
            } else {
                parent[rootX] = rootY;
                rank[rootY]++;
            }
        }
    }
}

public class KruskalAlgorithm {

    public static List<Edge> kruskalMST(List<Edge> edges, int numVertices) {
        List<Edge> result = new ArrayList<>();
        Collections.sort(edges); 

        DisjointSet disjointSet = new DisjointSet(numVertices);

        for (Edge edge : edges) {
            int rootSrc = disjointSet.find(edge.src);
            int rootDest = disjointSet.find(edge.dest);

            if (rootSrc != rootDest) {
                result.add(edge);
                disjointSet.union(rootSrc, rootDest);
            }
        }

        return result;
    }

    public static void main(String[] args) {
        int numVertices = 4;
        List<Edge> edges = new ArrayList<>();
        edges.add(new Edge(0, 0, 5));
        edges.add(new Edge(0, 2, 6));
        edges.add(new Edge(0, 3, 5));
        edges.add(new Edge(3, 2, 15));
        edges.add(new Edge(2, 3, 4));

        List<Edge> result = kruskalMST(edges, numVertices);

        System.out.println("Minimum Spanning Tree Edges:");
        for (Edge edge : result) {
            System.out.println(edge.src + " - " + edge.dest + ": " + edge.weight);
        }
    }
}
