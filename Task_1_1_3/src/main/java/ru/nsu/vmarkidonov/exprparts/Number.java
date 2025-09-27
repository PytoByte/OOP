package ru.nsu.vmarkidonov.exprparts;

import ru.nsu.vmarkidonov.Expression;

public class Number extends Expression {
    public final double num;

    public Number(double num) {
        this.num = num;
    }

    @Override
    public double eval(String values) {
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
    public Expression simplify() {
        return this.clone();
    }

    @Override
    public String toString() {
        return String.valueOf(num);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Number other = (Number) obj;

        return other.num == num;
    }
}
