package ru.nsu.vmarkidonov.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import ru.nsu.vmarkidonov.Expression;
import ru.nsu.vmarkidonov.exprparts.Add;
import ru.nsu.vmarkidonov.exprparts.Div;
import ru.nsu.vmarkidonov.exprparts.Mul;
import ru.nsu.vmarkidonov.exprparts.Number;
import ru.nsu.vmarkidonov.exprparts.Sub;
import ru.nsu.vmarkidonov.exprparts.Variable;

class ParserTest {

    @Test
    void parseExprNumInt() {
        Expression exp = Parser.parseExpression("1");
        Expression expected = new Number(1);
        assertEquals(expected, exp);
    }

    @Test
    void parseExprNumDouble() {
        Expression exp = Parser.parseExpression("3.1415");
        Expression expected = new Number(3.1415);
        assertEquals(expected, exp);
    }

    @Test
    void parseExprNumInBrackets() {
        Expression exp = Parser.parseExpression("(3.1415)");
        Expression expected = new Number(3.1415);
        assertEquals(expected, exp);
    }

    @Test
    void parseExprVar() {
        Expression exp = Parser.parseExpression("someVar");
        Expression expected = new Variable("someVar");
        assertEquals(expected, exp);
    }

    @Test
    void parseExprVarInBrackets() {
        Expression exp = Parser.parseExpression("(someVar)");
        Expression expected = new Variable("someVar");
        assertEquals(expected, exp);
    }

    @Test
    void parseExprSum() {
        Expression exp = Parser.parseExpression("1+1");
        Expression expected = new Add(new Number(1), new Number(1));
        assertEquals(expected, exp);
    }

    @Test
    void parseExprSub() {
        Expression exp = Parser.parseExpression("1-1");
        Expression expected = new Sub(new Number(1), new Number(1));
        assertEquals(expected, exp);
    }

    @Test
    void parseExprMul() {
        Expression exp = Parser.parseExpression("1*1");
        Expression expected = new Mul(new Number(1), new Number(1));
        assertEquals(expected, exp);
    }

    @Test
    void parseExprDiv() {
        Expression exp = Parser.parseExpression("1/1");
        Expression expected = new Div(new Number(1), new Number(1));
        assertEquals(expected, exp);
    }

    @Test
    void parseExprHigherPriorityRight() {
        Expression exp = Parser.parseExpression("1+2*3");
        Expression expected = new Add(new Number(1), new Mul(new Number(2), new Number(3)));
        assertEquals(expected, exp);
    }

    @Test
    void parseExprHigherPriorityLeft() {
        Expression exp = Parser.parseExpression("1*2+3");
        Expression expected = new Add(new Mul(new Number(1), new Number(2)), new Number(3));
        assertEquals(expected, exp);
    }

    @Test
    void parseExprEqualPriority() {
        Expression exp = Parser.parseExpression("1+2+3");
        Expression expected = new Add(new Add(new Number(1), new Number(2)), new Number(3));
        assertEquals(expected, exp);
    }

    @Test
    void parseExprEqualPriorityWithBrackets() {
        Expression exp = Parser.parseExpression("1+(2+3)");
        Expression expected = new Add(new Number(1), new Add(new Number(2), new Number(3)));
        assertEquals(expected, exp);
    }

    @Test
    void parseExprHigherPriorityRightWithBrackets() {
        Expression exp = Parser.parseExpression("(1+2)*3");
        Expression expected = new Mul(new Add(new Number(1), new Number(2)), new Number(3));
        assertEquals(expected, exp);
    }

    @Test
    void parseExprHigherPriorityLeftWithBrackets() {
        Expression exp = Parser.parseExpression("1*(2+3)");
        Expression expected = new Mul(new Number(1), new Add(new Number(2), new Number(3)));
        assertEquals(expected, exp);
    }

    @Test
    void parseExprWithBrackets() {
        Expression expr = Parser.parseExpression("1+2/(3*(4-5))");
        Expression expected = new Add(
                new Number(1),
                new Div(
                        new Number(2),
                        new Mul(
                                new Number(3),
                                new Sub(
                                        new Number(4),
                                        new Number(5)
                                )
                        )
                )
        );
        assertEquals(expected, expr);
    }

    @Test
    void parseExprMissRightBrackets() {
        assertThrows(ParserException.class, () -> Parser.parseExpression("((1+1)"));
    }

    @Test
    void parseExprMissLeftBrackets() {
        assertThrows(ParserException.class, () -> Parser.parseExpression("(1+1))"));
    }

    @Test
    void parseExprToManyDots() {
        assertThrows(ParserException.class, () -> Parser.parseExpression("1..0"));
    }

    @Test
    void parseExprIncomplete() {
        assertThrows(ParserException.class, () -> Parser.parseExpression("1+"));
    }

    @Test
    void parseExprOutsideOperator() {
        assertThrows(ParserException.class, () -> Parser.parseExpression("(1+)1"));
    }
}