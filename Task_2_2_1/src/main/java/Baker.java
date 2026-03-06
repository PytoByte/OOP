public class Baker implements Runnable {
    private final int id;
    private final long speed;
    private final OrderQueue queue;
    private final Warehouse warehouse;

    public Baker(int id, long speed, OrderQueue q, Warehouse w) {
        this.id = id;
        this.speed = speed;
        this.queue = q;
        this.warehouse = w;
    }

    @Override
    public void run() {
        Integer orderId = null;
        try {
            while (true) {
                orderId = queue.takeOrder();
                if (orderId == null) {
                    break;
                }
                System.out.printf("[%d] (baker %d) BAKING\n", orderId, id);
                Thread.sleep(speed);
                System.out.printf("[%d] (baker %d) BAKED\n", orderId, id);
                warehouse.putPizza(orderId);
                orderId = null;
            }
        } catch (InterruptedException _) {
            if (orderId != null) {
                try {
                    System.out.printf("[%d] (baker %d) RETURN_TO_QUEUE\n", orderId, id);
                    queue.addOrder(orderId);
                } catch (InterruptedException _) {}
            }
        }
    }
}
