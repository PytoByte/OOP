package ru.nsu.vmarkidonov.exprparts;

import org.junit.jupiter.api.Test;
import ru.nsu.vmarkidonov.Expression;

import static org.junit.jupiter.api.Assertions.*;

class AddTest {

    @Test
    void eval() {
        Expression exp = new Add(new Number(2), new Number(7));
        assertEquals(9, exp.eval(""));
    }

    @Test
    void derivative() {
        Expression exp = new Add(new Number(1), new Variable("x"));
        assertEquals("(0+0)", exp.derivative("x").toString());
    }

    @Test
    void testClone() {
        Expression exp = new Add(new Number(1), new Number(1));
        assertNotSame(exp, exp.clone());
    }

    @Test
    void testToString() {
        Expression exp = new Add(new Number(1), new Number(1));
        assertEquals("(1+1)", exp.toString());
    }
}