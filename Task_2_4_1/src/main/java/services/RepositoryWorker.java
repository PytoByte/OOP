package services;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import model.CommandExecutor;
import model.TestResults;
import model.TestResultsParseException;
import org.w3c.dom.Element;

/**
 * Worker for cloning repositories and running checks.
 */
public class RepositoryWorker {
    private final CommandExecutor executor;
    private final Path toolsDir;
    private final String checkstyleUrl;

    private Path repoRoot;
    private Path taskDir;

    /**
     * Creates a new RepositoryWorker.
     *
     * @param executor      command executor for running external processes
     * @param toolsDir      directory for storing tools like checkstyle
     * @param checkstyleUrl URL to download checkstyle jar
     */
    public RepositoryWorker(CommandExecutor executor, Path toolsDir, String checkstyleUrl) {
        this.executor = executor;
        this.toolsDir = toolsDir.toAbsolutePath();
        this.checkstyleUrl = checkstyleUrl;
    }

    /**
     * Clones a git repository to the specified work directory.
     *
     * @param url     repository URL
     * @param branch  branch name to clone
     * @param workDir target directory for the clone
     * @return true if clone succeeded, false otherwise
     * @throws IOException if can't create repository dir
     */
    public boolean cloneRepository(String url, String branch, Path workDir) throws IOException {
        this.repoRoot = workDir.toAbsolutePath();

        if (Files.exists(repoRoot)) {
            return true;
        }

        Files.createDirectories(repoRoot);

        return executor.execute(
                repoRoot,
                List.of("git", "clone", "--branch", branch, url, "."),
                "git"
        );
    }

    /**
     * Sets the task directory and returns the commit date.
     *
     * @param taskId the task directory name
     * @return commit date of the task, or null if not found
     */
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

    /**
     * Compiles the project using Gradle.
     *
     * @return true if compilation succeeded, false otherwise
     */
    public boolean compileProject() {
        return runGradle("testClasses");
    }

    /**
     * Runs Checkstyle code style validation.
     *
     * @return true if no warnings or errors found, false otherwise
     * @throws IOException if checkstyleJar or checkstyleXml not found
     */
    public boolean checkCodeStyle() throws IOException {
        Path checkstyleJar = prepareCheckstyleJar();
        Path checkstyleXml = prepareCheckstyleXml();
        Path src = taskDir.resolve("src");
        String targetPath = Files.exists(src) ? src.toString() : taskDir.toString();

        List<String> cmd = List.of(
                "java",
                "-Duser.language=en",
                "-jar",
                checkstyleJar.toAbsolutePath().toString(),
                "-c",
                checkstyleXml.toAbsolutePath().toString(),
                targetPath
        );

        final boolean[] hasWarnings = {false};
        boolean checkstyleExecuted = executor.execute(taskDir, cmd, "style",
                line -> {
                    if (line.contains("[WARN]") || line.contains("[ERROR]")) {
                        hasWarnings[0] = true;
                    }
                });
        return checkstyleExecuted && !hasWarnings[0];
    }

    /**
     * Generates Javadoc documentation for the project.
     *
     * @return true if generation succeeded, false otherwise
     */
    public boolean generateDocumentation() {
        return runGradle("javadoc", "-x", "test");
    }

    /**
     * Runs project tests and parses results.
     *
     * @return TestResults with passed/failed/skipped counts, or error result on failure
     * @throws IOException               if test results dir not found
     * @throws TestResultsParseException if there is an exception in parsing process
     */
    public TestResults runTests() throws IOException {
        if (!runGradle("test")) {
            return TestResults.error();
        }
        return parseXml(taskDir.resolve("build/test-results/test"));
    }

    /**
     * Gets the commit date for a task from git log.
     *
     * @param taskId the task directory name
     * @return commit date as OffsetDateTime, or null if unavailable
     */
    private OffsetDateTime getCommitDate(String taskId) {
        String[] date = {null};
        List<String> cmd = List.of("git", "log", "-1", "--format=%cI", "--", taskId);
        if (executor.execute(repoRoot, cmd, "git-log-date",
                line -> date[0] = line.trim()) && date[0] != null
        ) {
            return OffsetDateTime.parse(date[0], DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        }
        return null;
    }

    /**
     * Runs a Gradle command with standard options.
     *
     * @param args Gradle task and arguments
     * @return true if command succeeded, false otherwise
     */
    private boolean runGradle(String... args) {
        boolean isWindows = System.getProperty("os.name").toLowerCase().contains("win");
        Path wrapper = taskDir.resolve(isWindows ? "gradlew.bat" : "gradlew");
        List<String> cmd = new LinkedList<>();
        cmd.add(wrapper.toAbsolutePath().toString());
        cmd.add("-Dorg.gradle.java.home=" + System.getProperty("java.home"));
        cmd.add("-Duser.language=en");
        cmd.add("--max-workers=1");
        cmd.add("--no-daemon");
        cmd.addAll(Arrays.asList(args));

        return executor.execute(taskDir, cmd, "gradle");
    }

    /**
     * Parses JUnit XML test results from the specified directory.
     *
     * @param dir directory containing test result XML files
     * @return aggregated TestResults
     * @throws IOException               if test results dir not found
     * @throws TestResultsParseException if there is an exception in parsing process
     */
    private TestResults parseXml(Path dir) throws IOException {
        if (!Files.exists(dir)) {
            throw new IOException(
                    String.format("Test results dir not found: %s", dir.toAbsolutePath())
            );
        }
        try (var stream = Files.list(dir)) {
            int passedTotal = 0;
            int failedTotal = 0;
            int skippedTotal = 0;
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
            throw new TestResultsParseException(
                    String.format("Failed to parse test results %s", dir.toAbsolutePath()), e
            );
        }
    }

    /**
     * Downloads checkstyle jar if not already present.
     *
     * @return path to the checkstyle jar file
     * @throws IOException if download fails or tools dir can't be created
     */
    private Path prepareCheckstyleJar() throws IOException {
        Files.createDirectories(toolsDir);
        Path checkstyleJar = toolsDir.resolve("checkstyle-all.jar");
        if (!Files.exists(checkstyleJar)) {
            try (InputStream in = URI.create(checkstyleUrl).toURL().openStream()) {
                Files.copy(in, checkstyleJar, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                throw new IOException(String.format("Download failed %s", checkstyleUrl), e);
            }
        }
        return checkstyleJar;
    }

    /**
     * Copies checkstyle config XML from resources if not already present.
     *
     * @return path to the checkstyle xml file
     * @throws IOException if resource not found or copy fails
     */
    private Path prepareCheckstyleXml() throws IOException {
        Path checkstyleXml = toolsDir.resolve("checkstyle.xml");
        if (!Files.exists(checkstyleXml)) {
            try (InputStream in = getClass().getResourceAsStream("/google_checks.xml")) {
                if (in == null) {
                    throw new IOException(
                            String.format("File not found: %s", checkstyleXml.toAbsolutePath())
                    );
                }
                Files.copy(in, checkstyleXml, StandardCopyOption.REPLACE_EXISTING);
            }
        }
        return checkstyleXml;
    }
}