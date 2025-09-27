package ru.nsu.vmarkidonov;

public abstract class Expression {
    public abstract double eval(String values);

    public abstract Expression derivative(String var);

    public abstract Expression clone();

    public abstract Expression simplify();
}
