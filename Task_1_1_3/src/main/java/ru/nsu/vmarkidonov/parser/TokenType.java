package ru.nsu.vmarkidonov.parser;

/**
 * Contains types of tokens.
 */
public enum TokenType {
    ADD(2, 2),
    DIV(1, 2),
    MUL(1, 2),
    NUMBER(3, 0),
    SUB(2, 2),
    VARIABLE(0, 0),
    LBR(0, 0),
    RBR(0, 0),
    SUBEXP(0, 1),
    ERROR(0, 0);

    public final int priority;
    public final int paramCount;

    TokenType(int priority, int paramCount) {
        this.priority = priority;
        this.paramCount = paramCount;
    }

    /**
     * Checks if character is bracket.
     *
     * @param c any character
     * @return true if character is bracket, false overwise
     */
    public static boolean isBracket(char c) {
        return switch (c) {
            case '(', ')' -> true;
            default -> false;
        };
    }

    /**
     * Matches bracket character with it's type.
     *
     * @param c bracket character
     * @return type of bracket
     * @throws RuntimeException if character isn't bracket
     */
    public static TokenType matchBracket(char c) {
        return switch (c) {
            case '(' -> LBR;
            case ')' -> RBR;
            default -> throw new RuntimeException(String.format("Can't match bracket \"%c\"", c));
        };
    }

    /**
     * Checks if character is operator.
     *
     * @param c any character
     * @return true if character is operator, false overwise
     */
    public static boolean isOperator(char c) {
        return switch (c) {
            case '+', '-', '*', '/' -> true;
            default -> false;
        };
    }

    /**
     * Matches operator character with it's type.
     *
     * @param c operator character
     * @return type of operator
     * @throws RuntimeException if character isn't operator
     */
    public static TokenType matchOperator(char c) {
        return switch (c) {
            case '+' -> ADD;
            case '-' -> SUB;
            case '*' -> MUL;
            case '/' -> DIV;
            default -> throw new RuntimeException(String.format("Can't match operator \"%c\"", c));
        };
    }
}
