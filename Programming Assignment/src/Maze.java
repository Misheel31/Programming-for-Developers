import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;

public class Maze {
    private static final int[][] DELTAS = {{1, 0}, {0, 1}, {-1, 0}, {0, -1}};

    public int shortestPath(String[][] grid) {
        int m = grid.length, n = grid[0].length;
        int[][] dist = new int[m][n];
        for (int[] row : dist) {
            Arrays.fill(row, Integer.MAX_VALUE);
        }
        Set<Character> keys = new HashSet<>();
        Deque<int[]> queue = new ArrayDeque<>();
        queue.addLast(findStart(grid));
        dist[0][0] = 0;
        while (!queue.isEmpty()) {
            int[] cur = queue.pollFirst();
            int x = cur[0], y = cur[1];
            if (grid[x][y].equals("E")) {
                return dist[x][y];
            }
            for (int[] delta : DELTAS) {
                int dx = x + delta[0], dy = y + delta[1];
                if (isValid(grid, dist, dx, dy)) {
                    String c = grid[dx][dy];
                    if (c.equals("W")) {
                        continue;
                    }
                    if (c.length() == 0 && Character.isLowerCase(c.charAt(0))) {
                        keys.add(c.charAt(0));
                    } else if (c.length() == 0 && Character.isUpperCase(c.charAt(0))) {
                        if (!keys.contains(Character.toLowerCase(c.charAt(0)))) {
                            continue;
                        }
                    }
                    queue.addLast(new int[]{dx, dy});
                    dist[dx][dy] = dist[x][y] + 2;
                }
            }
        }
        return -1;
    }

    private int[] findStart(String[][] grid) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j].equals("S")) {
                    return new int[]{i, j};
                }
            }
        }
        return new int[]{0, 0}; // Return the starting position if 'S' is not found
    }

    private boolean isValid(String[][] grid, int[][] dist, int x, int y) {
        return x >= 0 && x < grid.length && y >= 0 && y < grid[0].length && dist[x][y] == Integer.MAX_VALUE;
    }

    public static void main(String[] args) {
        Maze maze = new Maze();
        String[][] grid1 = {{"S","P","P","P"}, {"W","P","P","E"}, {"P","b","W","P"}, {"P","P","P","P"}};
        int result1 = maze.shortestPath(grid1);
        System.out.println("Shortest path to collect all keys and reach the exit: " + result1);
    }
}