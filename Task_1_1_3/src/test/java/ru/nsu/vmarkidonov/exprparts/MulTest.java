package ru.nsu.vmarkidonov.exprparts;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

import java.util.HashMap;
import org.junit.jupiter.api.Test;
import ru.nsu.vmarkidonov.Expression;

class MulTest {

    @Test
    void eval() {
        Expression exp = new Mul(new Number(2), new Number(7));
        assertEquals(14, exp.eval(""));
    }

    @Test
    void evalHashMap() {
        Expression exp = new Mul(new Number(2), new Number(7));
        assertEquals(14, exp.eval(new HashMap<>()));
    }

    @Test
    void derivative() {
        Expression exp = new Mul(new Number(1), new Variable("x"));
        Expression expected = new Add(
                new Mul(new Number(0), new Variable("x")),
                new Mul(new Number(1), new Number(0))
        );
        assertEquals(expected, exp.derivative("x"));
    }

    @Test
    void testClone() {
        Expression exp = new Mul(new Number(1), new Number(1));
        assertNotSame(exp, exp.clone());
    }

    @Test
    void testToString() {
        Expression exp = new Mul(new Number(1), new Number(1));
        assertEquals("(1.0*1.0)", exp.toString());
    }

    @Test
    void simplifyNumbers() {
        Expression exp = new Mul(new Number(4), new Number(2));
        Expression expS = exp.simplify();
        assertEquals(new Number(8), expS);
    }

    @Test
    void simplifyZeroLeft() {
        Expression exp = new Mul(new Number(0), new Add(new Variable("x"), new Number(1)));
        Expression expS = exp.simplify();
        assertEquals(new Number(0), expS);
    }

    @Test
    void simplifyZeroRight() {
        Expression exp = new Mul(new Add(new Variable("x"), new Number(1)), new Number(0));
        Expression expS = exp.simplify();
        assertEquals(new Number(0), expS);
    }

    @Test
    void simplifyOneLeft() {
        Expression exp = new Mul(new Number(1), new Variable("x"));
        Expression expS = exp.simplify();
        assertEquals(new Variable("x"), expS);
    }

    @Test
    void simplifyOneRight() {
        Expression exp = new Mul(new Variable("x"), new Number(1));
        Expression expS = exp.simplify();
        assertEquals(new Variable("x"), expS);
    }

    @Test
    void simplifyVariableLeft() {
        Expression exp = new Mul(new Variable("x"), new Number(3));
        Expression expS = exp.simplify();
        assertEquals(exp, expS);
    }

    @Test
    void simplifyVariableRight() {
        Expression exp = new Mul(new Number(3), new Variable("x"));
        Expression expS = exp.simplify();
        assertEquals(exp, expS);
    }

    @Test
    void testEqualsAsymmetrical() {
        Expression exp1 = new Mul(new Number(2), new Number(3));
        Expression exp2 = new Mul(new Number(2), new Number(3));
        assertEquals(exp1.hashCode(), exp2.hashCode());
        assertEquals(exp1, exp2);
    }

    @Test
    void testEqualsSymmetrical() {
        Expression exp1 = new Mul(new Number(2), new Number(3));
        Expression exp2 = new Mul(new Number(3), new Number(2));
        assertEquals(exp1.hashCode(), exp2.hashCode());
        assertEquals(exp1, exp2);
    }

    @Test
    void testNotEquals() {
        Expression exp1 = new Mul(new Number(6), new Number(1));
        Expression exp2 = new Mul(new Number(5), new Number(10));
        assertNotEquals(exp1.hashCode(), exp2.hashCode());
        assertNotEquals(exp1, exp2);
    }
}