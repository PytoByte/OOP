package services;

/**
 * Named logger.
 */
public class Logger {
    private final String name;

    /**
     * Default constructor.
     *
     * @param name logger name
     */
    public Logger(String name) {
        this.name = name;
    }

    /**
     * Print info log with formatting.
     *
     * @param format format string
     * @param args args for format string
     */
    public void info(String format, Object... args) {
        String message = String.format(format, args);
        System.out.printf("(INFO) [%s] %s\n", name, message);
    }

    /**
     * Print error log with formatting.
     *
     * @param format format string
     * @param args args for format string
     */
    public void error(String format, Object... args) {
        String message = String.format(format, args);
        System.err.printf("(ERROR) [%s] %s\n", name, message);
    }
}
