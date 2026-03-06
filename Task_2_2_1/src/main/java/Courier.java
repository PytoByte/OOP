import java.util.List;

public class Courier implements Runnable {
    private final int id;
    private final int capacity;
    private final Warehouse warehouse;
    private volatile boolean running = true;
    public Courier(int id, int cap, Warehouse w) {
        this.id=id;
        this.capacity=cap;
        this.warehouse=w;
    }

    public void stop() {
        running = false;
    }

    @Override
    public void run() {
        try {
            while (running) {
                List<Integer> batch = warehouse.takePizzas(capacity);

                if (batch.isEmpty()) {
                    break;
                }

                for (int pid : batch) {
                    System.out.printf("[%d] DELIVERING_%d%n", pid, id);
                    Thread.sleep(1000); // Delivery time
                    System.out.printf("[%d] DELIVERED%n", pid);
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
