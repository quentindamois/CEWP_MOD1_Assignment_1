import java.util.Arrays;
/**
 * generated with chat gpt
 * */
public class GroveAlgorithmSequentiel {

    public static void main(String[] args) {
        // Define the size of the database
        int n = 128; // You can change this to your desired size
        System.out.println("The database size is " + n + ".");

        // Number of iterations (can be determined based on the database size)
        int iterations = (int) (Math.PI / 4 * Math.sqrt(n));

        // Create the database as an array of boolean values
        boolean[] database = new boolean[n];
        // Assume one entry is marked as the solution
        int indexSolution = (int)(Math.random() * (n-1));
        System.out.println("We put the solution at the index " + indexSolution + ".");
        database[indexSolution] = true; // Change this to your desired solution

        // Initialize the equal superposition
        double[] amplitudes = new double[n];
        Arrays.fill(amplitudes, 1.0 / Math.sqrt(n));

        // Perform Grover iterations
        for (int i = 0; i < iterations; i++) {
            // Phase inversion
            for (int j = 0; j < n; j++) {
                if (database[j]) {
                    amplitudes[j] = -amplitudes[j];
                }
            }

            // Amplitude amplification
            double sum = 0;
            for (int j = 0; j < n; j++) {
                sum += amplitudes[j];
            }
            double avg = sum / n;

            for (int j = 0; j < n; j++) {
                amplitudes[j] = 2 * avg - amplitudes[j];
            }
        }

        // Measure the state
        double maxAmplitude = 0;
        int maxIndex = 0;
        for (int i = 0; i < n; i++) {
            if (amplitudes[i] > maxAmplitude) {
                maxAmplitude = amplitudes[i];
                maxIndex = i;
            }
        }

        System.out.println("Solution found at index with sequential " + maxIndex + ".");
    }
}
