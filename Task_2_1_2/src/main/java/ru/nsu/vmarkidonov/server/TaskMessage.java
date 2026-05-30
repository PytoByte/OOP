package ru.nsu.vmarkidonov.server;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import ru.nsu.vmarkidonov.Protocol;

public class TaskMessage {
    private final long[] numbers;

    public TaskMessage(long[] numbers) {
        this.numbers = numbers;
    }

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
