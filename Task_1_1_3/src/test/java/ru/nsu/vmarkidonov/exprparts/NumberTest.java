package ru.nsu.vmarkidonov.exprparts;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

import java.util.HashMap;
import org.junit.jupiter.api.Test;
import ru.nsu.vmarkidonov.Expression;

class NumberTest {

    @Test
    void eval() {
        Expression exp = new Number(5);
        assertEquals(5, exp.eval(""));
    }

    @Test
    void evalHashMap() {
        Expression exp = new Number(5);
        assertEquals(5, exp.eval(new HashMap<>()));
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

    @Test
    void testEquals() {
        Expression exp1 = new Number(5);
        Expression exp2 = new Number(5);
        assertEquals(exp1.hashCode(), exp2.hashCode());
        assertEquals(exp1, exp2);
    }

    @Test
    void testNotEquals() {
        Expression exp1 = new Number(4);
        Expression exp2 = new Number(5);
        assertNotEquals(exp1.hashCode(), exp2.hashCode());
        assertNotEquals(exp1, exp2);
    }
}