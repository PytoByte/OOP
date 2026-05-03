package Services;

import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

class LoggerTest {
    @Test
    void info() {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(bos));
        try {
            Logger logger = new Logger("test");
            logger.info("Hello %s", "world");
            String output = bos.toString().trim();
            assertTrue(output.contains("[test] Hello world"));
        } finally {
            System.setOut(originalOut);
        }
    }
}