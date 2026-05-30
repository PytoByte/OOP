package ru.nsu.vmarkidonov.worker;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import org.junit.jupiter.api.Test;
import ru.nsu.vmarkidonov.Protocol;

class WorkerTest {

    @Test
    void testWorkerProtocolExchange() throws Exception {
        int udpPort = 15555;
        try (ServerSocket serverSocket = new ServerSocket(0)) {
            int tcpPort = serverSocket.getLocalPort();

            ServerLocator locator = new ServerLocator(udpPort, 2000);
            Worker worker = new Worker(locator);

            Thread workerThread = new Thread(worker);
            workerThread.start();

            Thread broadcaster = new Thread(() -> {
                try (DatagramSocket ds = new DatagramSocket()) {
                    byte[] msg = ("SERVER AT " + tcpPort).getBytes();
                    DatagramPacket packet = new DatagramPacket(
                            msg,
                            msg.length,
                            InetAddress.getLoopbackAddress(),
                            udpPort
                    );
                    Thread.sleep(200);
                    ds.send(packet);
                } catch (Exception e) {
                    fail(e);
                }
            });
            broadcaster.start();

            try (Socket socket = serverSocket.accept();
                 DataInputStream in = new DataInputStream(socket.getInputStream());
                 DataOutputStream out = new DataOutputStream(socket.getOutputStream())) {

                out.writeInt(Protocol.STATUS.ordinal());
                out.flush();
                assertEquals(Protocol.OK.ordinal(), in.readInt());

                out.writeInt(Protocol.TASK.ordinal());
                out.writeInt(3);
                out.writeLong(3L);
                out.writeLong(5L);
                out.writeLong(7L);
                out.flush();

                assertEquals(Protocol.ACCEPT.ordinal(), in.readInt());
                assertEquals(Protocol.RESULT.ordinal(), in.readInt());
                assertFalse(in.readBoolean());

                out.writeInt(Protocol.TASK.ordinal());
                out.writeInt(2);
                out.writeLong(11L);
                out.writeLong(4L);
                out.flush();

                assertEquals(Protocol.ACCEPT.ordinal(), in.readInt());
                assertEquals(Protocol.RESULT.ordinal(), in.readInt());
                assertTrue(in.readBoolean());
            } finally {
                worker.stop();
                workerThread.join(2000);
                broadcaster.join(2000);
            }
        }
    }

    @Test
    void testWorkerTaskInterruption() throws Exception {
        int udpPort = 15556;
        try (ServerSocket serverSocket = new ServerSocket(0)) {
            int tcpPort = serverSocket.getLocalPort();

            ServerLocator locator = new ServerLocator(udpPort, 2000);
            Worker worker = new Worker(locator);

            Thread workerThread = new Thread(worker);
            workerThread.start();

            Thread broadcaster = new Thread(() -> {
                try (DatagramSocket ds = new DatagramSocket()) {
                    byte[] msg = ("SERVER AT " + tcpPort).getBytes();
                    DatagramPacket packet = new DatagramPacket(
                            msg,
                            msg.length,
                            InetAddress.getLoopbackAddress(),
                            udpPort
                    );
                    Thread.sleep(200);
                    ds.send(packet);
                } catch (Exception e) {
                    fail(e);
                }
            });
            broadcaster.start();

            try (Socket socket = serverSocket.accept();
                 DataInputStream in = new DataInputStream(socket.getInputStream());
                 DataOutputStream out = new DataOutputStream(socket.getOutputStream())) {

                out.writeInt(Protocol.TASK.ordinal());
                out.writeInt(1);
                out.writeLong(999999999999999989L);
                out.flush();

                assertEquals(Protocol.ACCEPT.ordinal(), in.readInt());

                out.writeInt(Protocol.STOP.ordinal());
                out.flush();

                Thread.sleep(300);
                assertEquals(0, socket.getInputStream().available());
            } finally {
                worker.stop();
                workerThread.join(2000);
                broadcaster.join(2000);
            }
        }
    }
}
