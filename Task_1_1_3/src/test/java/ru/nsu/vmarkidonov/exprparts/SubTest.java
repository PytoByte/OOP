package ru.nsu.vmarkidonov.exprparts;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

import java.util.HashMap;
import org.junit.jupiter.api.Test;
import ru.nsu.vmarkidonov.Expression;

class SubTest {

    @Test
    void eval() {
        Expression exp = new Sub(new Number(2), new Number(7));
        assertEquals(-5, exp.eval(""));
    }

    @Test
    void evalHashMap() {
        Expression exp = new Sub(new Number(2), new Number(7));
        assertEquals(-5, exp.eval(new HashMap<>()));
    }

    @Test
    void derivative() {
        Expression exp = new Sub(new Number(1), new Variable("x"));
        Expression expected = new Sub(new Number(0), new Number(0));
        assertEquals(expected, exp.derivative("x"));
    }

    @Test
    void testClone() {
        Expression exp = new Sub(new Number(1), new Number(1));
        assertNotSame(exp, exp.clone());
    }

    @Test
    void testToString() {
        Expression exp = new Sub(new Number(1), new Number(1));
        assertEquals("(1.0-1.0)", exp.toString());
    }

    @Test
    void simplifyNumbers() {
        Expression exp = new Sub(new Number(3), new Number(2));
        Expression expS = exp.simplify();
        assertEquals(new Number(1), expS);
    }

    @Test
    void simplifyVariableLeft() {
        Expression exp = new Sub(new Variable("x"), new Number(3));
        Expression expS = exp.simplify();
        assertEquals(exp, expS);
    }

    @Test
    void simplifyVariableRight() {
        Expression exp = new Sub(new Number(3), new Variable("x"));
        Expression expS = exp.simplify();
        assertEquals(exp, expS);
    }

    @Test
    void simplifyEqualParts() {
        Expression exp = new Sub(
                new Add(new Number(3), new Variable("x")),
                new Add(new Variable("x"), new Number(3))
        );
        Expression expS = exp.simplify();
        assertEquals(new Number(0), expS);
    }
}