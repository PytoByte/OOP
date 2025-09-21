package ru.nsu.vmarkidonov.exprparts;

import ru.nsu.vmarkidonov.Expression;

public class Sub extends Operator {
    public Expression exp1;
    public Expression exp2;

    public Sub(Expression exp1, Expression exp2) {
        this.exp1 = exp1;
        this.exp2 = exp2;
    }

    @Override
    public int eval(String values) {
        return exp1.eval(values)-exp2.eval(values);
    }

    @Override
    public Expression derivative(String var) {
        return new Sub(exp1.derivative(var), exp2.derivative(var));
    }

    @Override
    public Expression clone() {
        return new Sub(exp1, exp2);
    }

    @Override
    public String toString() {
        return String.format("(%s-%s)", exp1, exp2);
    }
}
