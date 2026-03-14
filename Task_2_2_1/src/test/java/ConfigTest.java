import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class ConfigTest {
    Config initConfig() {
        return new Config(
                0,
                1,
                2,
                3,
                4,
                new long[] {5, 6},
                new int[] {7, 8, 9},
                new long[] {10, 11}
        );
    }

    @Test
    void workDurationSec() {
        Config cfg = initConfig();

        assertEquals(0, cfg.workDurationSec());
    }

    @Test
    void orderDelayMillis() {
        Config cfg = initConfig();

        assertEquals(1, cfg.orderDelayMillis());
    }

    @Test
    void bakers() {
        Config cfg = initConfig();

        assertEquals(2, cfg.bakers());
    }

    @Test
    void couriers() {
        Config cfg = initConfig();

        assertEquals(3, cfg.couriers());
    }

    @Test
    void warehouseCapacity() {
        Config cfg = initConfig();

        assertEquals(4, cfg.warehouseCapacity());
    }

    @Test
    void bakersSpeedMillis() {
        Config cfg = initConfig();

        assertArrayEquals(new long[] {5, 6}, cfg.bakersSpeedMillis());
    }

    @Test
    void couriersCapacity() {
        Config cfg = initConfig();

        assertArrayEquals(new int[] {7, 8, 9}, cfg.couriersCapacity());
    }

    @Test
    void couriersSpeedMillis() {
        Config cfg = initConfig();

        assertArrayEquals(new long[] {10, 11}, cfg.couriersSpeedMillis());
    }
}