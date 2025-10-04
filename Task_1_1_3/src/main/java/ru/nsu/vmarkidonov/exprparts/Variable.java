package ru.nsu.vmarkidonov.exprparts;

import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.regex.PatternSyntaxException;

import ru.nsu.vmarkidonov.Expression;
import ru.nsu.vmarkidonov.parser.ParserException;

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
    public double eval(HashMap<String, Double> values) {
        if (!values.containsKey(var)) {
            throw new NoSuchElementException(String.format("Value for variable %s not found", var));
        }

        return values.get(var);
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
    public int hashCode() {
        return var.hashCode();
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
