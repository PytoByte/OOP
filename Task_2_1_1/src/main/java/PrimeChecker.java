import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Prime number utilities with sequential and parallel composite checks.
 */
public class PrimeChecker {

    /**
     * Checks if a number is prime.
     *
     * @param n number to check
     * @return true if prime, false otherwise
     */
    public static boolean isPrime(long n) {
        if (n <= 1) {
            return false;
        }
        if (n == 2) {
            return true;
        }
        for (long i = 2; i * i <= n; i++) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks list for any composite number sequentially.
     *
     * @param numbers list of numbers
     * @return true if at least one composite found
     */
    public static boolean hasCompositeSequential(List<Long> numbers) {
        for (long num : numbers) {
            if (!isPrime(num)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks list for any composite number using multiple threads.
     *
     * @param numbers list of numbers
     * @param threadCount number of threads to use
     * @return true if at least one composite found
     * @throws InterruptedException if thread execution is interrupted
     */
    public static boolean hasCompositeThreads(List<Long> numbers, int threadCount)
            throws InterruptedException {
        int n = numbers.size();
        if (n == 0) {
            return false;
        }
        if (n < threadCount) {
            threadCount = n;
        }

        int chunkSize = n / threadCount;
        AtomicBoolean foundComposite = new AtomicBoolean(false);
        Thread[] threads = new Thread[threadCount];

        for (int i = 0; i < threadCount; i++) {
            final int start = i * chunkSize;
            final int end = (i + 1 == threadCount) ? n : start + chunkSize;

            threads[i] = new Thread(() -> {
                for (int j = start; j < end && !foundComposite.get(); j++) {
                    if (!isPrime(numbers.get(j))) {
                        foundComposite.set(true);
                        break;
                    }
                }
            });
            threads[i].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        return foundComposite.get();
    }

    /**
     * Checks list for any composite number using parallel stream.
     *
     * @param numbers list of numbers
     * @return true if at least one composite found
     */
    public static boolean hasCompositeParallelStream(List<Long> numbers) {
        return numbers.parallelStream()
                .unordered()
                .anyMatch(num -> !isPrime(num));
    }
}