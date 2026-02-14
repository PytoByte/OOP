import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests for PrimeChecker.
 */
class PrimeCheckerTest {
    @ParameterizedTest
    @ValueSource(longs = {-10, -1, 0, 1})
    void isPrime_nonPositiveOrOne_returnsFalse(long n) {
        assertFalse(PrimeChecker.isPrime(n));
    }

    @Test
    void isPrime_two_returnsTrue() {
        assertTrue(PrimeChecker.isPrime(2));
    }

    @ParameterizedTest
    @ValueSource(longs = {3, 5, 7, 11, 17, 97, 101, 999983})
    void isPrime_primes_returnsTrue(long n) {
        assertTrue(PrimeChecker.isPrime(n));
    }

    @ParameterizedTest
    @ValueSource(longs = {4, 6, 8, 9, 10, 15, 100, 1000, 999984})
    void isPrime_composites_returnsFalse(long n) {
        assertFalse(PrimeChecker.isPrime(n));
    }

    @Test
    void hasCompositeSequential_emptyList_returnsFalse() {
        assertFalse(PrimeChecker.hasCompositeSequential(List.of()));
    }

    @Test
    void hasCompositeSequential_allPrimes_returnsFalse() {
        assertFalse(PrimeChecker.hasCompositeSequential(List.of(2L, 3L, 5L, 7L, 11L)));
    }

    @Test
    void hasCompositeSequential_containsComposite_returnsTrue() {
        assertTrue(PrimeChecker.hasCompositeSequential(List.of(2L, 4L, 5L)));
    }

    @Test
    void hasCompositeThreads_emptyList_returnsFalse() throws InterruptedException {
        assertFalse(PrimeChecker.hasCompositeThreads(List.of(), 4));
    }

    @Test
    void hasCompositeThreads_splitListCorrectly() throws InterruptedException {
        List<Long> data = List.of(3L, 5L, 7L, 11L, 4L);
        assertTrue(PrimeChecker.hasCompositeThreads(data, 4));
    }

    @Test
    void hasCompositeThreads_threadCountExceedsListSize_worksCorrectly()
            throws InterruptedException {
        List<Long> data = List.of(2L, 4L);
        assertTrue(PrimeChecker.hasCompositeThreads(data, 10));
    }

    @Test
    void hasCompositeThreads_zeroThreadCount_throwsArithmeticException() {
        assertThrows(ArithmeticException.class, () ->
                PrimeChecker.hasCompositeThreads(List.of(2L, 4L), 0));
    }

    @Test
    void hasCompositeParallelStream_emptyList_returnsFalse() {
        assertFalse(PrimeChecker.hasCompositeParallelStream(List.of()));
    }

    @Test
    void hasCompositeParallelStream_allPrimes_returnsFalse() {
        assertFalse(PrimeChecker.hasCompositeParallelStream(List.of(2L, 3L, 5L, 7L)));
    }

    @Test
    void hasCompositeParallelStream_containsComposite_returnsTrue() {
        assertTrue(PrimeChecker.hasCompositeParallelStream(List.of(2L, 9L, 5L)));
    }

    @ParameterizedTest
    @ValueSource(strings = {"2,3,5,7,11", "4,6,8,9", "2,3,4,5", ""})
    void allMethods_consistent(String nums) throws InterruptedException {
        List<Long> list = nums.isEmpty()
                ? List.of()
                : Arrays.stream(nums.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());

        boolean seq = PrimeChecker.hasCompositeSequential(list);
        boolean parallel = PrimeChecker.hasCompositeThreads(list, Math.max(1, list.size() / 2));
        boolean stream = PrimeChecker.hasCompositeParallelStream(list);

        assertEquals(seq, parallel);
        assertEquals(seq, stream);
    }
}