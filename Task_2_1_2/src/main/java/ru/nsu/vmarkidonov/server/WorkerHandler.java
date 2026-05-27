package ru.nsu.vmarkidonov.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import ru.nsu.vmarkidonov.Protocol;

/**
 * Обработчик сетевой сессии отдельного вычислительного узла (воркера) на стороне сервера.
 */
public class WorkerHandler implements Runnable {
    private final Socket socket;
    private final TaskManager taskManager;
    private final int pingIntervalMs;
    private long[] currentTask = null;
    private boolean isBusy = false;

    /**
     * Создает обработчик воркера с привязанным сокетом и менеджером задач.
     *
     * @param socket активный сокет соединения с удаленным воркером
     * @param taskManager менеджер распределения вычислительных задач
     * @param pingIntervalMs интервал проверки доступности узла в миллисекундах
     */
    public WorkerHandler(Socket socket, TaskManager taskManager, int pingIntervalMs) {
        this.socket = socket;
        this.taskManager = taskManager;
        this.pingIntervalMs = pingIntervalMs;
    }

    /**
     * Запускает циклический обмен сообщениями с воркером, отправку задач и контроль таймаутов.
     */
    @Override
    public void run() {
        try (
                DataInputStream in = new DataInputStream(socket.getInputStream());
                DataOutputStream out = new DataOutputStream(socket.getOutputStream())
        ) {
            socket.setSoTimeout(1000);
            long lastActivityTime = System.currentTimeMillis();

            while (!socket.isClosed() && !Thread.currentThread().isInterrupted()) {
                if (taskManager.isFoundComposite() && isBusy && currentTask != null) {
                    try {
                        out.writeInt(Protocol.STOP.ordinal());
                        out.flush();
                    } catch (IOException _) {

                    }
                }

                if (!isBusy) {
                    if (taskManager.hasActiveBatch()) {
                        currentTask = taskManager.getNextTask();
                        if (currentTask != null) {
                            out.writeInt(Protocol.TASK.ordinal());
                            out.writeInt(currentTask.length);
                            for (long num : currentTask) {
                                out.writeLong(num);
                            }
                            out.flush();

                            socket.setSoTimeout(5000);
                            if (Protocol.fromOrdinal(in.readInt()) != Protocol.ACCEPT) {
                                throw new IOException("Нарушение протокола: ожидался ACCEPT");
                            }
                            isBusy = true;
                            lastActivityTime = System.currentTimeMillis();
                            socket.setSoTimeout(1000);
                            continue;
                        }
                    }

                    if (System.currentTimeMillis() - lastActivityTime > pingIntervalMs) {
                        out.writeInt(Protocol.STATUS.ordinal());
                        out.flush();
                        socket.setSoTimeout(5000);
                        if (Protocol.fromOrdinal(in.readInt()) != Protocol.OK) {
                            throw new IOException("Нарушение протокола: ожидался OK");
                        }
                        lastActivityTime = System.currentTimeMillis();
                        socket.setSoTimeout(1000);
                    } else {
                        Thread.sleep(200);
                    }
                } else {
                    try {
                        int code = in.readInt();
                        if (Protocol.fromOrdinal(code) == Protocol.RESULT) {
                            boolean hasComposite = in.readBoolean();
                            taskManager.taskFinished(hasComposite);
                            isBusy = false;
                            currentTask = null;
                            lastActivityTime = System.currentTimeMillis();
                        } else {
                            throw new IOException("Нарушение протокола: ожидался RESULT");
                        }
                    } catch (SocketTimeoutException _) {
                        if (taskManager.isFoundComposite()) {
                            isBusy = false;
                            currentTask = null;
                        }
                    }
                }
            }
        } catch (InterruptedException _) {
            Thread.currentThread().interrupt();
        } catch (IOException e) {
            if (!socket.isClosed()) {
                System.err.println("Воркер отключен по ошибке/таймауту: " + e.getMessage());
            }
        } catch (Exception e) {
            System.err.println("Непредвиденная ошибка в WorkerHandler: " + e.getMessage());
        } finally {
            if (isBusy && currentTask != null) {
                taskManager.returnTask(currentTask);
            }
            stop();
        }
    }

    /**
     * Принудительно закрывает сетевой сокет сессии текущего воркера.
     */
    public void stop() {
        if (socket != null && !socket.isClosed()) {
            try {
                socket.close();
            } catch (IOException _) {

            }
        }
    }
}
