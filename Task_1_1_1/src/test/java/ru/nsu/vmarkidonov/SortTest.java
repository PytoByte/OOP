package ru.nsu.vmarkidonov;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Random;

class SortTest {
    @Test
    void heapsortPositive() {
        int[] array = {1, 6, 5, 3, 7};
        Sort.heapSort(array);
        assertArrayEquals(new int[]{1, 3, 5, 6, 7}, array);
    }

    @Test
    void heapsortNegative() {
        int[] array = {-1, -6, -5, -3, -7};
        Sort.heapSort(array);
        assertArrayEquals(new int[]{-7, -6, -5, -3, -1}, array);
    }

    @Test
    void heapsortPositiveNegativeZero() {
        int[] array = {1, 4, 10, -2, -6, 0, -5};
        Sort.heapSort(array);
        assertArrayEquals(new int[]{-6, -5, -2, 0, 1, 4, 10}, array);
    }

    @Test
    void heapsortEmpty() {
        int[] array = {};
        Sort.heapSort(array);
        assertArrayEquals(new int[]{}, array);
    }

    @Test
    void heapsortNull() {
        Sort.heapSort(null);
    }

    @Test
    void heapsortOneElem() {
        int[] array = {10};
        Sort.heapSort(array);
        assertArrayEquals(new int[]{10}, array);
    }

    @Test
    void heapsortBigArray() {
        int size = 1000000;
        Random random = new Random();
        int[] array = new int[size];
        int[] expected = new int[size];

        for (int i = 0; i < size; i++) {
            int value = random.nextInt(size) - 500000;
            array[i] = value;
            expected[i] = value;
        }

        Sort.heapSort(array);

        Arrays.sort(expected);
        
        assertArrayEquals(expected, array);
    }
}