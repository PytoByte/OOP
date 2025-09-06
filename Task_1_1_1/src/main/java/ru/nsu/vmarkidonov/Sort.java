package ru.nsu.vmarkidonov;

/**
 * Class, containing sorting functions.
 */
public class Sort {
    /**
     * Returns left child index.
     *
     * @param parent index of parent
     * @return index of left child
     */
    private static int leftChild(int parent) {
        return parent * 2 + 1;
    }

    /**
     * Returns right child index.
     *
     * @param parent index of parent
     * @return index of right child
     */
    private static int rightChild(int parent) {
        return parent * 2 + 2;
    }

    /**
     * Swaps two elements in array.
     *
     * @param array input array
     * @param i     first index
     * @param j     second index
     */
    private static void swap(int[] array, int i, int j) {
        int tmp = array[i];
        array[i] = array[j];
        array[j] = tmp;
    }

    /**
     * Restores heap property by moving element down the heap.
     *
     * @param heap heap array
     * @param i    current index
     * @param size current heap size
     */
    private static void heapifyDown(int[] heap, int i, int size) {
        while (true) {
            int lChild = leftChild(i);
            int rChild = rightChild(i);

            if (lChild >= size) {
                break;
            }

            int largestChild = lChild;
            if (rChild < size && heap[rChild] > heap[lChild]) {
                largestChild = rChild;
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
     * Builds max-heap from array.
     *
     * @param array input array
     */
    private static void makeHeap(int[] array) {
        for (int i = array.length / 2 - 1; i >= 0; i--) {
            heapifyDown(array, i, array.length);
        }
    }

    /**
     * Heap sort aka pyramid sort.
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