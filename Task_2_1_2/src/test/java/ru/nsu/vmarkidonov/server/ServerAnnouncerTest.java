package ru.nsu.vmarkidonov.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import org.junit.jupiter.api.Test;

class ServerAnnouncerTest {

    private static final int TEST_UDP_PORT = 14446;
    private static final int TEST_TCP_PORT = 8085;

    @Test
    void testAnnouncerSendsCorrectBeacon() throws Exception {
        try (DatagramSocket receiverSocket = new DatagramSocket(null)) {
            receiverSocket.setReuseAddress(true);
            receiverSocket.bind(new InetSocketAddress(TEST_UDP_PORT));
            receiverSocket.setSoTimeout(2000);

            ServerAnnouncer announcer = new ServerAnnouncer(TEST_TCP_PORT, TEST_UDP_PORT, 50);
            Thread announcerThread = new Thread(announcer);
            announcerThread.start();

            byte[] buffer = new byte[512];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

            try {
                receiverSocket.receive(packet);
                String message = new String(packet.getData(), 0, packet.getLength()).trim();
                assertEquals("SERVER AT " + TEST_TCP_PORT, message);
            } finally {
                announcer.stop();
                announcerThread.join(1000);
            }
        }
    }

    @Test
    void testAnnouncerStopGracefully() throws Exception {
        ServerAnnouncer announcer = new ServerAnnouncer(TEST_TCP_PORT, TEST_UDP_PORT + 1, 50);
        Thread announcerThread = new Thread(announcer);
        announcerThread.start();

        Thread.sleep(100);
        assertTrue(announcerThread.isAlive());

        announcer.stop();
        announcerThread.join(1000);
        assertFalse(announcerThread.isAlive());
    }
}
