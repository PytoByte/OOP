import java.util.ArrayList;
import java.util.List;

public class Warehouse {
    private final List<Integer> storage = new ArrayList<>();
    private final int capacity;
    private boolean closing = false;

    public Warehouse(int capacity) {
        this.capacity = capacity;
    }

    public synchronized void close() {
        closing = true;
        notifyAll();
    }

    public synchronized void putPizza(int id) throws InterruptedException {
        if (closing) {
            return;
        }

        while (storage.size() >= capacity) {
            wait();
        }
        storage.add(id);
        notifyAll();
        System.out.printf("[%d] ON_WAREHOUSE%n", id);
    }

    public synchronized List<Integer> takePizzas(int maxCount) throws InterruptedException {
        while (storage.isEmpty() && !closing) {
            wait();
        }

        int count = Math.min(maxCount, storage.size());
        List<Integer> batch = new ArrayList<>(storage.subList(0, count));
        storage.subList(0, count).clear();
        notifyAll();
        return batch;
    }
}
