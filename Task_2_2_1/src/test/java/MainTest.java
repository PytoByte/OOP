import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class MainTest {

    @TempDir
    Path tempDir;

    private String createTestConfig() throws IOException {
        Path config = tempDir.resolve("test-config.json");
        try (FileWriter fw = new FileWriter(config.toFile())) {
            fw.write("""
            {
              "bakers": 1,
              "couriers": 1,
              "warehouseCapacity": 5,
              "bakersSpeedMillis": [30],
              "couriersCapacity": [2],
              "couriersSpeedMillis": [30],
              "orderDelayMillis": 20,
              "workDurationSec": 1
            }
            """);
        }
        return config.toString();
    }

    @Test
    void main_withValidConfig_doesNotThrow() throws Exception {
        String configPath = createTestConfig();

        Thread t = new Thread(() -> {
            try {
                Main.main(new String[]{configPath});
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        t.start();
        t.join(5000);

        assertFalse(t.isAlive(), "Main should complete within timeout");
    }

    @Test
    void main_withMissingConfig_throwsFileNotFoundException() {
        assertThrows(
                java.io.FileNotFoundException.class,
                () -> Main.main(new String[]{"nonexistent.json"})
        );
    }
}