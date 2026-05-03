package Services;

import Model.CommandExecutor;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Consumer;

/**
 * Real command executor.
 */
public class RealCommandExecutor implements CommandExecutor {
    @Override
    public boolean execute(
            Path dir,
            List<String> cmd,
            String loggerName,
            Consumer<String> inspector
    ) {
        Logger logger = new Logger(loggerName);
        try {
            Process process = new ProcessBuilder(cmd)
                    .directory(dir.toFile())
                    .redirectErrorStream(true)
                    .start();

            try (BufferedReader r = new BufferedReader(
                    new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))
            ) {
                String line;
                while ((line = r.readLine()) != null) {
                    logger.info(line);
                    if (inspector != null) {
                        inspector.accept(line);
                    }
                }
            }

            process.waitFor();

            return process.exitValue() == 0;
        } catch (Exception e) {
            return false;
        }
    }
}
