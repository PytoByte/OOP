package ru.nsu.vmarkidonov.tokens;

import ru.nsu.vmarkidonov.Expression;

public class Number extends Expression {
    private final int num;

    public Number(int num) {
        this.num = num;
    }

    @Override
    public int eval(String values) {
        return num;
    }

    @Override
    public Expression derivative(String var) {
        return new Number(0);
    }

    @Override
    public Expression clone() {
        return new Number(num);
    }

    @Override
    public String toString() {
        return String.valueOf(num);
    }
}
