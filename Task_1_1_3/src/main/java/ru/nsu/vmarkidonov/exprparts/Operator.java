package ru.nsu.vmarkidonov.exprparts;

import ru.nsu.vmarkidonov.Expression;

/**
 * Representation of an operator.
 */
public abstract class Operator extends Expression {
    protected Expression exp1;
    protected Expression exp2;
}
