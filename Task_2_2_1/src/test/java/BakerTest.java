import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;

class BakerTest {

    @Test
    void testSingleOrder() throws Exception {
        Channel queue = new Channel("Q", 10);
        Channel warehouse = new Channel("W", 10);
        long speed = 100;

        Thread t = new Thread(new Baker(1, speed, queue, warehouse));
        t.start();

        queue.put(1);
        Thread.sleep(speed + 50);
        queue.close();
        t.join();

        assertTrue(warehouse.get(1).contains(1));
    }

    @Test
    void testMultipleOrders() throws Exception {
        Channel queue = new Channel("Q", 10);
        Channel warehouse = new Channel("W", 10);
        long speed = 50;

        Thread t = new Thread(new Baker(1, speed, queue, warehouse));
        t.start();

        queue.put(1);
        queue.put(2);
        Thread.sleep((speed * 2) + 100);
        queue.close();
        t.join();

        List<Integer> res = warehouse.get(2);
        assertTrue(res.contains(1) && res.contains(2));
    }
}