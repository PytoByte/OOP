package ru.nsu.vmarkidonov.exprparts;

import java.util.NoSuchElementException;
import ru.nsu.vmarkidonov.Expression;

/**
 * Representation of the variable.
 */
public class Variable extends Expression {
    public final String var;

    /**
     * Creates variable.
     *
     * @param var variable name
     * @throws NullPointerException if variable name is null
     */
    public Variable(String var) {
        if (var == null) {
            throw new NullPointerException("Null variable name");
        }
        this.var = var;
    }

    @Override
    public double eval(String values) {
        String[] valuesArray = values.replaceAll("\\s", "").split(";");
        for (String pair : valuesArray) {
            String[] pairArray = pair.split("=");
            if (pairArray[0].strip().equals(var)) {
                return Double.parseDouble(pairArray[1]);
            }
        }
        throw new NoSuchElementException(String.format("Value for variable %s not found", var));
    }

    @Override
    public Expression derivative(String var) {
        if (this.var.equals(var.replaceAll("\\s", ""))) {
            return new Number(0);
        }
        return clone();
    }

    @Override
    public Expression clone() {
        return new Variable(var);
    }

    @Override
    public Expression simplify() {
        return this.clone();
    }

    @Override
    public String toString() {
        return var;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Variable other = (Variable) obj;

        return other.var.equals(var);
    }
}
