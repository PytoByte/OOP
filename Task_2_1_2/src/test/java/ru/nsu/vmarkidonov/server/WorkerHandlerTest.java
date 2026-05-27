package ru.nsu.vmarkidonov.server;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.Test;
import ru.nsu.vmarkidonov.Protocol;

class WorkerHandlerTest {

    @Test
    void testWorkerHandlerTaskFlow() throws Exception {
        TaskManager taskManager = new TaskManager();
        long[][] chunks = {{42L, 43L}};
        taskManager.submitBatch(chunks);

        AtomicReference<Throwable> threadException = new AtomicReference<>();

        try (ServerSocket serverSocket = new ServerSocket(0)) {
            int port = serverSocket.getLocalPort();

            Thread clientThread = new Thread(() -> {
                try {
                    Thread.sleep(50); // Даем серверу время вызвать accept()
                    try (Socket clientSocket = new Socket("localhost", port);
                         DataInputStream in = new DataInputStream(clientSocket.getInputStream());
                         DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream())) {

                        if (in.readInt() == Protocol.TASK.ordinal()) {
                            int length = in.readInt();
                            long[] receivedData = new long[length];
                            for (int i = 0; i < length; i++) {
                                receivedData[i] = in.readLong();
                            }
                            assertArrayEquals(new long[]{42L, 43L}, receivedData);

                            out.writeInt(Protocol.ACCEPT.ordinal());
                            out.flush();

                            Thread.sleep(100);

                            out.writeInt(Protocol.RESULT.ordinal());
                            out.writeBoolean(true);
                            out.flush();
                        }
                    }
                } catch (Throwable t) {
                    threadException.set(t);
                }
            });
            clientThread.start();

            try (Socket acceptedSocket = serverSocket.accept()) {
                WorkerHandler handler = new WorkerHandler(acceptedSocket, taskManager, 10000);
                Thread handlerThread = new Thread(handler);
                handlerThread.start();

                clientThread.join(2000);

                if (threadException.get() != null) {
                    fail("Ошибка в фоновом потоке воркера", threadException.get());
                }

                long start = System.currentTimeMillis();
                while (taskManager.hasActiveBatch() && (System.currentTimeMillis() - start < 2000)) {
                    Thread.sleep(50);
                }

                assertFalse(taskManager.hasActiveBatch());
                assertTrue(taskManager.isFoundComposite());

                handler.stop();
                handlerThread.join(1000);
            }
        }
    }

    @Test
    void testWorkerHandlerPingStatus() throws Exception {
        TaskManager taskManager = new TaskManager();
        AtomicReference<Throwable> threadException = new AtomicReference<>();

        try (ServerSocket serverSocket = new ServerSocket(0)) {
            int port = serverSocket.getLocalPort();

            Thread clientThread = new Thread(() -> {
                try {
                    Thread.sleep(50);
                    try (Socket clientSocket = new Socket("localhost", port);
                         DataInputStream in = new DataInputStream(clientSocket.getInputStream());
                         DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream())) {

                        if (in.readInt() == Protocol.STATUS.ordinal()) {
                            out.writeInt(Protocol.OK.ordinal());
                            out.flush();
                        }
                    }
                } catch (Throwable t) {
                    threadException.set(t);
                }
            });
            clientThread.start();

            try (Socket acceptedSocket = serverSocket.accept()) {
                WorkerHandler handler = new WorkerHandler(acceptedSocket, taskManager, 10);
                Thread handlerThread = new Thread(handler);
                handlerThread.start();

                clientThread.join(2000);

                if (threadException.get() != null) {
                    fail("Ошибка в фоновом потоке воркера", threadException.get());
                }

                handler.stop();
                handlerThread.join(1000);
            }
        }
    }

    @Test
    void testWorkerHandlerTaskInterruptionOnStopSignal() throws Exception {
        TaskManager taskManager = new TaskManager();
        long[][] chunks = {{100L, 200L}};
        taskManager.submitBatch(chunks);

        AtomicReference<Throwable> threadException = new AtomicReference<>();

        try (ServerSocket serverSocket = new ServerSocket(0)) {
            int port = serverSocket.getLocalPort();

            Thread clientThread = new Thread(() -> {
                try {
                    Thread.sleep(50);
                    try (Socket clientSocket = new Socket("localhost", port);
                         DataInputStream in = new DataInputStream(clientSocket.getInputStream());
                         DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream())) {

                        if (in.readInt() == Protocol.TASK.ordinal()) {
                            int length = in.readInt();
                            for (int i = 0; i < length; i++) {
                                in.readLong();
                            }
                            out.writeInt(Protocol.ACCEPT.ordinal());
                            out.flush();

                            assertEquals(Protocol.STOP.ordinal(), in.readInt());
                        }
                    }
                } catch (Throwable t) {
                    threadException.set(t);
                }
            });
            clientThread.start();

            try (Socket acceptedSocket = serverSocket.accept()) {
                WorkerHandler handler = new WorkerHandler(acceptedSocket, taskManager, 10000);
                Thread handlerThread = new Thread(handler);
                handlerThread.start();

                long start = System.currentTimeMillis();
                while (!taskManager.getNextTask().equals(chunks[0]) && (System.currentTimeMillis() - start < 1000)) {
                    Thread.sleep(10);
                }

                taskManager.taskFinished(true);

                clientThread.join(2000);

                if (threadException.get() != null) {
                    fail("Ошибка в фоновом потоке воркера", threadException.get());
                }

                handler.stop();
                handlerThread.join(1000);
            }
        }
    }
}
