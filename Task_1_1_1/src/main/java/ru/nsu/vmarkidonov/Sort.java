package ru.nsu.vmarkidonov;

// TODO: documentation and tests, make heap as class

/**
 * Class, containing sorting functions
 */
class Sort {
    public static int parent(int index) {
        if (index % 2 == 0) {
            return index / 2 - 1;
        } else {
            return index / 2;
        }
    }

    public static int[] childs(int index) {
        return new int[] {index*2+1, (index+1)*2};
    }

    public static void swap(int[] array, int i, int j) {
        int tmp = array[i];
        array[i] = array[j];
        array[j] = tmp;
    }

    public static void heapify_down(int[] heap, int i, int end) {
        while (true) {
            printarr(heap);
            int[] cs = childs(i);

            if (cs[0] >= end && cs[1] >= end) {
                break;
            }

            int swapping_child;
            if (heap[cs[0]] <=> heap[i] || cs[1] >= end) {
                swapping_child = cs[0];
            } else {
                swapping_child = cs[1];
            }

            swap(heap, i, swapping_child);
            i = swapping_child;
        }
    }

    public static int[] makeheap(int[] array) {
        int[] heap = array.clone();
        for (int i = 0; i < array.length; i++) {
            heapify_down(heap, i, array.length);
        }
        return heap;
    }

    public static int heappop(int[] heap, int end) {
        swap(heap, 0 , end-1);
        heapify_down(heap, 0, end-1);
        return heap[end-1];
    }

    /**
     * Heap sort aka pyramid sort
     *
     * @param array shuffled array
     */
    public static void heapsort(int[] array) {
        int[] heap = makeheap(array);
        printarr(heap);
        for (int i = 0; i < heap.length; i++) {
            array[i] = heappop(heap, heap.length-i);
        }
    }

    public static void printarr(int[] array) {
        for (int j : array) {
            System.out.printf("%d ", j);
        }
        System.out.print("\n");
    }
}
