package ru.nsu.vmarkidonov;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

// TODO: tests

class SortTest {
    @Test
    void heapsort() {
        int[] array = {1, 6, 5, 3, 7};
        Sort.heapsort(array);

        for (int j : array) {
            System.out.printf("%d ", j);
        }
        System.out.print("\n");

        assertArrayEquals(new int[]{1, 3, 5, 6, 7}, array);
    }
}