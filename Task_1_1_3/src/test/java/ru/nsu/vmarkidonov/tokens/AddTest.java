package ru.nsu.vmarkidonov.tokens;

import org.junit.jupiter.api.Test;
import ru.nsu.vmarkidonov.Expression;

import static org.junit.jupiter.api.Assertions.*;

class AddTest {

    @Test
    void eval() {
        Expression e = new Add(new Number(2), new Number(7));
        assertEquals(9, e.eval(""));
    }

    @Test
    void derivative() {
    }

    @Test
    void testClone() {
    }

    @Test
    void testToString() {
    }
}