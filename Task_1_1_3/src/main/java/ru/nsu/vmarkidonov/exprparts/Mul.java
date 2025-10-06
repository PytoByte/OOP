package ru.nsu.vmarkidonov.exprparts;

import java.util.HashMap;
import ru.nsu.vmarkidonov.Expression;

/**
 * Representation of the multiplication operator.
 */
public class Mul extends SymmetricOperator {
    /**
     * Creates divide expression.
     *
     * @param exp1 first subexpression
     * @param exp2 second subexpression
     * @throws NullPointerException if exp1 or exp2 is null
     */
    public Mul(Expression exp1, Expression exp2) {
        if (exp1 == null || exp2 == null) {
            throw new NullPointerException("Null subexpression");
        }
        this.exp1 = exp1;
        this.exp2 = exp2;
    }

    @Override
    public double eval(HashMap<String, Double> values) {
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
        } else if (exp1S.getClass() == Number.class) {
            if (exp1S.eval("") == 0) {
                return new Number(0);
            } else if (exp1S.eval("") == 1) {
                return exp2S.clone();
            }
        } else if (exp2S.getClass() == Number.class) {
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
}
