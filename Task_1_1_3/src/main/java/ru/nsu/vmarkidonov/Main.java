package ru.nsu.vmarkidonov;

import ru.nsu.vmarkidonov.tokens.Add;
import ru.nsu.vmarkidonov.tokens.Mul;
import ru.nsu.vmarkidonov.tokens.Variable;
import ru.nsu.vmarkidonov.tokens.Number;

public class Main {
    public static void main(String[] args) {
        Expression e = new Add(new Number(3), new Mul(new Number(2),
                new Variable("x")));

        System.out.println(e);
        System.out.println(e.eval("x = 10; y = 20"));
        System.out.println(e.derivative("x"));
    }
}