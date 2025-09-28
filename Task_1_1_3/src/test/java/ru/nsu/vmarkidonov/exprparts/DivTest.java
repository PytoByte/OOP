package ru.nsu.vmarkidonov.exprparts;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import ru.nsu.vmarkidonov.Expression;

class DivTest {

    @Test
    void eval() {
        Expression exp = new Div(new Number(10), new Number(2));
        assertEquals(5, exp.eval(""));
    }

    @Test
    void evalZeroRight() {
        Expression exp = new Div(new Number(10), new Number(0));
        assertThrows(RuntimeException.class, () -> exp.eval(""));
    }

    @Test
    void derivative() {
        Expression exp = new Div(new Number(1), new Variable("x"));
        Expression expected = new Div(
                new Sub(
                    new Mul(new Number(0), new Variable("x")),
                    new Mul(new Number(1), new Number(0))
                ),
                new Mul(new Variable("x"), new Variable("x"))
        );
        assertEquals(expected, exp.derivative("x"));
    }

    @Test
    void testClone() {
        Expression exp = new Div(new Number(1), new Number(1));
        assertNotSame(exp, exp.clone());
    }

    @Test
    void testToString() {
        Expression exp = new Div(new Number(1), new Number(1));
        assertEquals("(1.0/1.0)", exp.toString());
    }

    @Test
    void simplifyNumbers() {
        Expression exp = new Div(new Number(10), new Number(2));
        Expression expS = exp.simplify();
        assertEquals(new Number(5), expS);
    }

    @Test
    void simplifyZeroLeft() {
        Expression exp = new Div(new Number(0), new Variable("x"));
        Expression expS = exp.simplify();
        assertEquals(new Number(0), expS);
    }

    @Test
    void simplifyZeroRight() {
        Expression exp = new Div(new Variable("x"), new Number(0));
        assertThrows(RuntimeException.class, exp::simplify);
    }

    @Test
    void simplifyOneRight() {
        Expression exp = new Div(new Variable("x"), new Number(1));
        Expression expS = exp.simplify();
        assertEquals(new Variable("x"), expS);
    }

    @Test
    void simplifyVariableLeft() {
        Expression exp = new Div(new Variable("x"), new Number(3));
        Expression expS = exp.simplify();
        assertEquals(exp, expS);
    }

    @Test
    void simplifyVariableRight() {
        Expression exp = new Div(new Number(3), new Variable("x"));
        Expression expS = exp.simplify();
        assertEquals(exp, expS);
    }
}