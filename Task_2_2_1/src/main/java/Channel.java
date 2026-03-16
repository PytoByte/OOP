import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Канал передачи данных между потоками.
 */
public class Channel {
    private final String channelName;
    private final int capacity;
    private boolean closed = false;
    private final Queue<Integer> queue = new LinkedList<>();

    /**
     * Конструктор канала.
     *
     * @param channelName имя канала, отображаемое в логах
     * @param capacity вместимость канала. 0 = бесконечность
     * @throws IllegalArgumentException если capacity меньше 0
     */
    public Channel(String channelName, int capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException("capacity must be >= 0");
        }
        this.channelName = channelName;
        this.capacity = capacity;
    }

    /**
     * Закрыть канал.
     */
    public synchronized void close() {
        closed = true;
        notifyAll();
    }

    /**
     * Проверка на закрытие канала.
     *
     * @return true если канал закрыт, иначе false
     */
    public synchronized boolean isClosed() {
        return closed;
    }

    /**
     * Положить данные в канал.
     *
     * @param id id заказа
     * @throws InterruptedException при прерывании потока
     */
    public synchronized void put(int id) throws InterruptedException {
        if (closed) {
            return;
        }

        while (queue.size() >= capacity && capacity != 0) {
            wait();
        }
        queue.add(id);

        notifyAll();
        System.out.printf("[%d] ON_%s\n", id, channelName);
    }

    /**
     * Взять данные из канала.
     *
     * @param maxCount максимальное число данных, которое можно прочитать
     * @return Список считанных данных
     * @throws InterruptedException при прерывании потока
     */
    public synchronized List<Integer> get(int maxCount) throws InterruptedException {
        while (queue.isEmpty() && !closed) {
            wait();
        }

        int dequeueCount = Math.min(maxCount, queue.size());

        List<Integer> batch = new LinkedList<>();
        for (int i = 0; i < dequeueCount; i++) {
            batch.add(queue.poll());
        }

        notifyAll();
        return batch;
    }
}
