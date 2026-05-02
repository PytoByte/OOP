public class Logger {
    private final String name;

    public Logger(String name) {
        this.name = name;
    }

    public void info(String format, Object... args) {
        String message = String.format(format, args);
        System.out.printf("[%s] %s%n", name, message);
    }

    public void error(String format, Object... args) {
        String message = String.format(format, args);
        System.err.printf("[%s] %s%n", name, message);
    }
}
