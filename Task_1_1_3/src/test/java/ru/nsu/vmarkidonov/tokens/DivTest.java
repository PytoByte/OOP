package ru.nsu.vmarkidonov.tokens;

import org.junit.jupiter.api.Test;
import ru.nsu.vmarkidonov.Expression;

import static org.junit.jupiter.api.Assertions.*;

class DivTest {

    @Test
    void eval() {
        Expression e = new Div(new Number(10), new Number(2));
        assertEquals(5, e.eval(""));
    }

    @Test
    void derivative() {
        Expression e = new Div(new Number(1), new Variable("x"));
        assertEquals("(((0*x)-(1*0))/(x*x))", e.derivative("x").toString());
    }

    @Test
    void testClone() {
        Expression e = new Div(new Number(1), new Number(1));
        assertNotSame(e, e.clone());
    }

    @Test
    void testToString() {
        Expression e = new Div(new Number(1), new Number(1));
        assertEquals("(1/1)", e.toString());
    }
}