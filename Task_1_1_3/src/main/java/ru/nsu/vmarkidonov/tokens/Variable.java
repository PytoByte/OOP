package ru.nsu.vmarkidonov.tokens;

import ru.nsu.vmarkidonov.Expression;

import java.util.NoSuchElementException;

public class Variable extends Expression {
    private final String var;

    public Variable(String var) {
        this.var = var;
    }

    @Override
    public int eval(String values) {
        String[] valuesArray = values.replaceAll("\\s", "").split(";");
        for (String pair : valuesArray) {
            String[] pairArray = pair.split("=");
            if (pairArray[0].strip().equals(var)) {
                return Integer.parseInt(pairArray[1]);
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
    public String toString() {
        return var;
    }
}
