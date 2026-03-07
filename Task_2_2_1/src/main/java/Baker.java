import java.util.List;

/**
 * Класс пекаря.
 */
public class Baker implements Runnable {
    private final int id;
    private final long speed;
    private final Channel queue;
    private final Channel warehouse;

    /**
     * Стандартный конструктор пекаря.
     *
     * @param id id пекаря
     * @param speed время готовки
     * @param queue очередь заказов
     * @param warehouse склад
     */
    public Baker(int id, long speed, Channel queue, Channel warehouse) {
        this.id = id;
        this.speed = speed;
        this.queue = queue;
        this.warehouse = warehouse;
    }

    /**
     * Запуск пекаря.
     */
    @Override
    public void run() {
        List<Integer> batch = null;
        try {
            while (true) {
                batch = queue.get(1);

                if (batch.isEmpty()) {
                    break;
                }

                for (int i = 0; i < batch.size(); i++) {
                    System.out.printf("[%d] (baker %d) BAKING\n", batch.get(0), id);
                    Thread.sleep(speed);
                    System.out.printf("[%d] (baker %d) BAKED\n", batch.get(0), id);
                    warehouse.put(batch.remove(0));
                }
                batch = null;
            }
        } catch (InterruptedException e) {
            if (batch != null) {
                try {
                    for (int orderId : batch) {
                        System.out.printf("[%d] (baker %d) RETURN_TO_QUEUE\n", orderId, id);
                        queue.put(orderId);
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
