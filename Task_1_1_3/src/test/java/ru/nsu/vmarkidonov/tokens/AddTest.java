package ru.nsu.vmarkidonov.tokens;

import org.junit.jupiter.api.Test;
import ru.nsu.vmarkidonov.Expression;
import ru.nsu.vmarkidonov.tokens.Number;

import static org.junit.jupiter.api.Assertions.*;

class AddTest {

    @Test
    void eval() {
        Expression e = new Add(new Number(2), new Number(7));
        assertEquals(9, e.eval(""));
    }

    @Test
    void derivative() {
        Expression e = new Add(new Number(1), new Variable("x"));
        assertEquals("(0+0)", e.derivative("x").toString());
    }

    @Test
    void testClone() {
        Expression e = new Add(new Number(1), new Number(1));
        assertNotSame(e, e.clone());
    }

    @Test
    void testToString() {
        Expression e = new Add(new Number(1), new Number(1));
        assertEquals("(1+1)", e.toString());
    }
}