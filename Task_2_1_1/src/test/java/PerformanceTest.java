import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Performance test for PrimeChecker.
 */
public class PerformanceTest {
    final int iterations = 2;
    static List<Long> data;

    @BeforeAll
    static void initData() throws IOException {
        try (Stream<String> lines = Files.lines(
                Paths.get("src/test/resources/performance_test_data.txt"))
        ) {
            data = lines
                    .map(Long::parseLong)
                    .collect(Collectors.toList());
        }
    }

    @Test
    void performanceTest() throws InterruptedException {
        long timeSum = 0;
        for (int i = 0; i < iterations; i++) {
            long start = System.nanoTime();
            PrimeChecker.hasCompositeSequential(data);
            timeSum += System.nanoTime() - start;
        }
        System.out.printf("Sequential: %7.2f мс\n", (timeSum / iterations) / 1e6);

        timeSum = 0;
        System.out.println("Parallel:");
        for (int threads = 2; threads <= 8; threads++) {
            for (int i = 0; i < iterations; i++) {
                long start = System.nanoTime();
                PrimeChecker.hasCompositeParallel(data, threads);
                timeSum += System.nanoTime() - start;
            }
            System.out.printf("  %2d threads: %7.2f мс\n", threads, (timeSum / iterations) / 1e6);
        }

        timeSum = 0;
        for (int i = 0; i < iterations; i++) {
            long start = System.nanoTime();
            PrimeChecker.hasCompositeParallelStream(data);
            timeSum += System.nanoTime() - start;
        }
        System.out.printf("ParallelStream: %7.2f мс\n", (timeSum / iterations) / 1e6);
    }
}