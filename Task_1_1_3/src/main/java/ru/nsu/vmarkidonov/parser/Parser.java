package ru.nsu.vmarkidonov.parser;

import java.util.ArrayList;
import java.util.Stack;
import ru.nsu.vmarkidonov.Expression;

/**
 * Expression parser from string.
 */
public class Parser {
    /**
     * Gets lexeme length from token.
     *
     * @param token any token
     * @return lexeme length
     */
    private static int getLexemeLength(Token token) {
        return token.lexeme.length();
    }

    /**
     * Gets bracket token from string.
     *
     * @param expString string that contains expression
     * @param lexStart index of expString, there lexeme starts
     * @return bracket token
     */
    private static Token readBracket(String expString, int lexStart) {
        TokenType brType = TokenType.matchBracket(expString.charAt(lexStart));
        return new Token(brType, expString.substring(lexStart, lexStart + 1), lexStart);
    }

    /**
     * Gets number token from string.
     *
     * @param expString string that contains expression
     * @param lexStart index of expString, there lexeme starts
     * @return number token
     */
    private static Token readNumber(String expString, int lexStart) {
        int lexEnd = lexStart;

        boolean seenDot = false;
        while (Character.isDigit(expString.charAt(lexEnd)) || expString.charAt(lexEnd) == '.') {
            if (expString.charAt(lexEnd) == '.') {
                if (seenDot) {
                    throw new ParserException("Unexpected \".\"", lexEnd);
                }
                seenDot = true;
            }
            lexEnd++;
            if (lexEnd >= expString.length()) break;
        }

        return new Token(TokenType.NUMBER, expString.substring(lexStart, lexEnd), lexStart);
    }

    /**
     * Gets operator token from string.
     *
     * @param expString string that contains expression
     * @param lexStart index of expString, there lexeme starts
     * @return operator token
     */
    private static Token readOperator(String expString, int lexStart) {
        TokenType operType = TokenType.matchOperator(expString.charAt(lexStart));
        return new Token(operType, expString.substring(lexStart, lexStart + 1), lexStart);
    }

    /**
     * Gets variable token from string.
     *
     * @param expString string that contains expression
     * @param lexStart index of expString, there lexeme starts
     * @return variable token
     */
    private static Token readVariable(String expString, int lexStart) {
        int lexEnd = lexStart;

        char curChar = expString.charAt(lexEnd);
        while (!TokenType.isOperator(curChar) && !TokenType.isBracket(curChar)) {
            lexEnd++;
            if (lexEnd >= expString.length()) {
                break;
            }
            curChar = expString.charAt(lexEnd);
        }

        return new Token(TokenType.VARIABLE, expString.substring(lexStart, lexEnd), lexStart);
    }

    /**
     * Makes array of tokens from expression string.
     *
     * @param expString string that contains expression
     * @return array of tokens
     * @throws ParserException if brackets unclosed or found unrecognizable character
     */
    private static Token[] tokenizer(String expString) {
        expString = expString.replaceAll("\\s", "");

        ArrayList<Token> tokens = new ArrayList<>();
        Stack<Token> brackets = new Stack<>();

        int i = 0;
        while (i < expString.length()) {
            char curChar = expString.charAt(i);

            if (TokenType.isBracket(curChar)) {
                Token token = readBracket(expString, i);
                tokens.add(token);

                if (token.type == TokenType.LBR) {
                    brackets.push(tokens.get(tokens.size() - 1));
                } else if (token.type == TokenType.RBR && brackets.empty()) {
                    throw new ParserException("Unexpected \")\"", i);
                } else {
                    brackets.pop();
                }
                i += getLexemeLength(token);
            } else if (Character.isDigit(curChar) || curChar == '.') {
                Token token = readNumber(expString, i);
                tokens.add(token);
                i += getLexemeLength(token);
            } else if (TokenType.isOperator(curChar)) {
                Token token = readOperator(expString, i);
                tokens.add(token);
                i += getLexemeLength(token);
            } else if (!TokenType.isOperator(curChar) && !TokenType.isBracket(curChar)) {
                Token token = readVariable(expString, i);
                tokens.add(token);
                i += getLexemeLength(token);
            } else {
                throw new ParserException(String.format("Unrecognized char \"%c\"", curChar));
            }
        }

        if (!brackets.empty()) {
            throw new ParserException(String.format("Unclosed brackets: %s", brackets));
        }

        return tokens.toArray(Token[]::new);
    }

    /**
     * Expression parser from string.
     *
     * @param expString string that contains expression
     * @return parsed expression
     * @throws ParserException if found mistake in expression string
     */
    public static Expression parseExpression(String expString) {
        Token[] tokens = tokenizer(expString);

        Stack<Token> tokenTreeStack = new Stack<>();
        tokenTreeStack.push(null);

        for (Token token : tokens) {
            if (token.type == TokenType.LBR) {
                tokenTreeStack.push(null);
            } else if (token.type == TokenType.RBR) {
                Token subToken = new Token(TokenType.SUBEXP, "", -1);
                subToken.pushParam(tokenTreeStack.pop());
                Token currentToken = tokenTreeStack.pop();

                if (currentToken == null) {
                    tokenTreeStack.push(subToken);
                    continue;
                }

                Token iterToken = currentToken;
                while (iterToken.type.paramCount == 2
                        && iterToken.params[iterToken.type.paramCount - 1] != null
                ) {
                    iterToken = iterToken.params[iterToken.type.paramCount - 1];
                }
                if (iterToken.type.paramCount != 2) {
                    throw new ParserException("Subexpression outside operator", subToken);
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
                    while (iterToken.type.paramCount == 2 && iterToken.params[iterToken.type.paramCount - 1] != null) {
                        iterToken = iterToken.params[iterToken.type.paramCount - 1];
                    }
                    if (iterToken.type.paramCount != 2) {
                        throw new ParserException("Subexpression outside operator", token);
                    }
                    iterToken.pushParam(token);
                } else if (currentToken.type.priority <= token.type.priority) {
                    token.pushParam(currentToken);
                    currentToken = token;
                } else {
                    Token iterToken = currentToken;
                    while (iterToken.type.paramCount == 2 && iterToken.params[iterToken.type.paramCount - 1].type.paramCount > 1) {
                        iterToken = iterToken.params[iterToken.type.paramCount - 1];
                    }
                    if (iterToken.type.paramCount != 2) {
                        throw new ParserException("Subexpression outside operator", token);
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
