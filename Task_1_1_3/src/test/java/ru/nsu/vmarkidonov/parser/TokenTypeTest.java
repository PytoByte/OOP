package ru.nsu.vmarkidonov.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class TokenTypeTest {

    @Test
    void isBracket() {
        char[] brs = {'(', ')'};
        for (char br : brs) {
            assertTrue(TokenType.isBracket(br));
        }

        char[] notBrs = {'+', 'a', '1'};
        for (char notBr : notBrs) {
            assertFalse(TokenType.isBracket(notBr));
        }
    }

    @Test
    void matchBracket() {
        char[] brs = {'(', ')'};
        TokenType[] brsTypes = {TokenType.LBR, TokenType.RBR};
        for (int i = 0; i < brs.length; i++) {
            assertEquals(brsTypes[i], TokenType.matchBracket(brs[i]));
        }
    }

    @Test
    void isOperator() {
        char[] opers = {'+', '-', '*', '/'};
        for (char oper : opers) {
            assertTrue(TokenType.isOperator(oper));
        }

        char[] notOpers = {'(', ')', 'a', '1'};
        for (char notOper : notOpers) {
            assertFalse(TokenType.isOperator(notOper));
        }
    }

    @Test
    void matchOperator() {
        char[] opers = {'+', '-', '*', '/'};
        TokenType[] opersTypes = {TokenType.ADD, TokenType.SUB, TokenType.MUL, TokenType.DIV};
        for (int i = 0; i < opers.length; i++) {
            assertEquals(opersTypes[i], TokenType.matchOperator(opers[i]));
        }
    }
}