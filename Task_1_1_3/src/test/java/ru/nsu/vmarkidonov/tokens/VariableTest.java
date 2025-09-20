package ru.nsu.vmarkidonov.tokens;

import org.junit.jupiter.api.Test;
import ru.nsu.vmarkidonov.Expression;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class VariableTest {

    @Test
    void evalHasVarValue() {
        Expression e = new Variable("x");
        assertEquals(4, e.eval("x = 4"));
    }

    @Test
    void evalHasNotVarValue() {
        Expression e = new Variable("x");
        assertThrows(NoSuchElementException.class, () -> e.eval(""));
    }

    @Test
    void derivativeHasVar() {
        Expression e = new Variable("x");
        assertEquals("0", e.derivative("x").toString());
    }

    @Test
    void derivativeHasNotVar() {
        Expression e = new Variable("x");
        assertEquals("x", e.derivative("y").toString());
    }

    @Test
    void testClone() {
        Expression e = new Variable("x");
        assertNotSame(e, e.clone());
    }

    @Test
    void testToString() {
        Expression e = new Variable("x");
        assertEquals("x", e.toString());
    }
}