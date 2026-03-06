import java.util.List;

public class Courier implements Runnable {
    private final int id;
    private final int capacity;
    private final Warehouse warehouse;

    public Courier(int id, int cap, Warehouse w) {
        this.id = id;
        this.capacity = cap;
        this.warehouse = w;
    }

    @Override
    public void run() {
        List<Integer> batch = null;
        try {
            while (true) {
                batch = warehouse.takePizzas(capacity);

                if (batch.isEmpty()) {
                    break;
                }

                for (int pid : batch) {
                    System.out.printf("[%d] (courier %d) DELIVERING\n", pid, id);
                    Thread.sleep(1000);
                    System.out.printf("[%d] (courier %d) DELIVERED\n", pid, id);
                }
                batch = null;
            }
        } catch (InterruptedException _) {
            if (batch != null) {
                try {
                    for (int pid : batch) {
                        System.out.printf("[%d] (courier %d) RETURN_TO_WAREHOUSE\n", pid, id);
                        warehouse.putPizza(pid);
                    }
                } catch (InterruptedException _) {}
            }
        }
    }
}
