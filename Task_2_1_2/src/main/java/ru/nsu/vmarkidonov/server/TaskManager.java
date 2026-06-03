package ru.nsu.vmarkidonov.server;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Менеджер задач для распределения вычислительных блоков и синхронизации выполнения пакета данных.
 */
public class TaskManager {
    private final ConcurrentLinkedQueue<long[]> pendingTasks = new ConcurrentLinkedQueue<>();
    private final AtomicInteger activeTasksCount = new AtomicInteger(0);
    private volatile boolean foundComposite = false;
    private volatile boolean hasActiveBatch = false;

    private final ReentrantLock lock = new ReentrantLock();
    private final Condition completionCondition = lock.newCondition();

    /**
     * Инициализирует новый пакет вычислений и заполняет очередь задач блоками данных.
     *
     * @param chunks двумерный массив, содержащий разделенные блоки чисел для проверки
     */
    public void submitBatch(long[][] chunks) {
        lock.lock();
        try {
            pendingTasks.clear();
            activeTasksCount.set(0);
            foundComposite = false;
            hasActiveBatch = true;
            for (long[] chunk : chunks) {
                pendingTasks.offer(chunk);
            }
            completionCondition.signalAll();
        } finally {
            lock.unlock();
        }
    }

    /**
     * Извлекает следующий доступный блок чисел для обработки из очереди задач.
     *
     * @return массив чисел для вычислений или null, если доступных задач нет
     */
    public long[] getNextTask() {
        lock.lock();
        try {
            long[] task = pendingTasks.poll();
            if (task != null) {
                activeTasksCount.incrementAndGet();
            }
            return task;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Возвращает задачу обратно в очередь для повторной обработки в случае сбоя воркера.
     *
     * @param chunk массив чисел незавершенной задачи для повторного добавления
     */
    public void returnTask(long[] chunk) {
        lock.lock();
        try {
            if (hasActiveBatch && !foundComposite) {
                pendingTasks.offer(chunk);
                activeTasksCount.decrementAndGet();
                completionCondition.signalAll();
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * Фиксирует завершение обработки блока чисел и проверяет условия окончания всего пакета.
     *
     * @param resultIsComposite признак того, было ли обнаружено составное число в блоке
     */
    public void taskFinished(boolean resultIsComposite) {
        lock.lock();
        try {
            if (resultIsComposite) {
                foundComposite = true;
            }
            activeTasksCount.decrementAndGet();

            if (foundComposite || (pendingTasks.isEmpty() && activeTasksCount.get() == 0)) {
                hasActiveBatch = false;
                completionCondition.signalAll();
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * Проверяет факт обнаружения хотя бы одного составного числа в текущем пакете.
     *
     * @return true, если составное число найдено, иначе false
     */
    public boolean isFoundComposite() {
        return foundComposite;
    }

    /**
     * Проверяет, находится ли пакет задач в процессе выполнения.
     *
     * @return true, если пакет активен, иначе false
     */
    public boolean hasActiveBatch() {
        return hasActiveBatch;
    }

    /**
     * Блокирует поток до завершения обработки всех блоков или обнаружения составного числа.
     *
     * @throws InterruptedException если поток ожидания результатов был прерван
     */
    public void waitForCompletion() throws InterruptedException {
        lock.lock();
        try {
            while (hasActiveBatch
                    && !foundComposite
                    && !(pendingTasks.isEmpty() && activeTasksCount.get() == 0)
            ) {
                completionCondition.await();
            }
        } finally {
            lock.unlock();
        }
    }
}
