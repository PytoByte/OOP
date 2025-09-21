package ru.nsu.vmarkidonov.parser;

public enum TokenType {
    ADD,
    DIV,
    MUL,
    NUMBER,
    SUB,
    VARIABLE,
    LBR,
    RBR;

    public static boolean isOperator(char c) {
        return switch (c) {
            case '+', '-', '*', '/'-> true;
            default -> false;
        };
    }

    public static TokenType matchOperator(char c) {
        return switch (c) {
            case '+' -> ADD;
            case '-' -> SUB;
            case '*' -> MUL;
            case '/' -> DIV;
            default -> throw new RuntimeException("Can't match operator");
        };
    }
}
