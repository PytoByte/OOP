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
        assertEquals("0", exp.derivative("x").toString());
    }

    @Test
    void derivativeHasNotVar() {
        Expression exp = new Variable("x");
        assertEquals("x", exp.derivative("y").toString());
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
}