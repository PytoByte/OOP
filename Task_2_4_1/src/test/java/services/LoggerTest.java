package services;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.Test;

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