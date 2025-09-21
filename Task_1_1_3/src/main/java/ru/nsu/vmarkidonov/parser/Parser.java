package ru.nsu.vmarkidonov.parser;

import ru.nsu.vmarkidonov.Expression;
import ru.nsu.vmarkidonov.exprparts.Number;

import java.text.ParseException;
import java.util.Stack;

public class Parser {
    public static Expression parseExpression(String expString){
        Stack<Token> tokenStack = new Stack<>();
        for (int i = 0; i < expString.length(); i++) {
            if (Character.isWhitespace(expString.charAt(i))) {
                continue;
            }

            int lexStart = i;

            System.out.println("parse number");

            boolean seenDot = false;
            while (Character.isDigit(expString.charAt(i)) || expString.charAt(i)=='.') {
                if (expString.charAt(i)=='.') {
                    if (seenDot) {
                        throw new RuntimeException("Unexpected . while parsing number");
                    }
                    seenDot = true;
                }
                i++;
                if (i >= expString.length()) {
                    break;
                }
            }
            if (lexStart != i) {
                tokenStack.push(new Token(TokenType.NUMBER, expString.substring(lexStart, i)));
                i--;
                continue;
            }

            System.out.println("parse operator");

            boolean seenOperator = false;
            while (TokenType.isOperator(expString.charAt(i))) {
                if (seenOperator) {
                    throw new RuntimeException("Unexpected operator while parsing expression");
                }
                seenOperator = true;
                i++;
                if (i >= expString.length()) {
                    break;
                }
            }
            if (lexStart != i) {
                tokenStack.push(
                        new Token(
                                TokenType.matchOperator(expString.charAt(lexStart)),
                                expString.substring(lexStart, i)
                        )
                );
                i--;
                continue;
            }

            System.out.println("parse variable");

            while (expString.charAt(i) != '+' && !Character.isWhitespace(expString.charAt(i))) {
                i++;
                if (i >= expString.length()) {
                    break;
                }
            }
            if (lexStart != i) {
                tokenStack.push(new Token(TokenType.VARIABLE, expString.substring(lexStart, i)));
                i--;
                continue;
            }
        }

        for (Token token : tokenStack) {
            System.out.printf("%s %s\n", token.type().toString(), token.lexeme());
        }

        return new Number(0);
    }
}
