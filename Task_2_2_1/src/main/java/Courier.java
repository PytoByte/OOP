import java.util.List;

/**
 * Класс курьера.
 */
public class Courier implements Runnable {
    private final int id;
    private final int capacity;
    private final Channel warehouse;

    /**
     * Стандартный конструктор курьера.
     *
     * @param id id курьера
     * @param capacity сколько курьер может забрать пицц со склада
     * @param warehouse склад
     */
    public Courier(int id, int capacity, Channel warehouse) {
        this.id = id;
        this.capacity = capacity;
        this.warehouse = warehouse;
    }

    /**
     * Запуск курьера.
     */
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
                } catch (InterruptedException e1) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
            Thread.currentThread().interrupt();
        }
    }
}
