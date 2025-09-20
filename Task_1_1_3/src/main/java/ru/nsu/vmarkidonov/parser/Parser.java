package ru.nsu.vmarkidonov.parser;

import ru.nsu.vmarkidonov.Expression;
import ru.nsu.vmarkidonov.exprparts.Number;

public class Parser {
    public static Expression parseExpression(String expString) {
        char[] expChars = expString.toCharArray();

        Expression exp = null;

        for (int i = 0; i < expChars.length; i++) {
            if (exp != null) {
                if (Character.isDigit(expChars[i]) && (exp.getClass() == Number.class)) {
                    exp = new Number(((Number)exp).num);
                    continue;
                }
            }

            if (Character.isDigit(expChars[i])) {
                exp = new Number(expChars[i] - '0');
            }
        }

        return exp;
    }
}
