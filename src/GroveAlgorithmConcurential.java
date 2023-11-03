import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class GroveAlgorithmConcurential {
    public static int n;
    public static int iterations;
    public static boolean[] database;
    public static double[] amplitudes;
    private static GroverIteration aGroverIteration = new GroverIteration();
    public static void main(String[] args) {
        ExecutorService executor = Executors.newCachedThreadPool();
        // Define the size of the database
        n = 1000000; // You can change this to your desired size
        System.out.println("The database size is " + n);
        // Number of iterations (can be determined based on the database size)
        iterations = (int) (Math.PI / 4 * Math.sqrt(n));

        // Create the database as an array of boolean values
        database = new boolean[n];
        // Assume one entry is marked as the solution
        int indexSolution = (int)(Math.random() * (n-1));
        System.out.println("We put the solution at the index " + indexSolution);
        database[indexSolution] = true; // Change this to your desired solution

        // Initialize the equal superposition
        amplitudes = new double[n];
        Arrays.fill(amplitudes, 1.0 / Math.sqrt(n));

        // Perform Grover iterations
        for (int i = 0; i < iterations; i++) {
            executor.execute(new doOneGroverIteration());
        }

        executor.shutdown();
        while(!executor.isTerminated())
        {

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

        System.out.println("Solution found at index with multi thread wow " + maxIndex);
    }
    public static class doOneGroverIteration implements Runnable {
        public void run() {
            aGroverIteration.OneGroverIteration();
        }
    }
    private static class GroverIteration {
        private static Lock lock = new ReentrantLock();
        public void OneGroverIteration() {
            lock.lock();
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
            lock.unlock();
        }
    }
}