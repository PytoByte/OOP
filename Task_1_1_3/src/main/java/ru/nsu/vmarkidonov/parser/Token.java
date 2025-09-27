package ru.nsu.vmarkidonov.parser;

import ru.nsu.vmarkidonov.Expression;
import ru.nsu.vmarkidonov.exprparts.*;
import ru.nsu.vmarkidonov.exprparts.Number;

import java.util.ArrayList;
import java.util.LinkedList;

public class Token {
    public final TokenType type;
    public final String lexeme;
    public final Token[] params;
    private int paramsSize = 0;

    public Token(TokenType type, String lexeme) {
        this.params = new Token[type.paramCount];
        this.type = type;
        this.lexeme = lexeme;
    }

    public void push(Token token) {
        params[paramsSize++] = token;
    }

    public Token pop() {
        return params[--paramsSize];
    }

    public boolean isComplete() {
        return paramsSize == type.paramCount;
    }

    public Expression toExpression() {
        Expression[] exps = new Expression[type.paramCount];
        for (int i = 0; i < exps.length; i++) {
            exps[i] = params[i].toExpression();
        }
        return switch (type) {
            case ADD -> new Add(exps[0], exps[1]);
            case SUB -> new Sub(exps[0], exps[1]);
            case MUL -> new Mul(exps[0], exps[1]);
            case DIV -> new Div(exps[0], exps[1]);
            case SUBEXP -> exps[0];
            case NUMBER -> new Number(Double.parseDouble(lexeme));
            case VARIABLE -> new Variable(lexeme);
            default -> null;
        };
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(String.format("%s[%s] (", type, lexeme));
        for (Token token : params) {
            if (token == null) {
                sb.append("null ");
            } else {
                sb.append(String.format("%s ", token.toString()));
            }
        }
        sb.append(")");
        return sb.toString();
    }
}
