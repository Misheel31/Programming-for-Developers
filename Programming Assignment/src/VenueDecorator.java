public class VenueDecorator {

    public static int findMinimumCost(int[][] costs) {
        if (costs == null || costs.length == 0 || costs[0].length == 0) {
            return 0;
        }

        int n = costs.length;
        int k = costs[0].length;

        int[][] dp = new int[n][k];

        for (int j = 0; j < k; j++) {
            dp[0][j] = costs[0][j];
        }

        for (int i = 1; i < n; i++) {
            for (int j = 0; j < k; j++) {
                int minCost = Integer.MAX_VALUE;
                for (int l = 0; l < k; l++) {
                    if (l != j) {
                        minCost = Math.min(minCost, dp[i - 1][l]);
                    }
                }
                dp[i][j] = minCost + costs[i][j];
            }
        }

        int result = Integer.MAX_VALUE;
        for (int j = 0; j < k; j++) {
            result = Math.min(result, dp[n - 1][j]);
        }

        return result;
    }

    public static void main(String[] args) {
        int[][] costs2 = {{1, 3, 2}, {4, 6, 8}, {3, 1, 5}};
        System.out.println(findMinimumCost(costs2)); 
    }
}
