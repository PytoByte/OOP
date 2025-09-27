package ru.nsu.vmarkidonov.parser;

import org.junit.jupiter.api.Test;
import ru.nsu.vmarkidonov.Expression;
import ru.nsu.vmarkidonov.exprparts.Add;
import ru.nsu.vmarkidonov.exprparts.Number;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest {

    @Test
    void parseExpression() {
        Expression exp = Parser.parseExpression("1+1");
        Expression expected = new Add(new Number(1), new Number(1));
    }
}