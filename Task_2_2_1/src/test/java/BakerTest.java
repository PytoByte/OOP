import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import java.util.List;

class BakerTest {

    @Test
    void run() throws InterruptedException {
        Channel queue = new Channel("TEST_QUEUE", 0);
        Channel warehouse = new Channel("TEST_WAREHOUSE", 0);
        Baker baker = new Baker(0, 0, queue, warehouse);
        Thread bakerThread = new Thread(baker);

        bakerThread.start();

        queue.put(0);
        queue.put(1);

        queue.close();
        warehouse.close();

        List<Integer> queue_result = queue.get(2);
        List<Integer> warehouse_result = warehouse.get(2);

        assertTrue(queue_result.isEmpty());
        assertTrue(warehouse_result.contains(0));
        assertTrue(warehouse_result.contains(1));

        bakerThread.join(1000);
        assertFalse(bakerThread.isAlive());
    }
}