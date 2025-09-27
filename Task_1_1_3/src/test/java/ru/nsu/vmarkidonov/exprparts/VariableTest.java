package ru.nsu.vmarkidonov.exprparts;

import org.junit.jupiter.api.Test;
import ru.nsu.vmarkidonov.Expression;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class VariableTest {

    @Test
    void evalHasVarValue() {
        Expression exp = new Variable("x");
        assertEquals(4, exp.eval("x = 4"));
    }

    @Test
    void evalHasNotVarValue() {
        Expression exp = new Variable("x");
        assertThrows(NoSuchElementException.class, () -> exp.eval(""));
    }

    @Test
    void derivativeHasVar() {
        Expression exp = new Variable("x");
        Expression expected = new Number(0);
        assertEquals(expected, exp.derivative("x"));
    }

    @Test
    void derivativeHasNotVar() {
        Expression exp = new Variable("x");
        assertEquals(exp, exp.derivative("y"));
    }

    @Test
    void testClone() {
        Expression exp = new Variable("x");
        assertNotSame(exp, exp.clone());
    }

    @Test
    void testToString() {
        Expression exp = new Variable("x");
        assertEquals("x", exp.toString());
    }

    @Test
    void simplify() {
        Expression exp = new Variable("x");
        Expression expS = exp.simplify();
        assertEquals(exp, expS);
    }
}