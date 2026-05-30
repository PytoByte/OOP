package ru.nsu.vmarkidonov;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import org.junit.jupiter.api.Test;
import ru.nsu.vmarkidonov.server.Server;
import ru.nsu.vmarkidonov.worker.Worker;

class MainTest {

    @Test
    void testStartServerInitialization() throws IOException {
        Server server = Main.startServer();
        server.stop();
    }

    @Test
    void testStartWorkerInitialization() {
        Worker worker = Main.startWorker();
        worker.stop();
    }

    @Test
    void testRunTestsDoesNotThrowException() throws IOException {
        Server server = Main.startServer();
        Worker worker = Main.startWorker();
        assertTrue(Main.runTests(server));
        worker.stop();
        server.stop();
    }

    @Test
    void testMainWithInvalidArgs() throws Exception {
        Main.main(new String[]{"invalid"});
    }
}