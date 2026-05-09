package model;

import java.nio.file.Path;
import java.util.List;
import java.util.function.Consumer;

/**
 * Executor of terminal commands.
 */
public interface CommandExecutor {
    /**
     * Execute command and print output using named logger with line inspection.
     *
     * @param dir path where command will be executed
     * @param cmd command
     * @param loggerName logger name
     * @param inspector command output line consumer
     * @return true is executed successfully, false overwise
     */
    boolean execute(Path dir, List<String> cmd, String loggerName, Consumer<String> inspector);

    /**
     * Execute command and print output using named logger.
     *
     * @param dir path where command will be executed
     * @param cmd command
     * @param loggerName logger name
     * @return true is executed successfully, false overwise
     */
    default boolean execute(Path dir, List<String> cmd, String loggerName) {
        return execute(dir, cmd, loggerName, null);
    }
}
