public class Spaceship {

    public static int minTimeToBuildEngines(int[] engines, int splitCost) {
        int n = engines.length;
        int[] dp = new int[n + 1];
        dp[0] = 0;

        for (int i = 1; i <= n; i++) {
            dp[i] = Integer.MAX_VALUE;
            int time = 0;

            for (int j = i; j > 0; j--) {
                time = Math.max(time, engines[j - 1]);
                dp[i] = Math.min(dp[i], time + dp[i - j] + (j > 1 ? splitCost : 0));
            }
        }

        return dp[n];
    }

    public static void main(String[] args) {
        int[] engines = {1, 2, 3};
        int splitCost = 1;
        int minTime = minTimeToBuildEngines(engines, splitCost);
        System.out.println("Minimum time needed to build all engines: " + minTime);
    }
}
