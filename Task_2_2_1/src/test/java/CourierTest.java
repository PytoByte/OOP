import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
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

    @Test
    void testInterruptedException_returnsOrderToWarehouse() throws Exception {
        Channel warehouse = new Channel("W", 10);
        Courier courier = new Courier(1, 1, 10_000, warehouse);

        Thread t = new Thread(courier);
        t.start();

        warehouse.put(99);
        t.interrupt();
        t.join(2000);
        assertFalse(t.isAlive());

        List<Integer> result = warehouse.get(1);
        assertTrue(result.contains(99));
    }
}