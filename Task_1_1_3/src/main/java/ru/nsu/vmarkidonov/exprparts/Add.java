package ru.nsu.vmarkidonov.exprparts;

import ru.nsu.vmarkidonov.Expression;
import ru.nsu.vmarkidonov.parser.ParserException;

import java.util.HashMap;
import java.util.NoSuchElementException;

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

        if (exp1S.getClass() == Number.class && exp2S.getClass() == Number.class) {
            return new Number(eval(""));
        }

        return new Add(exp1S, exp2S);
    }

    @Override
    public String toString() {
        return String.format("(%s+%s)", exp1, exp2);
    }
}
