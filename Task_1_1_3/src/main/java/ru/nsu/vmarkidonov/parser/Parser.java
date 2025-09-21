package ru.nsu.vmarkidonov.parser;

import ru.nsu.vmarkidonov.Expression;
import ru.nsu.vmarkidonov.exprparts.*;
import ru.nsu.vmarkidonov.exprparts.Number;

import java.text.ParseException;
import java.util.Stack;

public class Parser {
    public static Expression parseExpression(String expString) {
        Stack<Token> tokenStack = new Stack<>();
        for (int i = 0; i < expString.length(); i++) {
            if (Character.isWhitespace(expString.charAt(i))) {
                continue;
            }

            int lexStart = i;

            System.out.println("parse br-s");
            if (TokenType.isBracket(expString.charAt(i))) {
                tokenStack.push(new Token(
                        TokenType.matchBracket(expString.charAt(i)),
                        expString.substring(lexStart, i+1))
                );
                continue;
            }

            System.out.println("parse number");
            boolean seenDot = false;
            while (Character.isDigit(expString.charAt(i)) || expString.charAt(i) == '.') {
                if (expString.charAt(i) == '.') {
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

            while (
                    expString.charAt(i) != '+' &&
                            !Character.isWhitespace(expString.charAt(i)) &&
                            !TokenType.isBracket(expString.charAt(i))
            ) {
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

        Stack<Expression> exprStack = new Stack<>();
        exprStack.push(null);

        TokenType lastOperator = TokenType.LBR;
        for (Token token : tokenStack) {
            System.out.println(exprStack.peek());
            if (token.type() == TokenType.LBR) {
                exprStack.push(null);
                continue;
            } else if (token.type() == TokenType.RBR) {
                Expression expr = exprStack.pop();
                Expression currentExpr = exprStack.pop();
                Operator oper = (Operator) currentExpr;
                oper.exp2 = expr;
                exprStack.push(currentExpr);
                continue;
            } else {
                Expression currentExpr = exprStack.pop();
                Expression newExpr = null;

                if (currentExpr == null) {
                    if (token.type() == TokenType.NUMBER) {
                        newExpr = new Number(Integer.parseInt(token.lexeme()));
                    } else if (token.type() == TokenType.VARIABLE) {
                        newExpr = new Variable(token.lexeme());
                    }
                    exprStack.push(newExpr);
                    continue;
                } else if (lastOperator.priority <= token.type().priority) {
                    if (token.type() == TokenType.NUMBER) {
                        Operator oper = (Operator) currentExpr;
                        while (oper.exp2 != null) {
                            oper = (Operator) oper.exp2;
                        }

                        oper.exp2 = new Number(Integer.parseInt(token.lexeme()));

                        newExpr = currentExpr;
                    } else if (token.type() == TokenType.VARIABLE) {
                        Operator oper = (Operator) currentExpr;
                        while (oper.exp2 != null) {
                            oper = (Operator) oper.exp2;
                        }

                        oper.exp2 = new Variable(token.lexeme());

                        newExpr = currentExpr;
                    } else if (token.type() == TokenType.ADD) {
                        newExpr = new Add(currentExpr, null);
                        lastOperator = token.type();
                    } else if (token.type() == TokenType.SUB) {
                        newExpr = new Sub(currentExpr, null);
                        lastOperator = token.type();
                    } else if (token.type() == TokenType.MUL) {
                        newExpr = new Mul(currentExpr, null);
                        lastOperator = token.type();
                    } else if (token.type() == TokenType.DIV) {
                        newExpr = new Div(currentExpr, null);
                        lastOperator = token.type();
                    }

                    exprStack.push(newExpr);
                    continue;
                } else {
                    if (token.type().priority == 0) {
                        Operator oper = (Operator) currentExpr;
                        while (oper.exp2 != null) {
                            oper = (Operator) oper.exp2;
                        }

                        if (token.type() == TokenType.NUMBER) {
                            newExpr = new Number(Integer.parseInt(token.lexeme()));
                        } else if (token.type() == TokenType.VARIABLE) {
                            newExpr = new Variable(token.lexeme());
                        }

                        oper.exp2 = newExpr;

                        exprStack.push(currentExpr);
                    } else {
                        Operator oper = (Operator) currentExpr;
                        if (token.type() == TokenType.ADD) {
                            newExpr = new Sub(oper.exp2, null);
                        } else if (token.type() == TokenType.SUB) {
                            newExpr = new Sub(oper.exp2, null);
                        } else if (token.type() == TokenType.MUL) {
                            newExpr = new Mul(oper.exp2, null);
                        } else if (token.type() == TokenType.DIV) {
                            newExpr = new Div(oper.exp2, null);
                        }

                        oper.exp2 = newExpr;

                        exprStack.push(currentExpr);
                    }
                }

                exprStack.push(currentExpr);
            }
        }

        return exprStack.pop();
    }
}
