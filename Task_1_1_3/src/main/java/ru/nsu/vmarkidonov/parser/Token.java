package ru.nsu.vmarkidonov.parser;

import ru.nsu.vmarkidonov.Expression;
import ru.nsu.vmarkidonov.exprparts.Add;
import ru.nsu.vmarkidonov.exprparts.Div;
import ru.nsu.vmarkidonov.exprparts.Mul;
import ru.nsu.vmarkidonov.exprparts.Number;
import ru.nsu.vmarkidonov.exprparts.Sub;
import ru.nsu.vmarkidonov.exprparts.Variable;

/**
 * Token for parser.
 */
class Token {
    public final TokenType type;
    public final String lexeme;
    private final Token[] params;
    private int paramsSize = 0;
    private final int pos;

    /**
     * Creates token from lexeme string.
     *
     * @param type type of token
     * @param lexeme substring, which represents the actual text of the token
     * @param pos start position of lexeme in string
     */
    public Token(TokenType type, String lexeme, int pos) {
        this.params = new Token[type.paramCount];
        this.type = type;
        this.lexeme = lexeme;
        this.pos = pos;
    }

    /**
     * Creates token from lexeme char.
     *
     * @param type type of token
     * @param lexeme substring, which represents the actual text of the token
     * @param pos start position of lexeme in string
     */
    public Token(TokenType type, char lexeme, int pos) {
        this.params = new Token[type.paramCount];
        this.type = type;
        this.lexeme = String.valueOf(lexeme);
        this.pos = pos;
    }

    /**
     * Pushes new parameter into parameters array.
     *
     * @param token any token
     */
    public void pushParam(Token token) {
        params[paramsSize++] = token;
    }

    /**
     * Pops parameter from parameters array.
     */
    public Token popParam() {
        return params[--paramsSize];
    }

    /**
     * Get last element of parameters array.
     *
     * @return last element of parameters array
     */
    public Token getLastParam() {
        return params[params.length - 1];
    }

    /**
     * Checks if this token is operator.
     *
     * @return true if operator, false overwise
     */
    public boolean isOperator() {
        return type.paramCount == 2;
    }

    /**
     * Checks if parameters array is full.
     *
     * @return true if parameters array is full, false overwise
     */
    public boolean isComplete() {
        return paramsSize == type.paramCount;
    }

    /**
     * Converts token tree into expression.
     *
     * @return expression, that token tree represents
     */
    public Expression toExpression() {
        Expression[] exprs = new Expression[type.paramCount];
        for (int i = 0; i < exprs.length; i++) {
            if (params[i] == null) {
                throw new ParserException(String.format("Expression incomplete: %s", this));
            }
            exprs[i] = params[i].toExpression();
        }
        return switch (type) {
            case ADD -> new Add(exprs[0], exprs[1]);
            case SUB -> new Sub(exprs[0], exprs[1]);
            case MUL -> new Mul(exprs[0], exprs[1]);
            case DIV -> new Div(exprs[0], exprs[1]);
            case SUBEXP -> exprs[0];
            case NUMBER -> new Number(Double.parseDouble(lexeme));
            case VARIABLE -> new Variable(lexeme);
            default -> null;
        };
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(
                String.format("%s(lexeme=\"%s\",pos=%d)", type, lexeme, pos)
        );
        if (params.length > 0) {
            sb.append("[");
            for (int i = 0; i < params.length; i++) {
                if (params[i] == null) {
                    sb.append("null");
                } else {
                    sb.append(String.format("%s", params[i]));
                }

                if (i < params.length - 1) {
                    sb.append(", ");
                }
            }
            sb.append("]");
        }
        return sb.toString();
    }
}
