package ru.nsu.vmarkidonov.server;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import ru.nsu.vmarkidonov.Protocol;

class TaskMessageTest {

    @Test
    void testToByteArrayFormat() throws IOException {
        long[] input = {10L, 20L, 30L};
        TaskMessage message = new TaskMessage(input);
        byte[] bytes = message.toByteArray();

        assertEquals(4 + 4 + 3 * 8, bytes.length);

        try (DataInputStream dis = new DataInputStream(new ByteArrayInputStream(bytes))) {
            assertEquals(Protocol.TASK.ordinal(), dis.readInt());
            assertEquals(3, dis.readInt());

            long[] output = new long[3];
            for (int i = 0; i < 3; i++) {
                output[i] = dis.readLong();
            }

            assertArrayEquals(input, output);
        }
    }

    @Test
    void testEmptyArray() throws IOException {
        long[] input = {};
        TaskMessage message = new TaskMessage(input);
        byte[] bytes = message.toByteArray();

        assertEquals(8, bytes.length);

        try (DataInputStream dis = new DataInputStream(new ByteArrayInputStream(bytes))) {
            assertEquals(Protocol.TASK.ordinal(), dis.readInt());
            assertEquals(0, dis.readInt());
        }
    }
}