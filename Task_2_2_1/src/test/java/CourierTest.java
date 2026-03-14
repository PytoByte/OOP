import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class CourierTest {

    @Test
    void testSingleOrder() throws Exception {
        Channel warehouse = new Channel("W", 10);
        long speed = 100;

        Thread t = new Thread(new Courier(1, 1, speed, warehouse));
        t.start();

        warehouse.put(1);
        Thread.sleep(speed + 50);
        warehouse.close();
        t.join();

        assertTrue(warehouse.get(1).isEmpty());
    }

    @Test
    void testMultipleOrders() throws Exception {
        Channel warehouse = new Channel("W", 10);
        long speed = 50;

        Thread t = new Thread(new Courier(1, 2, speed, warehouse));
        t.start();

        warehouse.put(1);
        warehouse.put(2);
        Thread.sleep((speed * 2) + 100);
        warehouse.close();
        t.join();

        assertTrue(warehouse.get(1).isEmpty());
    }
}