import Domain.TestResults;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Element;

public class RepositoryWorker {
    private static final int TIMEOUT_MIN = 10;
    private static final String CHECKSTYLE_URL = "https://github.com/checkstyle/checkstyle/" +
            "releases/download/checkstyle-10.17.0/checkstyle-10.17.0-all.jar";
    private static final Path TOOLS_DIR = Path.of("tools").toAbsolutePath();
    private static final Path CHECKSTYLE_JAR = TOOLS_DIR.resolve("checkstyle-all.jar");

    private Path repoRoot;
    private Path taskDir;

    public boolean cloneRepository(String url, String branch, Path workDir) {
        Logger logger = new Logger("worker");
        this.repoRoot = workDir.toAbsolutePath();
        try {
            if (Files.exists(repoRoot)) {
                deleteDirectory(repoRoot);
            }
            Files.createDirectories(repoRoot);
            return exec(repoRoot, List.of("git", "clone", "--branch", branch, url, "."), "git");
        } catch (IOException e) {
            logger.error("Cloning error: %s", e.getMessage());
            return false;
        }
    }

    public OffsetDateTime setTask(String taskId) {
        if (repoRoot == null) {
            return null;
        }
        Path target = repoRoot.resolve(taskId);
        if (!Files.exists(target)) {
            return null;
        }
        this.taskDir = target.toAbsolutePath();
        return getCommitDate(taskId);
    }

    public boolean compileProject() {
        return runGradle("testClasses");
    }

    public boolean generateDocumentation() {
        return runGradle("javadoc", "-x", "test");
    }

    public boolean checkCodeStyle() {
        try {
            Path jar = prepareCheckstyle();
            Path config = prepareConfig();
            Path src = taskDir.resolve("src");
            String targetPath = Files.exists(src) ? src.toString() : taskDir.toString();

            List<String> cmd = List.of(
                    "java", "-Duser.language=en",
                    "-jar", jar.toAbsolutePath().toString(),
                    "-c", config.toAbsolutePath().toString(),
                    targetPath
            );

            final boolean[] failed = {false};
            boolean ok = exec(taskDir, cmd, "style", line -> {
                if (line.contains("[WARN]") || line.contains("[ERROR]")) {
                    failed[0] = true;
                }
            });
            return ok && !failed[0];
        } catch (IOException e) {
            return false;
        }
    }

    public TestResults runTests() {
        runGradle("test");
        return parseXml(taskDir.resolve("build/test-results/test"));
    }

    private OffsetDateTime getCommitDate(String taskId) {
        String[] date = {null};
        List<String> cmd = List.of("git", "log", "-1", "--format=%cI", "--", taskId);
        if (exec(repoRoot, cmd, "git-log-date",
                line -> date[0] = line.trim()) && date[0] != null) {
            try {
                return OffsetDateTime.parse(date[0], DateTimeFormatter.ISO_OFFSET_DATE_TIME);
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    private boolean runGradle(String... args) {
        Path wrapper = taskDir.resolve("gradlew.bat");

        List<String> cmd = new LinkedList<>();
        cmd.add(wrapper.toAbsolutePath().toString());
        String currentJavaHome = System.getProperty("java.home");
        cmd.add("-Dorg.gradle.java.home=" + currentJavaHome);
        cmd.add("--no-daemon");
        cmd.addAll(Arrays.asList(args));
        return exec(taskDir, cmd, "gradle");
    }

    private boolean exec(
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
                    new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = r.readLine()) != null) {
                    logger.info(line);
                    if (inspector != null) {
                        inspector.accept(line);
                    }
                }
            }
            return process.waitFor(TIMEOUT_MIN, TimeUnit.MINUTES) && process.exitValue() == 0;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean exec(Path dir, List<String> cmd, String prefix) {
        return exec(dir, cmd, prefix, null);
    }

    private TestResults parseXml(Path dir) {
        if (!Files.exists(dir)) {
            return TestResults.error();
        }
        try (var stream = Files.list(dir)) {
            int passedTotal = 0, failedTotal = 0, skippedTotal = 0;
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            for (Path file : stream.filter(path -> path.toString().endsWith(".xml")).toList()) {
                Element root = db.parse(file.toFile()).getDocumentElement();
                int tests = Integer.parseInt(root.getAttribute("tests"));
                int failed = Integer.parseInt(root.getAttribute("failures"));
                int errors = Integer.parseInt(root.getAttribute("errors"));
                int skipped = Integer.parseInt(root.getAttribute("skipped"));
                passedTotal += (tests - failed - errors - skipped);
                failedTotal += (failed + errors);
                skippedTotal += skipped;
            }
            return new TestResults(passedTotal, failedTotal, skippedTotal);
        } catch (Exception e) {
            return TestResults.error();
        }
    }

    private Path prepareCheckstyle() throws IOException {
        Files.createDirectories(TOOLS_DIR);
        if (!Files.exists(CHECKSTYLE_JAR)) {
            System.out.println("[worker] Downloading Checkstyle...");
            try (InputStream in = URI.create(CHECKSTYLE_URL).toURL().openStream()) {
                Files.copy(in, CHECKSTYLE_JAR, StandardCopyOption.REPLACE_EXISTING);
            }
        }
        return CHECKSTYLE_JAR;
    }

    private Path prepareConfig() throws IOException {
        Path cfg = TOOLS_DIR.resolve("google_checks.xml");
        if (!Files.exists(cfg)) {
            try (InputStream in = getClass().getResourceAsStream("/google_checks.xml")) {
                if (in == null) {
                    throw new IOException("google_checks.xml not found in resources");
                }
                Files.copy(in, cfg, StandardCopyOption.REPLACE_EXISTING);
            }
        }
        return cfg;
    }

    private void deleteDirectory(Path path) throws IOException {
        try (var stream = Files.walk(path)) {
            stream.sorted(Comparator.reverseOrder()).forEach(p -> p.toFile().delete());
        }
    }
}