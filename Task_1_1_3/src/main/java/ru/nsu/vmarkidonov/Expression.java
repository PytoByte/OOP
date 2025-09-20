package ru.nsu.vmarkidonov;

public abstract class Expression {
    public abstract int eval(String values);

    public abstract Expression derivative(String var);

    public abstract Expression clone();
}
