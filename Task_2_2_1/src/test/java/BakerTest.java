import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import java.util.List;

class BakerTest {

    @Test
    void run() throws InterruptedException {
        Channel queue = new Channel("TEST_QUEUE", 0);
        Channel warehouse = new Channel("TEST_WAREHOUSE", 0);
        Baker baker = new Baker(0, 1, queue, warehouse);

        List<Integer> queue_result = queue.get(2);
        List<Integer> warehouse_result = warehouse.get(2);

        queue.put(0);
        queue.put(1);

        queue.close();

        baker.run();

        warehouse.close();

        assertTrue(queue_result.isEmpty());
        assertTrue(warehouse_result.contains(0));
        assertTrue(warehouse_result.contains(1));
    }
}