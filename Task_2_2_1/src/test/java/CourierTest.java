import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import java.util.List;

class CourierTest {

    @Test
    void run() throws InterruptedException {
        Channel warehouse = new Channel("TEST_WAREHOUSE", 0);
        Courier courier = new Courier(0, 2, 0, warehouse);
        Thread courierThread = new Thread(courier);

        courierThread.start();

        warehouse.put(0);
        warehouse.put(1);

        warehouse.close();

        List<Integer> warehouse_result = warehouse.get(2);

        courierThread.join(1000);
        assertFalse(courierThread.isAlive());
        assertTrue(warehouse_result.isEmpty());
    }
}