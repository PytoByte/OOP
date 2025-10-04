package ru.nsu.vmarkidonov.exprparts;

import ru.nsu.vmarkidonov.Expression;

import java.util.HashMap;

/**
 * Representation of the division operator.
 */
public class Div extends AsymmetricOperator {
    /**
     * Creates divide expression.
     *
     * @param exp1 first subexpression
     * @param exp2 second subexpression
     * @throws NullPointerException if exp1 or exp2 is null
     */
    public Div(Expression exp1, Expression exp2) {
        if (exp1 == null || exp2 == null) {
            throw new NullPointerException("Null subexpression");
        }
        this.exp1 = exp1;
        this.exp2 = exp2;
    }

    @Override
    public double eval(HashMap<String, Double> values) {
        if (exp2.eval(values) == 0) {
            throw new ArithmeticException("Division by zero");
        }
        return exp1.eval(values) / exp2.eval(values);
    }

    @Override
    public Expression derivative(String var) {
        return new Div(
                new Sub(
                        new Mul(exp1.derivative(var), exp2.clone()),
                        new Mul(exp1.clone(), exp2.derivative(var))
                ),
                new Mul(exp2.clone(), exp2.clone())
        );
    }

    @Override
    public Expression clone() {
        return new Div(exp1, exp2);
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
            }
        } else if (exp1S.getClass() == Variable.class && exp2S.getClass() == Number.class) {
            if (exp2S.eval("") == 0) {
                throw new RuntimeException("Division by zero");
            } else if (exp2S.eval("") == 1) {
                return exp1.clone();
            }
        }

        return new Div(exp1S, exp2S);
    }

    @Override
    public String toString() {
        return String.format("(%s/%s)", exp1, exp2);
    }
}
