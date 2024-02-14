import java.util.PriorityQueue;

public class Spaceship {

    public static int minTimeToBuildEngines(int[] engines, int splitCost) {
        // Create a minimum heap to store the time to build each engine
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        int totalTime = 0;

        // Initially, there is only one engineer available
        for (int engine : engines) {
            minHeap.offer(engine);
        }

        while (minHeap.size() > 1) {
            // Take two engineers with the smallest workload
            int engineer1 = minHeap.poll();
            int engineer2 = minHeap.poll();

            totalTime += splitCost;
        }

        return totalTime;
    }

    public static void main(String[] args) {
        int[] engines = {3, 4, 5, 2};
        int splitCost = 2;

        int minTime = minTimeToBuildEngines(engines, splitCost);

        System.out.println("Minimum time needed to build all engines: " + minTime);
    }
}
