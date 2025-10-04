package ru.nsu.vmarkidonov;

import java.util.HashMap;
import java.util.NoSuchElementException;

/**
 * The expression element or the expression itself.
 */
public abstract class Expression {
    /**
     * Calculates the value of an expression with variable assignment.
     *
     * @param values variable assignment
     * @return calculation result
     * @throws NoSuchElementException if one of variables has no value
     * @throws NumberFormatException  if assignment not a number
     * @throws IllegalStateException  if variable assignment not in correct format
     */
    public abstract double eval(HashMap<String, Double> values);

    /**
     * Calculates the value of an expression with variable assignment.
     * Parses values from string.
     *
     * @param values variable assignment in format "var1=value1;var2=value2"
     * @return calculation result
     * @throws NoSuchElementException if one of variables has no value
     * @throws NumberFormatException  if assignment not a number
     * @throws IllegalStateException  if variable assignment not in correct format
     */
    public double eval(String values) {
        HashMap<String, Double> valuesMap = new HashMap<>();

        if (values.isBlank()) {
            return eval(valuesMap);
        }

        String[] valuesArray = values.replaceAll("\\s", "").split(";");
        for (String pair : valuesArray) {
            String[] pairArray = pair.split("=");
            if (pairArray.length != 2) {
                throw new IllegalStateException("Wrong format of variable assignment");
            }
            valuesMap.put(pairArray[0].strip(), Double.parseDouble(pairArray[1]));
        }

        return eval(valuesMap);
    }

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
