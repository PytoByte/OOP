package ru.nsu.vmarkidonov.worker;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;
import ru.nsu.vmarkidonov.Protocol;

/**
 * Вычислительный узел, выполняющий поиск составных чисел в полученных от сервера пакетах.
 */
public class Worker implements Runnable {
    private final ServerLocator locator;
    private volatile boolean isWorking = false;
    private volatile Socket currentSocket;

    /**
     * Создает воркер с указанным локатором сервера.
     *
     * @param locator локатор для автоматического обнаружения сервера в сети
     */
    public Worker(ServerLocator locator) {
        this.locator = locator;
    }

    /**
     * Запускает основной цикл обнаружения сервера и обработки сетевых задач.
     */
    @Override
    public void run() {
        isWorking = true;

        while (isWorking) {
            ServerDetails target = locateServer();
            if (target == null) {
                continue;
            }

            connectAndProcess(target);
        }
        System.out.println("Воркер успешно завершен.");
    }

    /**
     * Выполняет поиск параметров подключения к серверу через локатор.
     *
     * @return параметры подключения к найденному серверу или null при ошибке обнаружения
     */
    private ServerDetails locateServer() {
        try {
            System.out.println("Сканирование сети на наличие сервера...");
            return locator.locate();
        } catch (Exception e) {
            if (isWorking) {
                System.err.println("Сервер не отозвался. Повтор через 2.5 секунды...");
                try {
                    Thread.sleep(2500);
                } catch (InterruptedException ignored) {
                    Thread.currentThread().interrupt();
                }
            }
            return null;
        }
    }

    /**
     * Устанавливает TCP-соединение с сервером и передает управление обработчику сообщений.
     *
     * @param target параметры удаленного сервера для подключения
     */
    private void connectAndProcess(ServerDetails target) {
        try (Socket socket = new Socket(target.ipAddress(), target.port());
             DataInputStream in = new DataInputStream(socket.getInputStream());
             DataOutputStream out = new DataOutputStream(socket.getOutputStream())) {

            currentSocket = socket;
            System.out.println("Выполнено подключение к узлу: " + target.ipAddress());
            processMessages(in, out, socket);

        } catch (Exception e) {
            if (isWorking) {
                System.err.println("Соединение с сервером разорвано: " + e.getMessage());
            }
        } finally {
            currentSocket = null;
        }
    }

    /**
     * Читает и обрабатывает входящие команды из сетевого потока данных сокета.
     *
     * @param in входной поток данных сокета
     * @param out выходной поток данных сокета
     * @param socket активный сокет соединения с сервером
     * @throws IOException при ошибках чтения или записи в сетевые потоки
     */
    private void processMessages(
            DataInputStream in,
            DataOutputStream out,
            Socket socket
    ) throws IOException {
        AtomicBoolean interruptFlag = new AtomicBoolean(false);

        while (isWorking && !socket.isClosed()) {
            int code = in.readInt();
            Protocol flag = Protocol.fromOrdinal(code);

            switch (flag) {
                case STATUS -> sendResponse(out, Protocol.OK);
                case STOP -> interruptFlag.set(true);
                case TASK -> handleTask(in, out, interruptFlag);
                default -> {
                    System.err.println("Неожиданный ответ сервера: " + flag.name());
                    stop();
                }
            }
        }
    }

    /**
     * Принимает вычислительную задачу от сервера и запускает её в отдельном потоке.
     *
     * @param in входной поток данных сокета
     * @param out выходной поток данных сокета
     * @param interruptFlag флаг для возможности экстренного прерывания вычислений
     * @throws IOException при ошибках чтения параметров или массива чисел из потока
     */
    private void handleTask(
            DataInputStream in,
            DataOutputStream out,
            AtomicBoolean interruptFlag
    ) throws IOException {
        int length = in.readInt();
        long[] data = new long[length];
        for (int i = 0; i < length; i++) {
            data[i] = in.readLong();
        }

        sendResponse(out, Protocol.ACCEPT);
        interruptFlag.set(false);

        new Thread(() -> runCalculations(data, out, interruptFlag)).start();
    }

    /**
     * Выполняет последовательную проверку массива чисел на простоту в фоновом потоке.
     *
     * @param data массив чисел для проверки
     * @param out выходной поток сокета для отправки результата вычислений
     * @param interruptFlag флаг для отслеживания сигнала отмены задачи
     */
    private void runCalculations(
            long[] data,
            DataOutputStream out,
            AtomicBoolean interruptFlag
    ) {
        boolean hasComposite = false;
        for (long num : data) {
            if (interruptFlag.get()) {
                return;
            }
            if (!PrimeChecker.isPrime(num)) {
                hasComposite = true;
                break;
            }
        }
        sendResult(out, hasComposite, interruptFlag);
    }

    /**
     * Отправляет синхронизированный ответ со статусом протокола в выходной поток.
     *
     * @param out выходной поток данных сокета
     * @param status отправляемый статус протокола
     * @throws IOException при ошибках записи данных в сокет
     */
    private void sendResponse(
            DataOutputStream out,
            Protocol status
    ) throws IOException {
        synchronized (out) {
            out.writeInt(status.ordinal());
            out.flush();
        }
    }

    /**
     * Синхронизированно отправляет финальный результат вычислений на сервер.
     *
     * @param out выходной поток данных сокета
     * @param hasComposite признак обнаружения хотя бы одного составного числа
     * @param interruptFlag флаг проверки актуальности отправки данных
     */
    private void sendResult(
            DataOutputStream out,
            boolean hasComposite,
            AtomicBoolean interruptFlag
    ) {
        if (interruptFlag.get()) {
            return;
        }
        try {
            synchronized (out) {
                out.writeInt(Protocol.RESULT.ordinal());
                out.writeBoolean(hasComposite);
                out.flush();
            }
        } catch (Exception e) {
            System.err.println("Ошибка IO при ответе серверу на задачу " + e);
            stop();
        }
    }

    /**
     * Закрывает активное сетевое соединение и останавливает рабочий цикл воркера.
     */
    public void stop() {
        isWorking = false;

        if (currentSocket != null) {
            try {
                currentSocket.close();
            } catch (IOException ignored) {
                System.out.println("Игнорирование ошибки закрытия серверного сокета");
            }
        }
    }
}
