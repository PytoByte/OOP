package ru.nsu.vmarkidonov.parser;

import ru.nsu.vmarkidonov.Expression;
import ru.nsu.vmarkidonov.exprparts.*;
import ru.nsu.vmarkidonov.exprparts.Number;

public class Token {
    public final TokenType type;
    public final String lexeme;
    public final Token[] params;
    private int paramsSize = 0;
    private final int pos;

    public Token(TokenType type, String lexeme, int pos) {
        this.params = new Token[type.paramCount];
        this.type = type;
        this.lexeme = lexeme;
        this.pos = pos;
    }

    public void pushParam(Token token) {
        params[paramsSize++] = token;
    }

    public Token popParam() {
        return params[--paramsSize];
    }

    public boolean isComplete() {
        return paramsSize == type.paramCount;
    }

    public Expression toExpression() {
        Expression[] exprs = new Expression[type.paramCount];
        for (int i = 0; i < exprs.length; i++) {
            if (params[i] == null) {
                throw new RuntimeException("Expression incomplete");
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
        StringBuilder sb = new StringBuilder(String.format("%s(lexeme=\"%s\",pos=%d)", type, lexeme, pos));
        if (params.length > 0) {
            sb.append("[");
            for (int i = 0; i < params.length; i++) {
                if (params[i] == null) {
                    sb.append("null");
                } else {
                    sb.append(String.format("%s", params[i]));
                }

                if (i < params.length-1) {
                    sb.append(", ");
                }
            }
            sb.append("]");
        }
        return sb.toString();
    }
}
