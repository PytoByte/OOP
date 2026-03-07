import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;

class BakerTest {

    @Test
    void run() throws InterruptedException {
        Channel queue = new Channel("TEST_QUEUE", 0);
        Channel warehouse = new Channel("TEST_WAREHOUSE", 0);
        Baker baker = new Baker(0, 1, queue, warehouse);

        List<Integer> queueResult = queue.get(2);
        List<Integer> warehouseResult = warehouse.get(2);

        queue.put(0);
        queue.put(1);

        queue.close();

        baker.run();

        warehouse.close();

        assertTrue(queueResult.isEmpty());
        assertTrue(warehouseResult.contains(0));
        assertTrue(warehouseResult.contains(1));
    }
}