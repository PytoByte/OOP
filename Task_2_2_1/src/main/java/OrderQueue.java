import java.util.LinkedList;
import java.util.Queue;

class OrderQueue {
    private final Queue<Integer> queue = new LinkedList<>();
    private boolean accepting = true;

    public synchronized void addOrder(int id) throws InterruptedException {
        while (!accepting) {
            wait();
        }
        queue.add(id);
        notifyAll();
        System.out.printf("[%d] ORDER_CREATED%n", id);
    }

    public synchronized Integer takeOrder() throws InterruptedException {
        while (queue.isEmpty() && accepting) {
            wait();
        }
        if (queue.isEmpty()) {
            return null;
        }
        Integer id = queue.poll();
        notifyAll();
        return id;
    }

    public synchronized void stopAccepting() {
        accepting = false;
        notifyAll();
    }

    public synchronized boolean isEmpty() {
        return queue.isEmpty();
    }

    public synchronized boolean isAccepting() {
        return this.accepting;
    }
}