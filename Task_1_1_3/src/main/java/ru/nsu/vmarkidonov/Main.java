package ru.nsu.vmarkidonov;

import ru.nsu.vmarkidonov.exprparts.Add;
import ru.nsu.vmarkidonov.exprparts.Mul;
import ru.nsu.vmarkidonov.exprparts.Variable;
import ru.nsu.vmarkidonov.exprparts.Number;
import ru.nsu.vmarkidonov.parser.Parser;

public class Main {
    public static void main(String[] args) {
        Expression e = new Add(new Number(3), new Mul(new Number(2), new Variable("x")));

        System.out.println(e);
        System.out.println(e.eval("x = 10; y = 20"));
        System.out.println(e.derivative("x"));
        e = Parser.parseExpression("999999+x+y-z*(56-0)/94794-88888.1314/(345*890)");
        System.out.println(e);
        Expression e1 = e.simplify();
        System.out.println(e1);
    }
}