package ru.nsu.vmarkidonov.tokens;

import org.junit.jupiter.api.Test;
import ru.nsu.vmarkidonov.Expression;

import static org.junit.jupiter.api.Assertions.*;

class MulTest {

    @Test
    void eval() {
        Expression e = new Mul(new Number(2), new Number(7));
        assertEquals(14, e.eval(""));
    }

    @Test
    void derivative() {
        Expression e = new Mul(new Number(1), new Variable("x"));
        assertEquals("((0*x)+(1*0))", e.derivative("x").toString());
    }

    @Test
    void testClone() {
        Expression e = new Mul(new Number(1), new Number(1));
        assertNotSame(e, e.clone());
    }

    @Test
    void testToString() {
        Expression e = new Mul(new Number(1), new Number(1));
        assertEquals("(1*1)", e.toString());
    }
}