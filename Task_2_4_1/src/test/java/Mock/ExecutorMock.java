package Mock;

import Model.CommandExecutor;

import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Mock executor which can return commands it receives.
 */
public class ExecutorMock implements CommandExecutor {
    public final List<ExecutionCall> calls = new LinkedList<>();
    public boolean nextResult = true;
    public String outputToInject = null;

    @Override
    public boolean execute(Path dir, List<String> cmd, String loggerName, Consumer<String> inspector) {
        calls.add(new ExecutionCall(dir, cmd, loggerName));
        if (inspector != null && outputToInject != null) {
            inspector.accept(outputToInject);
        }
        return nextResult;
    }

    /**
     * Received command model.
     *
     * @param dir path where command will be executed
     * @param cmd command
     * @param loggerName logger name
     */
    public record ExecutionCall(Path dir, List<String> cmd, String loggerName) {}
}