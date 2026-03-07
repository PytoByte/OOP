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
                new long[] {7, 8, 9}
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

        assertEquals(1, cfg.orderDelayMillis());
    }

    @Test
    void couriers() {
    }

    @Test
    void warehouseCapacity() {
    }

    @Test
    void bakersSpeed() {
    }

    @Test
    void couriersCapacity() {
    }
}