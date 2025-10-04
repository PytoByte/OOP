package ru.nsu.vmarkidonov.exprparts;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashMap;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;
import ru.nsu.vmarkidonov.Expression;

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
    void evalHashMapHasVarValue() {
        Expression exp = new Variable("x");
        HashMap<String, Double> values = new HashMap<>();
        values.put("x", 5.0);
        assertEquals(5, exp.eval(values));
    }

    @Test
    void evalHashMapHasNotVarValue() {
        Expression exp = new Variable("x");
        HashMap<String, Double> values = new HashMap<>();
        values.put("y", 5.0);
        assertThrows(NoSuchElementException.class, () -> exp.eval(values));
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

    @Test
    void testEquals() {
        Expression exp1 = new Variable("x");
        Expression exp2 = new Variable("x");
        assertEquals(exp1.hashCode(), exp2.hashCode());
        assertEquals(exp1, exp2);
    }

    @Test
    void testNotEquals() {
        Expression exp1 = new Variable("x");
        Expression exp2 = new Variable("y");
        assertNotEquals(exp1.hashCode(), exp2.hashCode());
        assertNotEquals(exp1, exp2);
    }
}