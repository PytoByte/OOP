import java.util.LinkedList;
import java.util.Queue;

public class OrderQueue {
    private final Queue<Integer> queue = new LinkedList<>();
    private boolean closing = false;

    public synchronized void close() {
        closing = true;
        notifyAll();
    }

    public synchronized void addOrder(int id) throws InterruptedException {
        if (closing) {
            return;
        }

        queue.add(id);
        notifyAll();
        System.out.printf("[%d] ORDER_CREATED%n", id);
    }

    public synchronized Integer takeOrder() throws InterruptedException {
        while (queue.isEmpty() && !closing) {
            wait();
        }

        if (queue.isEmpty()) {
            return null;
        }

        Integer id = queue.poll();
        notifyAll();
        return id;
    }

    public synchronized boolean isClosing() {
        return this.closing;
    }
}