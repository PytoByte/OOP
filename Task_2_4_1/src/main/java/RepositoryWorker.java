import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Utility class for managing student repositories and Gradle tasks.
 */
public class RepositoryWorker {

    public static Path cloneRepository(String repoUrl, String branch, Path workDir) {
        try {
            if (!Files.exists(workDir)) {
                Files.createDirectories(workDir);
            }

            // Clone into the workDir directly
            List<String> command = List.of(
                    "git", "clone", "--branch", branch, "--depth", "1", repoUrl, "."
            );

            ProcessBuilder pb = new ProcessBuilder(command);
            pb.directory(workDir.toFile());
            pb.redirectErrorStream(true);

            System.out.println("[INFO] Cloning: " + repoUrl + " (branch: " + branch + ")");
            Process process = pb.start();

            // Read output in a separate thread to prevent buffer overflow
            Thread outputThread = new Thread(() -> printStream(process.getInputStream(), "[git]"));
            outputThread.start();

            if (!process.waitFor(5, TimeUnit.MINUTES)) {
                process.destroyForcibly();
                System.err.println("[ERROR] git clone timed out");
                return null;
            }

            if (process.exitValue() != 0) {
                System.err.println("[ERROR] git clone failed with exit code: " + process.exitValue());
                return null;
            }

            outputThread.join(2000);
            return workDir;

        } catch (IOException | InterruptedException e) {
            System.err.println("[ERROR] Cloning failed: " + e.getMessage());
            Thread.currentThread().interrupt();
            return null;
        }
    }

    public static boolean compileProject(Path projectDir) {
        // 'classes' and 'testClasses' ensures everything is compiled without running tests
        return runGradleTask(projectDir, "classes", "testClasses");
    }

    public static TestResults runTests(Path projectDir) {
        boolean success = runGradleTask(projectDir, "test");
        if (!success) {
            System.err.println("[WARN] Gradle 'test' task failed or returned non-zero");
        }
        return parseTestResults(projectDir);
    }

    public static boolean generateDocumentation(Path projectDir) {
        return runGradleTask(projectDir, "javadoc");
    }

    public static boolean checkCodeStyle(Path projectDir) {
        return runGradleTask(projectDir, "checkstyleMain", "checkstyleTest");
    }

    private static boolean runGradleTask(Path projectDir, String... args) {
        try {
            boolean isWindows = System.getProperty("os.name").toLowerCase().contains("win");
            Path gradlew = projectDir.resolve(isWindows ? "gradlew.bat" : "gradlew");

            if (!Files.exists(gradlew)) {
                System.err.println("[ERROR] gradlew not found at: " + gradlew.toAbsolutePath());
                return false;
            }

            if (!isWindows) {
                try {
                    Files.setPosixFilePermissions(gradlew, PosixFilePermissions.fromString("rwxr-xr-x"));
                } catch (IOException e) {
                    System.err.println("[WARN] Could not set executable permissions on gradlew");
                }
            }

            List<String> command = new ArrayList<>();
            command.add(gradlew.toAbsolutePath().toString());
            command.addAll(List.of(args));

            ProcessBuilder pb = new ProcessBuilder(command);
            pb.directory(projectDir.toFile());
            pb.redirectErrorStream(true);

            // Setup Environment
            var env = pb.environment();
            String javaHome = findCompatibleJavaHome();
            if (javaHome != null) {
                env.put("JAVA_HOME", javaHome);
            }
            env.put("TERM", "dumb");
            env.put("JAVA_OPTS", "-Dorg.gradle.daemon=false -Djava.awt.headless=true");

            System.out.println("[INFO] Running Gradle: " + String.join(" ", args) + " in " + projectDir);
            Process process = pb.start();

            // Asynchronous log reading to prevent process deadlock
            Thread logThread = new Thread(() -> printStream(process.getInputStream(), "[gradle]"));
            logThread.start();

            boolean finished = process.waitFor(10, TimeUnit.MINUTES);
            logThread.join(5000);

            if (!finished) {
                process.destroyForcibly();
                System.err.println("[ERROR] Gradle task timed out");
                return false;
            }

            return process.exitValue() == 0;

        } catch (IOException | InterruptedException e) {
            System.err.println("[ERROR] Execution failed: " + e.getMessage());
            Thread.currentThread().interrupt();
            return false;
        }
    }

    private static String findCompatibleJavaHome() {
        // Priority 1: Current JAVA_HOME
        String envJavaHome = System.getenv("JAVA_HOME");
        if (envJavaHome != null && Files.isDirectory(Path.of(envJavaHome))) {
            return envJavaHome;
        }

        // Priority 2: Standard paths (Simplified)
        String[] paths = System.getProperty("os.name").toLowerCase().contains("win")
                ? new String[]{"C:\\Program Files\\Java\\jdk-21", "C:\\Program Files\\Java\\jdk-17"}
                : new String[]{"/usr/lib/jvm/java-21-openjdk", "/usr/lib/jvm/java-17-openjdk"};

        for (String path : paths) {
            if (Files.isDirectory(Path.of(path))) return path;
        }

        return System.getProperty("java.home"); // Fallback
    }

    private static void printStream(java.io.InputStream stream, String prefix) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(prefix + " " + line);
            }
        } catch (IOException ignored) {}
    }

    private static TestResults parseTestResults(Path projectDir) {
        TestResults results = new TestResults();
        Path testResultsDir = projectDir.resolve("build/test-results/test");
        if (!Files.exists(testResultsDir)) return results;

        try (var files = Files.list(testResultsDir)) {
            files.filter(p -> p.toString().endsWith(".xml")).forEach(xmlFile -> {
                try {
                    String content = Files.readString(xmlFile);
                    int tests = extractInt(content, "tests");
                    int failures = extractInt(content, "failures");
                    int skipped = extractInt(content, "skipped");
                    results.passed += (tests - failures - skipped);
                    results.failed += failures;
                    results.skipped += skipped;
                } catch (IOException ignored) {}
            });
        } catch (IOException e) {
            System.err.println("[ERROR] Failed to parse test reports: " + e.getMessage());
        }
        return results;
    }

    private static int extractInt(String xml, String attr) {
        String pattern = attr + "=\"";
        int start = xml.indexOf(pattern);
        if (start == -1) return 0;
        start += pattern.length();
        int end = xml.indexOf("\"", start);
        return (end == -1) ? 0 : Integer.parseInt(xml.substring(start, end));
    }

    public static class TestResults {
        public int passed = 0, failed = 0, skipped = 0;
        public int getTotal() { return passed + failed + skipped; }
        @Override public String toString() { return String.format("Passed: %d, Failed: %d, Skipped: %d", passed, failed, skipped); }
    }
}