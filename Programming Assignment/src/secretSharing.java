import java.util.*;

public class secretSharing {

    public static List<Integer> getIndividualsKnowingSecret(int n, int[][] intervals, int firstPerson) {
        List<Integer> result = new ArrayList<>();
        Set<Integer> knownIndividuals = new HashSet<>();

        // Initialize the set with the first person
        knownIndividuals.add(firstPerson);
        result.add(firstPerson);

        // Iterate through each individual and check if they are within any of the intervals
        for (int i = 1; i < n; i++) {
            boolean knowsSecret = false;

            for (int[] interval : intervals) {
                if (i >= interval[0] && i <= interval[1]) {
                    knowsSecret = true;
                    break;
                }
            }

            if (knowsSecret) {
                knownIndividuals.add(i);
                result.add(i);
            }
        }

        return result;
    }

    public static void main(String[] args) {
        int n = 5;
        int[][] intervals = {{0, 2}, {1, 3}, {2, 4}};
        int firstPerson = 0;

        List<Integer> result = getIndividualsKnowingSecret(n, intervals, firstPerson);

        System.out.println("Individuals who know the secret: " + result);
    }
}