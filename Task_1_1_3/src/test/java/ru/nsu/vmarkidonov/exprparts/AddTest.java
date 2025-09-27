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
        Expression expected = new Add(new Number(0), new Number(0));
        assertEquals(expected, exp.derivative("x"));
    }

    @Test
    void testClone() {
        Expression exp = new Add(new Number(1), new Number(1));
        assertNotSame(exp, exp.clone());
    }

    @Test
    void testToString() {
        Expression exp = new Add(new Number(1), new Number(1));
        assertEquals("(1.0+1.0)", exp.toString());
    }

    @Test
    void simplifyNumbers() {
        Expression exp = new Add(new Number(2), new Number(3));
        Expression expS = exp.simplify();
        assertEquals(new Number(5), expS);
    }

    @Test
    void simplifyVariableLeft() {
        Expression exp = new Add(new Variable("x"), new Number(3));
        Expression expS = exp.simplify();
        assertEquals(exp, expS);
    }

    @Test
    void simplifyVariableRight() {
        Expression exp = new Add(new Number(3), new Variable("x"));
        Expression expS = exp.simplify();
        assertEquals(exp, expS);
    }
}