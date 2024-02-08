import java.util.PriorityQueue;

public class ScoreTracker {
    private PriorityQueue<Double> minHeap;
    private PriorityQueue<Double> maxHeap;
    private int size;

    public ScoreTracker() {
        minHeap = new PriorityQueue<>();
        maxHeap = new PriorityQueue<>((a, b) -> (int) (b - a));
        size = 0;
    }

    public void addScore(double score) {
        size++;
        minHeap.add(score);
        maxHeap.add(minHeap.poll());

        if (size % 2 == 1) {
            minHeap.add(maxHeap.poll());
        }
    }

    public double getMedianScore() {
        if (size == 0) {
            throw new IllegalStateException("No scores added yet.");
        }

        if (size % 2 == 1) {
            return minHeap.peek();
        }

        return (minHeap.peek() + maxHeap.peek()) / 2.0;
    }

    public static void main(String[] args) {
        ScoreTracker scoreTracker = new ScoreTracker();

        scoreTracker.addScore(85.5); 
        scoreTracker.addScore(92.3); 
        scoreTracker.addScore(77.8); 
        scoreTracker.addScore(90.1); 

        double median1 = scoreTracker.getMedianScore(); 
        System.out.println("Median 1 :" + median1);

        scoreTracker.addScore(81.2); 
        scoreTracker.addScore(88.7); 

        double median2 = scoreTracker.getMedianScore(); 
        System.out.println("Median 2 :" + median2);
    }
}