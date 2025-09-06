package ru.nsu.vmarkidonov;

import java.util.Arrays;
import java.util.Random;

class SortMachine {
    public static void main(String[] args) {

        int size = 10000000;
        Random random = new Random();
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            int value = random.nextInt(size) - 5000000;
            array[i] = value;
        }

        System.out.printf("Array size: %d\n", size);
        System.out.print("Run sorting...\n");
        long timeStart = System.currentTimeMillis();
        Sort.heapSort(array);
        System.out.printf("Heap sorting time: %.03f seconds\n",
                (double) (System.currentTimeMillis() - timeStart) / 1000);

        System.out.print("Run trusted sorting algorithm java.util.Arrays.sort ...\n");
        int[] expected = array.clone();
        Arrays.sort(expected);

        System.out.print("Comparing results...\n");
        for (int i = 0; i < array.length; i++) {
            if (array[i] != expected[i]) {
                System.out.print("Heap sort wrong result\n");
                return;
            }
        }

        System.out.print("Heap sort correct result\n");

    }
}
