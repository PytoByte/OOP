package ru.nsu.vmarkidonov.exprparts;

import ru.nsu.vmarkidonov.Expression;

import java.util.Objects;

/**
 * Representation of the add operator.
 */
public class Add extends Expression {
    public Expression exp1;
    public Expression exp2;

    public Add(Expression exp1, Expression exp2) {
        this.exp1 = exp1;
        this.exp2 = exp2;
    }

    @Override
    public double eval(String values) {
        return exp1.eval(values)+exp2.eval(values);
    }

    @Override
    public Expression derivative(String var) {
        return new Add(exp1.derivative(var), exp2.derivative(var));
    }

    @Override
    public Expression clone() {
        return new Add(exp1, exp2);
    }

    @Override
    public Expression simplify() {
        Expression exp1S = exp1.simplify();
        Expression exp2S = exp2.simplify();

        if (exp1S.getClass() == Number.class && exp2S.getClass() == Number.class) {
            return new Number(eval(""));
        }

        return new Add(exp1S, exp2S);
    }

    @Override
    public String toString() {
        return String.format("(%s+%s)", exp1, exp2);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Add other = (Add) obj;

        return (exp1.equals(other.exp1) && exp2.equals(other.exp2)) ||
                (exp1.equals(other.exp2) && exp2.equals(other.exp1));
    }
}
