package ru.nsu.vmarkidonov.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Служба широковещательного UDP-оповещения воркеров о присутствии и сетевых параметрах сервера.
 */
public class ServerAnnouncer implements Runnable {
    private static final String BROADCAST_IP = "255.255.255.255";
    private final int tcpPort;
    private final int udpPort;
    private final int intervalMs;
    private volatile boolean active = false;

    /**
     * Создает анонсер сервера с заданными сетевыми портами и интервалом отправки маяков.
     *
     * @param tcpPort TCP-порт сервера, сообщаемый воркерам для последующего подключения
     * @param udpPort UDP-порт, на который отправляются широковещательные пакеты
     * @param intervalMs интервал между отправками пакетов оповещения в миллисекундах
     */
    public ServerAnnouncer(int tcpPort, int udpPort, int intervalMs) {
        this.tcpPort = tcpPort;
        this.udpPort = udpPort;
        this.intervalMs = intervalMs;
    }

    /**
     * Запускает циклический процесс рассылки широковещательных UDP-пакетов в сеть.
     */
    @Override
    public void run() {
        active = true;
        try (DatagramSocket socket = new DatagramSocket()) {
            socket.setBroadcast(true);
            InetAddress targetAddress = InetAddress.getByName(BROADCAST_IP);

            String beaconMsg = "SERVER AT " + tcpPort;
            byte[] txBuffer = beaconMsg.getBytes();

            while (active && !Thread.currentThread().isInterrupted()) {
                DatagramPacket packet = new DatagramPacket(
                        txBuffer,
                        txBuffer.length,
                        targetAddress, udpPort
                );
                socket.send(packet);
                Thread.sleep(intervalMs);
            }
        } catch (IOException e) {
            if (active) {
                System.err.println("Ошибка вещания сервера: " + e.getMessage());
            }
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        } finally {
            active = false;
        }
    }

    /**
     * Останавливает процесс рассылки пакетов оповещения.
     */
    public void stop() {
        active = false;
    }
}
