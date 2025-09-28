package ru.nsu.vmarkidonov.exprparts;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

import org.junit.jupiter.api.Test;
import ru.nsu.vmarkidonov.Expression;

class NumberTest {

    @Test
    void eval() {
        Expression exp = new Number(5);
        assertEquals(5, exp.eval(""));
    }

    @Test
    void derivative() {
        Expression exp = new Number(5);
        assertEquals(new Number(0), exp.derivative(""));
    }

    @Test
    void testClone() {
        Expression exp = new Number(5);
        assertNotSame(exp, exp.clone());
    }

    @Test
    void testToString() {
        Expression exp = new Number(5);
        assertEquals("5.0", exp.toString());
    }

    @Test
    void simplify() {
        Expression exp = new Number(5);
        Expression expS = exp.simplify();
        assertEquals(exp, expS);
    }
}