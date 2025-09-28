package ru.nsu.vmarkidonov.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import ru.nsu.vmarkidonov.Expression;
import ru.nsu.vmarkidonov.exprparts.Add;
import ru.nsu.vmarkidonov.exprparts.Number;

class TokenTest {

    @Test
    void pushParam() {
        Token token = new Token(TokenType.ADD, "+", 1);
        token.pushParam(new Token(TokenType.NUMBER, "1", 0));
        token.pushParam(new Token(TokenType.NUMBER, "2", 2));
        assertTrue(token.isComplete());
    }

    @Test
    void popParam() {
        Token token = new Token(TokenType.ADD, "+", 1);
        Token pushingParam = new Token(TokenType.NUMBER, "1", 0);
        token.pushParam(pushingParam);
        Token poppingParam = token.popParam();
        assertSame(pushingParam, poppingParam);
    }

    @Test
    void toExpression() {
        Token tokenTree = new Token(TokenType.ADD, "+", 1);
        tokenTree.pushParam(new Token(TokenType.NUMBER, "1", 0));
        tokenTree.pushParam(new Token(TokenType.NUMBER, "2", 2));
        Expression expected = new Add(new Number(1), new Number(2));
        assertEquals(expected, tokenTree.toExpression());
    }

    @Test
    void testToString() {
        Token tokenTree = new Token(TokenType.ADD, "+", 1);
        tokenTree.pushParam(new Token(TokenType.NUMBER, "1", 0));
        tokenTree.pushParam(new Token(TokenType.NUMBER, "2", 2));
        assertEquals(
                "ADD(lexeme=\"+\",pos=1)[NUMBER(lexeme=\"1\",pos=0), NUMBER(lexeme=\"2\",pos=2)]",
                tokenTree.toString()
        );
    }
}