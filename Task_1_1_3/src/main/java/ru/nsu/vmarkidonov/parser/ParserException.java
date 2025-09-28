package ru.nsu.vmarkidonov.parser;

/**
 * Exception for parser.
 */
public class ParserException extends RuntimeException {
    public ParserException(String message, Token token) {
        super(String.format("%s: %s", message, token));
    }

    public ParserException(String message) {
        super(message);
    }
}
