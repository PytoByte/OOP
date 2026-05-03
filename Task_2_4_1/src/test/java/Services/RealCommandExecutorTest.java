package Services;

import Domain.CommandExecutor;
import org.junit.jupiter.api.Test;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class RealCommandExecutorTest {
    @Test
    void execute() {
        CommandExecutor executor = new RealCommandExecutor();
        List<String> output = new ArrayList<>();

        List<String> command = System.getProperty("os.name").toLowerCase().contains("win")
                ? List.of("cmd", "/c", "echo", "hello")
                : List.of("echo", "hello");

        boolean success = executor.execute(
                Path.of("."),
                command,
                "test-logger",
                output::add
        );

        assertTrue(success);
        assertTrue(output.contains("hello"));
    }
}