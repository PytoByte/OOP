package ru.nsu.vmarkidonov;

import ru.nsu.vmarkidonov.exprparts.Add;
import ru.nsu.vmarkidonov.exprparts.Mul;
import ru.nsu.vmarkidonov.exprparts.Variable;
import ru.nsu.vmarkidonov.exprparts.Number;
import ru.nsu.vmarkidonov.parser.Parser;

import java.text.ParseException;

public class Main {
    public static void main(String[] args) {
        Expression e = new Add(new Number(3), new Mul(new Number(2),
                new Variable("x")));

        System.out.println(e);
        System.out.println(e.eval("x = 10; y = 20"));
        System.out.println(e.derivative("x"));
        Parser.parseExpression("1+2+3+4");
    }
}