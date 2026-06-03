package ru.nsu.vmarkidonov.server;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import ru.nsu.vmarkidonov.Protocol;

/**
 * Объект сообщение, описывающий выдачу задачи.
 */
public class TaskMessage {
    private final long[] numbers;

    /**
     * Базовый конструктор.
     *
     * @param numbers массив чисел, переданных клиенту
     */
    public TaskMessage(long[] numbers) {
        this.numbers = numbers;
    }

    /**
     * Сериализация в массив байт.
     *
     * @return массив байт
     * @throws IOException при ошибке сериализация
     */
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (DataOutputStream dos = new DataOutputStream(baos)) {
            dos.writeInt(Protocol.TASK.ordinal());
            dos.writeInt(numbers.length);
            for (long num : numbers) {
                dos.writeLong(num);
            }
            dos.flush();
        }
        return baos.toByteArray();
    }
}
