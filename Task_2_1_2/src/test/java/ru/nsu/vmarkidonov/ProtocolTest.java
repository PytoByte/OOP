package ru.nsu.vmarkidonov;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class ProtocolTest {

    @Test
    void testFromOrdinalValid() {
        assertEquals(Protocol.STATUS, Protocol.fromOrdinal(0));
        assertEquals(Protocol.OK, Protocol.fromOrdinal(1));
        assertEquals(Protocol.TASK, Protocol.fromOrdinal(2));
        assertEquals(Protocol.ACCEPT, Protocol.fromOrdinal(3));
        assertEquals(Protocol.RESULT, Protocol.fromOrdinal(4));
        assertEquals(Protocol.STOP, Protocol.fromOrdinal(5));
    }

    @Test
    void testFromOrdinalNegativeBoundary() {
        assertThrows(IllegalArgumentException.class, () -> Protocol.fromOrdinal(-1));
        assertThrows(IllegalArgumentException.class, () -> Protocol.fromOrdinal(-100));
    }

    @Test
    void testFromOrdinalOverflowBoundary() {
        int outOfBoundsOrdinal = Protocol.values().length;
        assertThrows(IllegalArgumentException.class, () -> Protocol.fromOrdinal(outOfBoundsOrdinal));
        assertThrows(IllegalArgumentException.class, () -> Protocol.fromOrdinal(outOfBoundsOrdinal + 1));
    }
}
