package ru.nsu.vmarkidonov.tokens;

import org.junit.jupiter.api.Test;
import ru.nsu.vmarkidonov.Expression;

import static org.junit.jupiter.api.Assertions.*;

class NumberTest {

    @Test
    void eval() {
        Expression e = new Number(5);
        assertEquals(5, e.eval(""));
    }

    @Test
    void derivative() {
        Expression e = new Number(5);
        assertEquals("0", e.derivative("").toString());
    }

    @Test
    void testClone() {
        Expression e = new Number(5);
        assertNotSame(e, e.clone());
    }

    @Test
    void testToString() {
        Expression e = new Number(5);
        assertEquals("5", e.toString());
    }
}