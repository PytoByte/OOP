package model;

/**
 * Exception thrown when test results cannot be parsed.
 */
public class TestResultsParseException extends RuntimeException {
    public TestResultsParseException(String message, Throwable cause) {
        super(message, cause);
    }
}
