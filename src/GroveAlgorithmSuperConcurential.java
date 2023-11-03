import java.util.Arrays;
        import java.util.concurrent.ExecutorService;
        import java.util.concurrent.Executors;
        import java.util.concurrent.locks.Lock;
        import java.util.concurrent.locks.ReentrantLock;

public class GroveAlgorithmSuperConcurential {
    public static int n;
    public static int iterations;
    public static boolean[] database;
    public static double[] amplitudes;
    public static double  maxAmplitude = 0;
    public static int maxIndex = 0;
    //public static int i;
    private static GroverIteration aGroverIteration = new GroverIteration();
    public static void main(String[] args) {
        ExecutorService executor = Executors.newCachedThreadPool();
        // Define the size of the database
        n = 16777216; // You can change this to your desired size

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
        /*
        double maxAmplitude = 0;
        int maxIndex = 0;
        */
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
        private static double sum;
        private static double avg;
        private static oneGroverIterationAction groverStep = new oneGroverIterationAction();
        public void OneGroverIteration() {
            ExecutorService executor = Executors.newCachedThreadPool();
            lock.lock();
            for (int j = 0; j < n; j++) {
                executor.execute(new doOneInvertion(j));
            }
            executor.shutdown();
            while(!executor.isTerminated())
            {

            }
            ExecutorService executor1 = Executors.newCachedThreadPool();
            // Amplitude amplification
            sum = 0;
            for (int j = 0; j < n; j++) {
                executor1.execute(new doAmplitudeAmplufication(j));
            }
            executor1.shutdown();
            while(!executor1.isTerminated())
            {

            }
            avg = sum / n;
            ExecutorService executor2 = Executors.newCachedThreadPool();
            for (int j = 0; j < n; j++) {
                executor2.execute(new doAmplitudeUpdate(j));
            }
            executor2.shutdown();
            while(!executor2.isTerminated())
            {

            }
            lock.unlock();
        }
        public class doOneInvertion implements Runnable {
            int i;
            doOneInvertion(int i) {
                super();
                this.i = i;
            }
            public void run(){
                groverStep.invertion(i);
            }
        }
        public class doAmplitudeAmplufication implements Runnable {
            private int i;
            doAmplitudeAmplufication(int i) {
                super();
                this.i = i;
            }
            public void run() {
                groverStep.amplitudeAmplification(i);
            }
        }
        public  class doAmplitudeUpdate implements Runnable {
            private int i;
            doAmplitudeUpdate(int i) {
                super();
                this.i = i;
            }
            public void run() {
                groverStep.amplitudeUpdated(i);
            }
        }
        public static class oneGroverIterationAction {
            private static Lock lock = new ReentrantLock();

            public  void invertion(int number) {
                lock.lock();
                if (database[number]) {

                    amplitudes[number] = -amplitudes[number];
                }
                lock.unlock();
            }
            public  void amplitudeAmplification(int number) {
                lock.lock();
                sum += amplitudes[number];
                lock.unlock();
            }
            public  void amplitudeUpdated(int number) {
                lock.lock();
                amplitudes[number] = 2 * avg - amplitudes[number];
                lock.unlock();
            }
        }
    }

}