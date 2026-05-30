package ru.nsu.vmarkidonov;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import ru.nsu.vmarkidonov.server.Server;
import ru.nsu.vmarkidonov.server.ServerAnnouncer;
import ru.nsu.vmarkidonov.server.TaskManager;
import ru.nsu.vmarkidonov.worker.ServerLocator;
import ru.nsu.vmarkidonov.worker.Worker;

/**
 * Главный класс для управления жизненным циклом сервера, воркеров и запуска тестов.
 */
public class Main {

    /**
     * Точка входа в приложение для разбора аргументов и запуска системы в заданном режиме.
     *
     * @param args аргументы командной строки, определяющие режим ("server", "worker" или "test")
     * @throws Exception при ошибках ввода-вывода или прерывания потоков во время ожидания
     */
    public static void main(String[] args) throws Exception {
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
        System.setErr(new PrintStream(System.err, true, StandardCharsets.UTF_8));

        if (args.length > 0 && args[0].equalsIgnoreCase("server")) {
            Server server = startServer();
            runTests(server);
            server.stop();
        } else if (args.length > 0 && args[0].equalsIgnoreCase("worker")) {
            startWorker();
        } else if (args.length > 0 && args[0].equalsIgnoreCase("test")) {
            int workerCount = 2;
            Worker[] workers = new Worker[workerCount];
            System.out.println("Запуск " + workerCount + " вычислительных воркеров...");
            for (int i = 0; i < workerCount; i++) {
                workers[i] = startWorker();
            }
            Server server = startServer();
            Thread.sleep(3000);
            runTests(server);

            System.out.println("\nТесты завершены. Остановка фоновых служб...");

            server.stop();
            for (Worker worker : workers) {
                worker.stop();
            }
        } else {
            System.out.println("Доступные режимы: test, server, worker");
        }
    }

    /**
     * Конфигурирует, собирает зависимости и запускает TCP-сервер.
     *
     * @return полностью инициализированный и запущенный экземпляр сервера
     * @throws IOException если не удалось открыть серверный сокет или занять порт
     */
    public static Server startServer() throws IOException {
        int tcpPort = 8080;
        int udpPort = 4445;
        int broadcastIntervalMs = 2000;
        int chunkSize = 1000;
        int pingIntervalMs = 30000;

        TaskManager taskManager = new TaskManager();
        ServerAnnouncer announcer = new ServerAnnouncer(tcpPort, udpPort, broadcastIntervalMs);

        Server server = new Server(tcpPort, taskManager, announcer, chunkSize, pingIntervalMs);
        server.start();
        return server;
    }

    /**
     * Создает сетевой локатор и запускает узел-воркер в отдельном фоновом потоке.
     *
     * @return созданный экземпляр воркера, готовый к обработке задач
     */
    public static Worker startWorker() {
        int udpPort = 4445;
        int timeoutMs = 4000;

        ServerLocator locator = new ServerLocator(udpPort, timeoutMs);

        Worker worker = new Worker(locator);

        Thread workerThread = new Thread(worker);
        workerThread.start();
        return worker;
    }

    /**
     * Выполняет демонстрационные пакеты тестов на проверку наличия составных чисел.
     *
     * @param server активный экземпляр сервера для отправки вычислительных пакетов
     */
    public static void runTests(Server server) {
        long[] firstBatch = {6, 8, 7, 13, 5, 9, 4};
        long[] secondBatch = {
                20319251,
                6997901,
                6997927,
                6997937,
                17858849,
                6997967,
                6998009,
                6998029,
                6998039,
                20165149,
                6998051,
                6998053
        };

        System.out.println("\n--- ТЕСТ ПАКЕТА №1 ---");
        boolean res1 = server.hasComposite(firstBatch);
        System.out.println("Результат №1 (Должен быть true): " + res1);

        System.out.println("\n--- ТЕСТ ПАКЕТА №2 ---");
        boolean res2 = server.hasComposite(secondBatch);
        System.out.println("Результат №2 (Должен быть false): " + res2);
    }
}
