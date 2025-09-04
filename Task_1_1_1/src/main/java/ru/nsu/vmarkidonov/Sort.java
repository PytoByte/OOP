package ru.nsu.vmarkidonov;

/**
 * Class, containing sorting functions
 */
public class Sort {
    /**
     * Returns child indices for given index in heap
     *
     * @param index current index
     * @return array with left and right child indices
     */
    public static int[] childs(int index) {
        return new int[]{index * 2 + 1, index * 2 + 2};
    }

    /**
     * Swaps two elements in array
     *
     * @param array input array
     * @param i     first index
     * @param j     second index
     */
    public static void swap(int[] array, int i, int j) {
        int tmp = array[i];
        array[i] = array[j];
        array[j] = tmp;
    }

    /**
     * Restores heap property by moving element down the heap
     *
     * @param heap heap array
     * @param i    current index
     * @param end  end index (exclusive)
     */
    public static void heapifyDown(int[] heap, int i, int end) {
        while (true) {
            int[] children = childs(i);
            int leftChild = children[0];
            int rightChild = children[1];

            if (leftChild >= end) {
                break;
            }

            int largestChild = leftChild;
            if (rightChild < end && heap[rightChild] > heap[leftChild]) {
                largestChild = rightChild;
            }

            if (heap[largestChild] > heap[i]) {
                swap(heap, i, largestChild);
                i = largestChild;
            } else {
                break;
            }
        }
    }

    /**
     * Builds max-heap from array
     *
     * @param array input array
     */
    public static void makeHeap(int[] array) {
        for (int i = array.length / 2 - 1; i >= 0; i--) {
            heapifyDown(array, i, array.length);
        }
    }

    /**
     * Heap sort aka pyramid sort
     *
     * @param array array to be sorted
     */
    public static void heapSort(int[] array) {
        if (array == null || array.length <= 1) {
            return;
        }

        makeHeap(array);

        for (int i = array.length - 1; i > 0; i--) {
            swap(array, 0, i);
            heapifyDown(array, 0, i);
        }
    }
}