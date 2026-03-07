import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ChannelTest {

    @Test
    void close() throws InterruptedException {
        Channel channel = new Channel("TEST_CHANNEL", 0);
        channel.put(0);
        channel.close();
        assertTrue(channel.isClosed());
        channel.put(1);
        assertEquals(1, channel.get(2).size());
    }

    @Test
    void isClosed() {
        Channel channel = new Channel("TEST_CHANNEL", 0);
        assertFalse(channel.isClosed());
        channel.close();
        assertTrue(channel.isClosed());
    }

    @Test
    void put() throws InterruptedException {
        Channel channel = new Channel("TEST_CHANNEL", 0);
        channel.put(0);
        assertEquals(1, channel.get(2).size());
        channel.put(1);
        channel.put(2);
        assertEquals(2, channel.get(2).size());
    }

    @Test
    void get() throws InterruptedException {
        Channel channel = new Channel("TEST_CHANNEL", 0);
        channel.put(0);
        channel.put(1);
        channel.put(2);
        assertArrayEquals(new Integer[] {0, 1, 2}, channel.get(3).toArray(new Integer[0]));
    }
}