public class Baker implements Runnable {
    private final int id;
    private final long speed;
    private final OrderQueue queue;
    private final Warehouse warehouse;
    private volatile boolean running = true;

    public Baker(int id, long speed, OrderQueue q, Warehouse w) {
        this.id = id;
        this.speed = speed;
        this.queue = q;
        this.warehouse = w;
    }

    public void stop() {
        running = false;
    }

    @Override
    public void run() {
        try {
            while (running) {
                Integer orderId = queue.takeOrder();
                if (orderId == null) break;
                System.out.printf("[%d] BAKING_%d%n", orderId, id);
                Thread.sleep(speed);
                warehouse.putPizza(orderId);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
