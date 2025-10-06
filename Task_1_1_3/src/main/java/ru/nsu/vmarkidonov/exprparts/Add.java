package ru.nsu.vmarkidonov.exprparts;

import java.util.HashMap;
import ru.nsu.vmarkidonov.Expression;

/**
 * Representation of the add operator.
 */
public class Add extends SymmetricOperator {
    /**
     * Creates add expression.
     *
     * @param exp1 first subexpression
     * @param exp2 second subexpression
     * @throws NullPointerException if exp1 or exp2 is null
     */
    public Add(Expression exp1, Expression exp2) {
        if (exp1 == null || exp2 == null) {
            throw new NullPointerException("Null subexpression");
        }
        this.exp1 = exp1;
        this.exp2 = exp2;
    }

    @Override
    public double eval(HashMap<String, Double> values) {
        return exp1.eval(values) + exp2.eval(values);
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

        Expression addS = new Add(exp1S, exp2S);

        if (exp1S.getClass() == Number.class && exp2S.getClass() == Number.class) {
            return new Number(addS.eval(""));
        } else if (exp1S.getClass() == Number.class) {
            if (exp1S.equals(Number.zero)) {
                return exp2S.clone();
            }
        } else if (exp2S.getClass() == Number.class) {
            if (exp2S.equals(Number.zero)) {
                return exp1S.clone();
            }
        }

        return addS;
    }

    @Override
    public String toString() {
        return String.format("(%s+%s)", exp1, exp2);
    }
}
