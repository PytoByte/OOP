package ru.nsu.vmarkidonov.worker;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import org.junit.jupiter.api.Test;

class ServerLocatorTest {

    private static final int TEST_PORT = 14445;

    @Test
    void testLocateSuccess() throws Exception {
        ServerLocator locator = new ServerLocator(TEST_PORT, 2000);

        Thread senderThread = new Thread(() -> {
            try (DatagramSocket socket = new DatagramSocket()) {
                byte[] data = "SERVER AT 8080".getBytes();
                DatagramPacket packet = new DatagramPacket(
                        data,
                        data.length,
                        InetAddress.getLoopbackAddress(),
                        TEST_PORT
                );
                Thread.sleep(100);
                socket.send(packet);
            } catch (Exception e) {
                fail(e);
            }
        });
        senderThread.start();

        ServerDetails details = locator.locate();
        assertNotNull(details);
        assertEquals(8080, details.port());
        assertEquals(InetAddress.getLoopbackAddress().getHostAddress(), details.ipAddress());

        senderThread.join();
    }

    @Test
    void testLocateInvalidPrefix() {
        ServerLocator locator = new ServerLocator(TEST_PORT + 1, 1000);

        Thread senderThread = new Thread(() -> {
            try (DatagramSocket socket = new DatagramSocket()) {
                byte[] data = "INVALID PREFIX 8080".getBytes();
                DatagramPacket packet = new DatagramPacket(
                        data,
                        data.length,
                        InetAddress.getLoopbackAddress(),
                        TEST_PORT + 1
                );
                Thread.sleep(100);
                socket.send(packet);
            } catch (Exception e) {
                fail(e);
            }
        });
        senderThread.start();

        assertThrows(IOException.class, locator::locate);

        try {
            senderThread.join();
        } catch (InterruptedException e) {
            fail(e);
        }
    }

    @Test
    void testLocateInvalidPortFormat() {
        ServerLocator locator = new ServerLocator(TEST_PORT + 2, 1000);

        Thread senderThread = new Thread(() -> {
            try (DatagramSocket socket = new DatagramSocket()) {
                byte[] data = "SERVER AT abc".getBytes();
                DatagramPacket packet = new DatagramPacket(
                        data,
                        data.length,
                        InetAddress.getLoopbackAddress(),
                        TEST_PORT + 2
                );
                Thread.sleep(100);
                socket.send(packet);
            } catch (Exception e) {
                fail(e);
            }
        });
        senderThread.start();

        assertThrows(NumberFormatException.class, locator::locate);

        try {
            senderThread.join();
        } catch (InterruptedException e) {
            fail(e);
        }
    }

    @Test
    void testLocateTimeout() {
        ServerLocator locator = new ServerLocator(TEST_PORT + 3, 200);
        assertThrows(SocketTimeoutException.class, locator::locate);
    }
}
