public class ClothingManufacturing {

    public static int minMovesToEqualize(int[] dresses) {
        int n = dresses.length;
        int totalDresses = 0;

        for (int dress : dresses) {
            totalDresses += dress;
        }

        int targetDresses = totalDresses / n;
        int moves = 0;
        int balance = 0;

        for (int i = 0; i < n; i++) {
            balance += dresses[i] - targetDresses;
            moves += Math.abs(balance);
        }

        return moves / 2; 
    }

    public static void main(String[] args) {
        int[] dresses = { 1, 0, 5};
        int result = minMovesToEqualize(dresses);

        System.out.println("Minimum moves needed: " + result);
    }
}
