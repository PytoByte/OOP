import java.util.List;

public class Courier implements Runnable {
    private final int id;
    private final int capacity;
    private final Channel warehouse;

    public Courier(int id, int cap, Channel warehouse) {
        this.id = id;
        this.capacity = cap;
        this.warehouse = warehouse;
    }

    @Override
    public void run() {
        List<Integer> batch = null;
        try {
            while (true) {
                batch = warehouse.get(capacity);

                if (batch.isEmpty()) {
                    break;
                }

                for (int i = 0; i < batch.size(); i++) {
                    System.out.printf("[%d] (courier %d) DELIVERING\n", batch.get(0), id);
                    Thread.sleep(1000);
                    System.out.printf("[%d] (courier %d) DELIVERED\n", batch.remove(0), id);
                }
                batch = null;
            }
        } catch (InterruptedException e) {
            if (batch != null) {
                try {
                    for (int orderId : batch) {
                        System.out.printf("[%d] (courier %d) RETURN_TO_WAREHOUSE\n", orderId, id);
                        warehouse.put(orderId);
                    }
                } catch (InterruptedException e1) {}
            }
        }
    }
}
