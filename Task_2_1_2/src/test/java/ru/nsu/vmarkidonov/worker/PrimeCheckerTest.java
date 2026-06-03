package ru.nsu.vmarkidonov.worker;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class PrimeCheckerTest {

    @Test
    void testEdgeCases() {
        assertFalse(PrimeChecker.isPrime(-100));
        assertFalse(PrimeChecker.isPrime(-1));
        assertFalse(PrimeChecker.isPrime(0));
        assertFalse(PrimeChecker.isPrime(1));
        assertTrue(PrimeChecker.isPrime(2));
    }

    @Test
    void testSmallPrimeNumbers() {
        assertTrue(PrimeChecker.isPrime(3));
        assertTrue(PrimeChecker.isPrime(5));
        assertTrue(PrimeChecker.isPrime(7));
        assertTrue(PrimeChecker.isPrime(11));
        assertTrue(PrimeChecker.isPrime(13));
        assertTrue(PrimeChecker.isPrime(17));
    }

    @Test
    void testSmallCompositeNumbers() {
        assertFalse(PrimeChecker.isPrime(4));
        assertFalse(PrimeChecker.isPrime(6));
        assertFalse(PrimeChecker.isPrime(8));
        assertFalse(PrimeChecker.isPrime(9));
        assertFalse(PrimeChecker.isPrime(10));
        assertFalse(PrimeChecker.isPrime(12));
        assertFalse(PrimeChecker.isPrime(15));
    }

    @Test
    void testLargePrimeNumbers() {
        assertTrue(PrimeChecker.isPrime(6997901));
        assertTrue(PrimeChecker.isPrime(6997927));
        assertTrue(PrimeChecker.isPrime(17858849));
    }
}
