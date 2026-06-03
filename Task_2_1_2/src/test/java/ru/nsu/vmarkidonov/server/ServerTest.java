package ru.nsu.vmarkidonov.server;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.ServerSocket;
import java.net.Socket;
import org.junit.jupiter.api.Test;
import ru.nsu.vmarkidonov.Protocol;

class ServerTest {

    private int getAssignedPort(Server server) throws Exception {
        Field field = Server.class.getDeclaredField("serverSocket");
        field.setAccessible(true);
        ServerSocket ss = (ServerSocket) field.get(server);
        return ss.getLocalPort();
    }

    @Test
    void testServerChunksAndProcessesCorrectly() throws Exception {
        TaskManager taskManager = new TaskManager();
        ServerAnnouncer announcer = new ServerAnnouncer(0, 14447, 50);
        Server server = new Server(0, taskManager, announcer, 2, 10000);

        server.start();
        int port = getAssignedPort(server);

        Thread workerSim = new Thread(() -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            try (Socket socket = new Socket("localhost", port);
                 DataInputStream in = new DataInputStream(socket.getInputStream());
                 DataOutputStream out = new DataOutputStream(socket.getOutputStream())
            ) {

                assertEquals(Protocol.TASK.ordinal(), in.readInt());
                int length1 = in.readInt();
                assertEquals(2, length1);

                long[] chunk1 = new long[length1];
                for (int i = 0; i < length1; i++) {
                    chunk1[i] = in.readLong();
                }
                assertArrayEquals(new long[]{3L, 5L}, chunk1);

                out.writeInt(Protocol.ACCEPT.ordinal());
                out.flush();
                out.writeInt(Protocol.RESULT.ordinal());
                out.writeBoolean(false);
                out.flush();

                assertEquals(Protocol.TASK.ordinal(), in.readInt());
                int length2 = in.readInt();
                assertEquals(1, length2);

                long[] chunk2 = new long[length2];
                for (int i = 0; i < length2; i++) {
                    chunk2[i] = in.readLong();
                }
                assertArrayEquals(new long[]{7L}, chunk2);

                out.writeInt(Protocol.ACCEPT.ordinal());
                out.flush();
                out.writeInt(Protocol.RESULT.ordinal());
                out.writeBoolean(false);
                out.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        workerSim.start();

        long[] input = {3L, 5L, 7L};
        boolean result = server.hasComposite(input);

        assertFalse(result);

        workerSim.join(2000);
        server.stop();
    }

    @Test
    void testServerShortCircuitsOnCompositeNumber() throws Exception {
        TaskManager taskManager = new TaskManager();
        ServerAnnouncer announcer = new ServerAnnouncer(0, 14448, 50);
        Server server = new Server(0, taskManager, announcer, 1, 10000);

        server.start();
        int port = getAssignedPort(server);

        Thread workerSim = new Thread(() -> {
            try {
                Thread.sleep(100);

                try (Socket socket = new Socket("localhost", port);
                     DataInputStream in = new DataInputStream(socket.getInputStream());
                     DataOutputStream out = new DataOutputStream(socket.getOutputStream())) {

                    assertEquals(Protocol.TASK.ordinal(), in.readInt());

                    out.writeInt(Protocol.ACCEPT.ordinal());
                    out.flush();

                    out.writeInt(Protocol.RESULT.ordinal());
                    out.writeBoolean(true);
                    out.flush();
                }
            } catch (Exception e) {
                fail(e);
            }
        });
        workerSim.start();

        long[] input = {4L, 5L, 7L};
        boolean result = server.hasComposite(input);

        assertTrue(result);

        workerSim.join(2000);
        server.stop();
    }
}
