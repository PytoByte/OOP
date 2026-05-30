package ru.nsu.vmarkidonov.server;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;

class TaskManagerTest {

    @Test
    void testSubmitAndGetTasks() {
        TaskManager manager = new TaskManager();
        assertFalse(manager.hasActiveBatch());

        long[][] chunks = {{1L, 2L}, {3L, 4L}};
        manager.submitBatch(chunks);

        assertTrue(manager.hasActiveBatch());
        assertFalse(manager.isFoundComposite());

        assertArrayEquals(new long[]{1L, 2L}, manager.getNextTask());
        assertArrayEquals(new long[]{3L, 4L}, manager.getNextTask());
        assertNull(manager.getNextTask());
    }

    @Test
    void testReturnTask() {
        TaskManager manager = new TaskManager();
        long[][] chunks = {{5L, 6L}};
        manager.submitBatch(chunks);

        long[] task = manager.getNextTask();
        assertNotNull(task);

        manager.returnTask(task);
        assertArrayEquals(new long[]{5L, 6L}, manager.getNextTask());
    }

    @Test
    void testTaskFinishedWithoutComposite() throws InterruptedException {
        TaskManager manager = new TaskManager();
        long[][] chunks = {{1L}, {2L}};
        manager.submitBatch(chunks);

        manager.getNextTask();
        manager.getNextTask();

        manager.taskFinished(false);
        assertTrue(manager.hasActiveBatch());

        manager.taskFinished(false);
        assertFalse(manager.hasActiveBatch());
        assertFalse(manager.isFoundComposite());

        manager.waitForCompletion();
    }

    @Test
    void testTaskFinishedWithComposite() throws InterruptedException {
        TaskManager manager = new TaskManager();
        long[][] chunks = {{1L}, {2L}, {3L}};
        manager.submitBatch(chunks);

        long[] t1 = manager.getNextTask();
        assertNotNull(t1);

        manager.taskFinished(true);
        assertFalse(manager.hasActiveBatch());
        assertTrue(manager.isFoundComposite());

        manager.waitForCompletion();
    }

    @Test
    void testWaitForCompletionBlocking() throws InterruptedException {
        TaskManager manager = new TaskManager();
        long[][] chunks = {{10L}};
        manager.submitBatch(chunks);

        long[] task = manager.getNextTask();
        assertNotNull(task);

        CountDownLatch latch = new CountDownLatch(1);
        Thread waiterThread = new Thread(() -> {
            try {
                manager.waitForCompletion();
                latch.countDown();
            } catch (InterruptedException ignored) {
            }
        });
        waiterThread.start();

        assertFalse(latch.await(100, TimeUnit.MILLISECONDS));

        manager.taskFinished(false);
        assertTrue(latch.await(1000, TimeUnit.MILLISECONDS));
        waiterThread.join();
    }
}
