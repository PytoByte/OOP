package ru.nsu.vmarkidonov.worker;

/**
 * Утилитарный класс для проверки чисел на простоту.
 */
public class PrimeChecker {

    /**
     * Проверяет, является ли заданное число простым.
     *
     * @param n проверяемое число
     * @return true, если число является простым, и false, если оно составное или меньше либо равно 1
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
}
