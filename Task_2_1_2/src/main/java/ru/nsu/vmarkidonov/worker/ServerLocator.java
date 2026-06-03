package ru.nsu.vmarkidonov.worker;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

/**
 * Класс для обнаружения активного сервера в локальной сети по протоколу UDP.
 */
public class ServerLocator {
    private static final int RX_BUFFER_SIZE = 512;
    private static final String EXPECTED_PREFIX = "SERVER AT ";
    private final int udpPort;
    private final int timeoutMs;

    /**
     * Создает локатор сервера с указанными сетевыми параметрами.
     *
     * @param udpPort UDP-порт для прослушивания широковещательных пакетов анонса
     * @param timeoutMs максимальное время ожидания ответа от сервера в миллисекундах
     */
    public ServerLocator(int udpPort, int timeoutMs) {
        this.udpPort = udpPort;
        this.timeoutMs = timeoutMs;
    }

    /**
     * Ожидает UDP-пакет от сервера, парсит его данные и возвращает параметры подключения.
     *
     * @return объект с IP-адресом и TCP-портом найденного сервера
     * @throws IOException если превышен таймаут, сокет закрыт или формат сообщения некорректен
     */
    public ServerDetails locate() throws IOException {
        try (DatagramSocket socket = new DatagramSocket(null)) {
            socket.setReuseAddress(true);
            socket.bind(new InetSocketAddress(udpPort));
            socket.setSoTimeout(timeoutMs);

            byte[] rxBuffer = new byte[RX_BUFFER_SIZE];
            DatagramPacket packet = new DatagramPacket(rxBuffer, rxBuffer.length);

            socket.receive(packet);
            String rawMessage = new String(packet.getData(), 0, packet.getLength()).trim();

            if (!rawMessage.startsWith(EXPECTED_PREFIX)) {
                throw new IOException("Неверный формат сообщения от сервера: " + rawMessage);
            }

            String serverIp = packet.getAddress().getHostAddress();
            String portPart = rawMessage.substring(EXPECTED_PREFIX.length()).trim();
            int serverTcpPort = Integer.parseInt(portPart);

            System.out.println(
                    "Сервер успешно локализован по адресу " + serverIp + ":" + serverTcpPort
            );
            return new ServerDetails(serverIp, serverTcpPort);
        }
    }
}
