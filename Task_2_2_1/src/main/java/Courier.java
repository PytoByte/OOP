import java.util.List;

/**
 * Класс курьера.
 */
public class Courier implements Runnable {
    private final int id;
    private final int capacity;
    private final long speed;
    private final Channel warehouse;

    /**
     * Стандартный конструктор курьера.
     *
     * @param id id курьера
     * @param capacity сколько курьер может забрать пицц со склада
     * @param speed время доставки в миллисекундах
     * @param warehouse склад
     * @throws IllegalArgumentException если capacity меньше 0
     */
    public Courier(int id, int capacity, long speed, Channel warehouse) {
        if (capacity < 0) {
            throw new IllegalArgumentException("capacity must be >= 0");
        }
        this.id = id;
        this.capacity = capacity;
        this.speed = speed;
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

                int batchSize = batch.size();
                for (int i = 0; i < batchSize; i++) {
                    System.out.printf("[%d] (courier %d) DELIVERING\n", batch.get(0), id);
                    Thread.sleep(speed);
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
