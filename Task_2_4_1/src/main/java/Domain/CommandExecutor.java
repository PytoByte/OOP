package Domain;

import java.nio.file.Path;
import java.util.List;
import java.util.function.Consumer;

public interface CommandExecutor {
    boolean execute(Path dir, List<String> cmd, String loggerName, Consumer<String> inspector);

    default boolean execute(Path dir, List<String> cmd, String loggerName) {
        return execute(dir, cmd, loggerName, null);
    }
}
