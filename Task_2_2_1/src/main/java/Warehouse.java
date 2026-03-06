import java.util.ArrayList;
import java.util.List;

public class Warehouse {
    private final List<Integer> storage = new ArrayList<>();
    private final int capacity;

    public Warehouse(int capacity) {
        this.capacity = capacity;
    }

    public synchronized void putPizza(int id) throws InterruptedException {
        while (storage.size() >= capacity) {
            wait();
        }
        storage.add(id);
        notifyAll();
        System.out.printf("[%d] ON_WAREHOUSE%n", id);
    }

    public synchronized List<Integer> takePizzas(int maxCount) throws InterruptedException {
        while (storage.isEmpty()) {
            wait();
        }
        int count = Math.min(maxCount, storage.size());
        List<Integer> batch = new ArrayList<>(storage.subList(0, count));
        storage.subList(0, count).clear();
        notifyAll();
        return batch;
    }

    public synchronized boolean isEmpty() {
        return storage.isEmpty();
    }
}
