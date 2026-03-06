import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Channel {
    private final int capacity;
    private boolean closed = false;
    private final Queue<Integer> queue = new LinkedList<>();

    public Channel(int capacity) {
        this.capacity = capacity;
    }

    public synchronized void close() {
        closed = true;
        notifyAll();
    }

    public synchronized void put(int id) throws InterruptedException {
        if (closed) {
            return;
        }

        while (queue.size() >= capacity && capacity != 0) {
            wait();
        }
        queue.add(id);

        notifyAll();
        System.out.printf("[%d] ON_CHANNEL\n", id);
    }

    public synchronized List<Integer> get(int maxCount) throws InterruptedException {
        while (queue.isEmpty() && !closed) {
            wait();
        }

        List<Integer> batch = new ArrayList<>();
        for (int i = 0; i < Math.min(maxCount, queue.size()); i++) {
            batch.add(queue.poll());
        }

        notifyAll();
        return batch;
    }
}
