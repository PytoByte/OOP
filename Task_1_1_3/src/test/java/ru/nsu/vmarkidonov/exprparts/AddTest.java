package ru.nsu.vmarkidonov.exprparts;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

import java.util.HashMap;
import org.junit.jupiter.api.Test;
import ru.nsu.vmarkidonov.Expression;

class AddTest {
    @Test
    void eval() {
        Expression exp = new Add(new Number(2), new Number(7));
        assertEquals(9, exp.eval(""));
    }

    @Test
    void evalHashMap() {
        Expression exp = new Add(new Number(2), new Number(7));
        assertEquals(9, exp.eval(new HashMap<>()));
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
    void simplifyZeroRight() {
        Expression exp = new Add(new Mul(new Variable("x"), new Number(10)), new Number(0));
        Expression expS = exp.simplify();

        Expression expected = new Mul(new Variable("x"), new Number(10));
        assertEquals(expected, expS);
    }

    @Test
    void simplifyZeroLeft() {
        Expression exp = new Add(new Number(0), new Mul(new Variable("x"), new Number(10)));
        Expression expS = exp.simplify();

        Expression expected = new Mul(new Variable("x"), new Number(10));
        assertEquals(expected, expS);
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

    @Test
    void testEqualsAsymmetrical() {
        Expression exp1 = new Add(new Number(2), new Number(3));
        Expression exp2 = new Add(new Number(2), new Number(3));
        assertEquals(exp1.hashCode(), exp2.hashCode());
        assertEquals(exp1, exp2);
    }

    @Test
    void testEqualsSymmetrical() {
        Expression exp1 = new Add(new Number(2), new Number(3));
        Expression exp2 = new Add(new Number(3), new Number(2));
        assertEquals(exp1.hashCode(), exp2.hashCode());
        assertEquals(exp1, exp2);
    }

    @Test
    void testNotEquals() {
        Expression exp1 = new Add(new Number(6), new Number(1));
        Expression exp2 = new Add(new Number(5), new Number(10));
        assertNotEquals(exp1.hashCode(), exp2.hashCode());
        assertNotEquals(exp1, exp2);
    }
}