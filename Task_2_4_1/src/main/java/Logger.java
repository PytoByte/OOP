public class Logger {
    private final String name;

    public Logger(String name) {
        this.name = name;
    }

    public void info(String format, Object... args) {
        String message = String.format(format, args);
        System.out.printf("[%s INFO] %s%n", name, message);
    }
}
