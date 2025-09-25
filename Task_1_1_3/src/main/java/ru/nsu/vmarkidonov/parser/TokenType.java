package ru.nsu.vmarkidonov.parser;

public enum TokenType {
    ADD(2, 2),
    DIV(1, 2),
    MUL(1, 2),
    NUMBER(3, 0),
    SUB(2, 2),
    VARIABLE(0, 0),
    LBR(0, 0),
    RBR(0, 0),
    SUBEXP(0, 1);

    public final int priority;
    public final int paramCount;

    TokenType(int priority, int paramCount) {
        this.priority = priority;
        this.paramCount = paramCount;
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
