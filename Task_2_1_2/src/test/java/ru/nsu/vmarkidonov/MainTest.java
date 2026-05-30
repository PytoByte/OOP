package ru.nsu.vmarkidonov;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;
import ru.nsu.vmarkidonov.server.Server;

class MainTest {
    @Test
    void testRunTestsLogic() throws Exception {
        Server server = Main.startServer();
        try {
            assertTrue(Main.runTests(server));
        } finally {
            server.stop();
        }
    }

    @Test
    void testMainInvalidArgs() throws Exception {
        Main.main(new String[]{"wrong_mode"});
    }

    @Test
    void testMainModesRunSuccessfully() throws Exception {
        String[] modes = {"server", "worker"};
        for (String mode : modes) {
            Thread t = new Thread(() -> {
                try {
                    Main.main(new String[]{mode});
                } catch (Exception e) {
                    fail(e);
                }
            });
            t.start();
            Thread.sleep(800);
            t.interrupt();
            t.join(1000);
        }
    }

    @Test
    void testMainTestModeFullCycle() throws Exception {
        Thread testThread = new Thread(() -> {
            try {
                Main.main(new String[]{"test"});
            } catch (Exception e) {
                fail(e);
            }
        });
        testThread.start();
        Thread.sleep(4500);
        testThread.interrupt();
        testThread.join(1000);
    }
}
