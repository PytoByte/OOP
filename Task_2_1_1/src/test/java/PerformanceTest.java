import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PerformanceTest {
    final static int ITERATIONS = 2;
    static List<Long> DATA;

    @Test
    void performanceTest() throws IOException, InterruptedException {
        DATA = Files.lines(Paths.get("src/test/resources/performance_test_data.txt"))
                .map(Long::parseLong)
                .collect(Collectors.toList());

        long seqSum = 0;
        for (int i = 0; i < ITERATIONS; i++) {
            long start = System.nanoTime();
            PrimeChecker.hasCompositeSequential(DATA);
            seqSum += System.nanoTime() - start;
        }
        System.out.printf("Sequential: %7.2f мс\n", (seqSum / ITERATIONS) / 1e6);

        System.out.println("Parallel:");
        for (int threads = 2; threads <= 8; threads++) {
            long sum = 0;
            for (int i = 0; i < ITERATIONS; i++) {
                long start = System.nanoTime();
                PrimeChecker.hasCompositeParallel(DATA, threads);
                sum += System.nanoTime() - start;
            }
            System.out.printf("  %2d threads: %7.2f мс\n", threads, (sum / ITERATIONS) / 1e6);
        }

        long streamSum = 0;
        for (int i = 0; i < ITERATIONS; i++) {
            long start = System.nanoTime();
            PrimeChecker.hasCompositeParallelStream(DATA);
            streamSum += System.nanoTime() - start;
        }
        System.out.printf("ParallelStream: %7.2f мс\n", (streamSum / ITERATIONS) / 1e6);
    }
}