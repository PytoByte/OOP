package ru.nsu.vmarkidonov.exprparts;

import org.junit.jupiter.api.Test;
import ru.nsu.vmarkidonov.Expression;

import static org.junit.jupiter.api.Assertions.*;

class DivTest {

    @Test
    void eval() {
        Expression exp = new Div(new Number(10), new Number(2));
        assertEquals(5, exp.eval(""));
    }

    @Test
    void derivative() {
        Expression exp = new Div(new Number(1), new Variable("x"));
        assertEquals("(((0*x)-(1*0))/(x*x))", exp.derivative("x").toString());
    }

    @Test
    void testClone() {
        Expression exp = new Div(new Number(1), new Number(1));
        assertNotSame(exp, exp.clone());
    }

    @Test
    void testToString() {
        Expression exp = new Div(new Number(1), new Number(1));
        assertEquals("(1/1)", exp.toString());
    }
}