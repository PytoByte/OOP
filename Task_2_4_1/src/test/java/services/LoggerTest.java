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
            assertTrue(output.contains("(INFO) [test] Hello world"));
        } finally {
            System.setOut(originalOut);
        }
    }

    @Test
    void error() {
        PrintStream originalErr = System.err;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        System.setErr(new PrintStream(bos));
        try {
            Logger logger = new Logger("test");
            logger.error("Hello %s", "world");
            String output = bos.toString().trim();
            assertTrue(output.contains("(ERROR) [test] Hello world"));
        } finally {
            System.setErr(originalErr);
        }
    }
}