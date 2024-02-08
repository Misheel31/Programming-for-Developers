import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class ANTColony {

    private double[][] distances;
    private double[][] pheromones;
    private int numAnts;
    private double decay;
    private double alpha;
    private double beta;

    public ANTColony(double[][] distances, int numAnts, double decay, double alpha, double beta) {
        this.distances = distances;
        this.pheromones = new double[distances.length][distances.length];
        this.numAnts = numAnts;
        this.decay = decay;
        this.alpha = alpha;
        this.beta = beta;

        // Initialize pheromones
        for (int i = 0; i < pheromones.length; i++) {
            for (int j = 0; j < pheromones[i].length; j++) {
                pheromones[i][j] = 1.0;
            }
        }
    }

    public void run(int numIterations) {
        for (int iteration = 0; iteration < numIterations; iteration++) {
            List<List<Integer>> antTours = new ArrayList<>();
            for (int i = 0; i < numAnts; i++) {
                List<Integer> tour = generateTour();
                antTours.add(tour);
            }

            updatePheromones(antTours);
            decayPheromones();
        }
    }

    private List<Integer> generateTour() {
        List<Integer> tour = new ArrayList<>();
        Set<Integer> visited = new HashSet<>();

        int currentCity = new Random().nextInt(distances.length);
        visited.add(currentCity);
        tour.add(currentCity);

        for (int i = 0; i < distances.length - 1; i++) {
            int nextCity = selectNextCity(currentCity, visited);
            tour.add(nextCity);
            visited.add(nextCity);
            currentCity = nextCity;
        }

        tour.add(tour.get(0)); // Complete the tour by returning to the starting city
        return tour;
    }

    private int selectNextCity(int currentCity, Set<Integer> visited) {
        double[] probabilities = new double[distances.length - visited.size()];
        int index = 0;

        for (int i = 0; i < distances.length; i++) {
            if (!visited.contains(i)) {
                probabilities[index] = Math.pow(pheromones[currentCity][i], alpha) * Math.pow(1.0 / distances[currentCity][i], beta);
                index++;
            }
        }

        double totalProbability = Arrays.stream(probabilities).sum();
        double randomValue = new Random().nextDouble() * totalProbability;

        for (int i = 0; i < probabilities.length; i++) {
            randomValue -= probabilities[i];
            if (randomValue <= 0) {
                int nextCity = 0;
                for (int j = 0; j < distances.length; j++) {
                    if (!visited.contains(j)) {
                        if (i == 0) {
                            nextCity = j;
                            break;
                        }
                        i--;
                    }
                }
                return nextCity;
            }
        }

        throw new RuntimeException("Error in selecting next city.");
    }

    private void updatePheromones(List<List<Integer>> antTours) {
        for (int i = 0; i < pheromones.length; i++) {
            for (int j = 0; j < pheromones[i].length; j++) {
                pheromones[i][j] *= decay;
            }
        }

        for (List<Integer> tour : antTours) {
            double tourLength = calculateTourLength(tour);
            for (int i = 0; i < tour.size() - 1; i++) {
                int city1 = tour.get(i);
                int city2 = tour.get(i + 1);
                pheromones[city1][city2] += 1.0 / tourLength;
                pheromones[city2][city1] += 1.0 / tourLength;
            }
        }
    }

    private void decayPheromones() {
        for (int i = 0; i < pheromones.length; i++) {
            for (int j = 0; j < pheromones[i].length; j++) {
                pheromones[i][j] *= decay;
            }
        }
    }

    private double calculateTourLength(List<Integer> tour) {
        double length = 0.0;
        for (int i = 0; i < tour.size() - 1; i++) {
            int city1 = tour.get(i);
            int city2 = tour.get(i + 1);
            length += distances[city1][city2];
        }
        return length;
    }

    public static void main(String[] args) {
        // Example usage:
        double[][] distances = {
            {Double.POSITIVE_INFINITY, 2, 2, 5, 7},
            {2, Double.POSITIVE_INFINITY, 4, 8, 2},
            {2, 4, Double.POSITIVE_INFINITY, 1, 3},
            {5, 8, 1, Double.POSITIVE_INFINITY, 2},
            {7, 2, 3, 2, Double.POSITIVE_INFINITY}
        };

        int numAnts = 5;
        double decay = 0.95;
        double alpha = 1.0;
        double beta = 2.0;

        ANTColony antColony = new ANTColony(distances, numAnts, decay, alpha, beta);
        antColony.run(100);

        // Print the final pheromone matrix
        System.out.println("Final Pheromone Matrix:");
        for (double[] row : antColony.pheromones) {
            System.out.println(Arrays.toString(row));
        }
    }
}
