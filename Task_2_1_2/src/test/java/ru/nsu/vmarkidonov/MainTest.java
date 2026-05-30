package ru.nsu.vmarkidonov;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

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

    @Test
    void testMainModes() throws Exception {
        Thread serverThread = new Thread(() -> {
            try {
                Main.main(new String[]{"server"});
            } catch (Exception e) {
                fail(e);
            }
        });
        serverThread.start();
        Thread.sleep(500);
        serverThread.interrupt();

        Thread workerThread = new Thread(() -> {
            try {
                Main.main(new String[]{"worker"});
            } catch (Exception e) {
                fail(e);
            }
        });
        workerThread.start();
        Thread.sleep(500);
        workerThread.interrupt();
    }
}