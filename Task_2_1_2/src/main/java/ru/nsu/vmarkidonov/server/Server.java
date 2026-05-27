package ru.nsu.vmarkidonov.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Координирующий TCP-сервер для распределенной проверки массивов чисел на наличие составных элементов.
 */
public class Server {
    private final int tcpPort;
    private final int chunkSize;
    private final int pingIntervalMs;

    private final TaskManager taskManager;
    private final ServerAnnouncer announcer;

    private final Set<WorkerHandler> activeHandlers = ConcurrentHashMap.newKeySet();
    private ServerSocket serverSocket;
    private Thread announcerThread;
    private volatile boolean isRunning = false;

    /**
     * Создает экземпляр сервера с заданными параметрами конфигурации и компонентами.
     *
     * @param tcpPort TCP-порт для приема входящих соединений от вычислительных узлов
     * @param taskManager менеджер распределения пакетов вычислений и отслеживания результатов
     * @param announcer служба широковещательного UDP-оповещения о присутствии сервера в сети
     * @param chunkSize максимальный размер блока чисел, отправляемого воркеру на проверку
     * @param pingIntervalMs периодичность проверки доступности подключенных воркеров в миллисекундах
     */
    public Server(
            int tcpPort,
            TaskManager taskManager,
            ServerAnnouncer announcer,
            int chunkSize,
            int pingIntervalMs
    ) {
        this.tcpPort = tcpPort;
        this.taskManager = taskManager;
        this.announcer = announcer;
        this.chunkSize = chunkSize;
        this.pingIntervalMs = pingIntervalMs;
    }

    /**
     * Инициализирует серверный сокет, запускает UDP-анонсы и активирует поток приема воркеров.
     *
     * @throws IOException если не удалось занять указанный TCP-порт или открыть сокет
     */
    public void start() throws IOException {
        isRunning = true;
        serverSocket = new ServerSocket(tcpPort);

        announcerThread = new Thread(announcer);
        announcerThread.start();

        new Thread(this::listenForWorkers).start();

        System.out.println("Служба TCP-сервера запущена на порту " + tcpPort);
    }

    /**
     * Принимает входящие TCP-соединения от воркеров в циклическом фоновом режиме.
     */
    private void listenForWorkers() {
        while (isRunning) {
            try {
                Socket workerSocket = serverSocket.accept();
                WorkerHandler handler = new WorkerHandler(
                        workerSocket,
                        taskManager,
                        pingIntervalMs
                );

                activeHandlers.add(handler);

                Thread handlerThread = new Thread(() -> {
                    try {
                        handler.run();
                    } finally {
                        activeHandlers.remove(handler);
                    }
                });
                handlerThread.start();
            } catch (IOException _) {
                if (!isRunning) {
                    break;
                }
            }
        }
    }

    /**
     * Разбивает входной массив данных на блоки и координирует их распределенную валидацию.
     *
     * @param input исходный массив чисел для выполнения проверки на простоту
     * @return true, если среди чисел обнаружено хотя бы одно составное, иначе false
     */
    public boolean hasComposite(long[] input) {
        int numChunks = (int) Math.ceil((double) input.length / chunkSize);
        long[][] chunks = new long[numChunks][];
        for (int i = 0; i < numChunks; i++) {
            int start = i * chunkSize;
            int length = Math.min(input.length - start, chunkSize);
            chunks[i] = new long[length];
            System.arraycopy(input, start, chunks[i], 0, length);
        }

        taskManager.submitBatch(chunks);

        try {
            taskManager.waitForCompletion();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return taskManager.isFoundComposite();
    }

    /**
     * Прекращает отправку UDP-анонсов, закрывает серверный сокет и останавливает все сессии воркеров.
     */
    public void stop() {
        isRunning = false;

        if (announcer != null) {
            announcer.stop();
        }
        if (announcerThread != null) {
            announcerThread.interrupt();
        }
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException ignored) {
            }
        }

        for (WorkerHandler handler : activeHandlers) {
            handler.stop();
        }
        activeHandlers.clear();
    }
}
