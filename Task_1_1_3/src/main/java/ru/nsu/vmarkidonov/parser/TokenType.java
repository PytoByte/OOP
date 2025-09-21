package ru.nsu.vmarkidonov.parser;

public enum TokenType {
    ADD(2),
    DIV(1),
    MUL(1),
    NUMBER(3),
    SUB(2),
    VARIABLE(0),
    LBR(0),
    RBR(0);

    public final int priority;

    TokenType(int priority) {
        this.priority = priority;
    }

    public static boolean isBracket(char c) {
        return switch (c) {
            case '(', ')' -> true;
            default -> false;
        };
    }

    public static TokenType matchBracket(char c) {
        return switch (c) {
            case '(' -> LBR;
            case ')' -> RBR;
            default -> throw new RuntimeException("Can't match bracket");
        };
    }

    public static boolean isOperator(char c) {
        return switch (c) {
            case '+', '-', '*', '/' -> true;
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
