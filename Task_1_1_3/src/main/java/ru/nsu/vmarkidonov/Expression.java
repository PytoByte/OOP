package ru.nsu.vmarkidonov;

/**
 * The expression element or the expression itself.
 */
public abstract class Expression {
    /**
     * Calculates the value of an expression with variable assignment.
     *
     * @param values variable assignment in format "var1=value1;var2=value2"
     * @return calculation result
     */
    public abstract double eval(String values);

    /**
     * Calculates the derivative with respect to a variable.
     *
     * @param var variable with respect to which we are differentiating
     * @return a new expression that is a derivative
     */
    public abstract Expression derivative(String var);

    /**
     * Cloning the expression.
     *
     * @return clone of expression
     */
    public abstract Expression clone();

    /**
     * Simplifies the expression.
     *
     * @return simplified expression
     */
    public abstract Expression simplify();
}
