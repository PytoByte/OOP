package ru.nsu.vmarkidonov.parser;

import ru.nsu.vmarkidonov.Expression;

import java.util.Stack;

public class Parser {
    public static Expression parseExpression(String expString) {
        Stack<Token> tokenStack = new Stack<>();
        int openBracketsCount = 0;
        for (int i = 0; i < expString.length(); i++) {
            if (Character.isWhitespace(expString.charAt(i))) {
                continue;
            }

            int lexStart = i;

            if (TokenType.isBracket(expString.charAt(i))) {
                TokenType brType = TokenType.matchBracket(expString.charAt(i));
                if (brType == TokenType.LBR) {
                    openBracketsCount++;
                } else if (brType == TokenType.RBR) {
                    if (--openBracketsCount < 0) {
                        throw new RuntimeException("Unexpected \")\"");
                    }
                }

                tokenStack.push(
                        new Token(
                                TokenType.matchBracket(expString.charAt(i)),
                                expString.substring(lexStart, i + 1),
                                lexStart
                        )
                );
                continue;
            }

            boolean seenDot = false;
            while (Character.isDigit(expString.charAt(i)) ||
                    expString.charAt(i) == '.' ||
                    expString.charAt(i) == '-') {
                if (expString.charAt(i) == '-' && !tokenStack.isEmpty()) {
                    if (tokenStack.peek().type == TokenType.RBR ||
                            tokenStack.peek().type == TokenType.NUMBER ||
                            tokenStack.peek().type == TokenType.VARIABLE) {
                        break;
                    }
                }

                if (expString.charAt(i) == '.') {
                    if (seenDot) {
                        throw new RuntimeException(
                                String.format("Unexpected \".\" at pos %d", i)
                        );
                    }
                    seenDot = true;
                }

                if (expString.charAt(i) == '-' && lexStart != i) {
                    if (expString.charAt(lexStart) == '-') {
                        throw new RuntimeException(
                                String.format("Unexpected \"-\" at pos %d", i)
                        );
                    }
                    break;
                }

                i++;
                if (i >= expString.length()) {
                    break;
                }
            }
            if (lexStart != i) {
                tokenStack.push(
                        new Token(
                                TokenType.NUMBER,
                                expString.substring(lexStart, i),
                                lexStart
                        )
                );
                i--;
                continue;
            }

            boolean seenOperator = false;
            while (TokenType.isOperator(expString.charAt(i))) {
                if (seenOperator) {
                    throw new RuntimeException(
                            String.format("Unexpected operator at pos %d", i)
                    );
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
                                expString.substring(lexStart, i),
                                lexStart
                        )
                );
                i--;
                continue;
            }

            while (
                    !TokenType.isOperator(expString.charAt(i)) &&
                            !Character.isWhitespace(expString.charAt(i)) &&
                            !TokenType.isBracket(expString.charAt(i))
            ) {
                i++;
                if (i >= expString.length()) {
                    break;
                }
            }
            if (lexStart != i) {
                tokenStack.push(
                        new Token(
                                TokenType.VARIABLE,
                                expString.substring(lexStart, i),
                                lexStart
                        )
                );
                i--;
            }
        }

        if (openBracketsCount > 0) {
            throw new RuntimeException("Not all brackets are closed");
        }

        for (Token token : tokenStack) {
            System.out.println(token);
        }

        Stack<Token> tokenTreeStack = new Stack<>();
        tokenTreeStack.push(null);

        for (Token token : tokenStack) {
            if (token.type == TokenType.LBR) {
                tokenTreeStack.push(null);
            } else if (token.type == TokenType.RBR) {
                Token subToken = new Token(
                        TokenType.SUBEXP,
                        "",
                        -1
                );
                subToken.pushParam(tokenTreeStack.pop());
                Token currentToken = tokenTreeStack.pop();

                if (currentToken == null) {
                    tokenTreeStack.push(subToken);
                    continue;
                }

                Token iterToken = currentToken;
                while (iterToken.params[iterToken.type.paramCount - 1] != null) {
                    iterToken = iterToken.params[iterToken.type.paramCount - 1];
                }
                iterToken.pushParam(subToken);
                tokenTreeStack.push(currentToken);
            } else {
                Token currentToken = tokenTreeStack.pop();

                if (currentToken == null) {
                    currentToken = token;
                } else if (currentToken.type.paramCount == 0) {
                    token.pushParam(currentToken);
                    currentToken = token;
                } else if (token.type.paramCount == 0) {
                    Token iterToken = currentToken;
                    while (iterToken.params[iterToken.type.paramCount - 1] != null) {
                        iterToken = iterToken.params[iterToken.type.paramCount - 1];
                    }
                    iterToken.pushParam(token);
                } else if (currentToken.type.priority <= token.type.priority) {
                    token.pushParam(currentToken);
                    currentToken = token;
                } else {
                    Token iterToken = currentToken;
                    while (iterToken.params[iterToken.type.paramCount - 1].type.paramCount > 1) {
                        iterToken = iterToken.params[iterToken.type.paramCount - 1];
                    }
                    Token secOp = iterToken.popParam();
                    token.pushParam(secOp);
                    iterToken.pushParam(token);
                }

                tokenTreeStack.push(currentToken);
            }
        }
        return tokenTreeStack.pop().toExpression();
    }
}
