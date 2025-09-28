package ru.nsu.vmarkidonov.exprparts;

import ru.nsu.vmarkidonov.Expression;

/**
 * Representation of the multiplication operator.
 */
public class Mul extends Expression {
    public Expression exp1;
    public Expression exp2;

    public Mul(Expression exp1, Expression exp2) {
        this.exp1 = exp1;
        this.exp2 = exp2;
    }

    @Override
    public double eval(String values) {
        return exp1.eval(values) * exp2.eval(values);
    }

    @Override
    public Expression derivative(String var) {
        return new Add(
                new Mul(exp1.derivative(var), exp2.clone()),
                new Mul(exp1.clone(), exp2.derivative(var))
        );
    }

    @Override
    public Expression clone() {
        return new Mul(exp1, exp2);
    }

    @Override
    public Expression simplify() {
        Expression exp1S = exp1.simplify();
        Expression exp2S = exp2.simplify();

        if (exp1S.getClass() == Number.class && exp2S.getClass() == Number.class) {
            return new Number(eval(""));
        } else if (exp1S.getClass() == Number.class && exp2S.getClass() == Variable.class) {
            if (exp1S.eval("") == 0) {
                return new Number(0);
            } else if (exp1S.eval("") == 1) {
                return exp2S.clone();
            }
        } else if (exp1S.getClass() == Variable.class && exp2S.getClass() == Number.class) {
            if (exp2S.eval("") == 0) {
                return new Number(0);
            } else if (exp2S.eval("") == 1) {
                return exp1S.clone();
            }
        }

        return new Mul(exp1S, exp2S);
    }

    @Override
    public String toString() {
        return String.format("(%s*%s)", exp1, exp2);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Mul other = (Mul) obj;

        return (exp1.equals(other.exp1) && exp2.equals(other.exp2))
                || (exp1.equals(other.exp2) && exp2.equals(other.exp1));
    }
}
