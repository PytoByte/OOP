import Domain.TestResults;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RepositoryWorker {

    private static final int DEFAULT_TIMEOUT_MIN = 10;
    private static final String CHECKSTYLE_URL = "https://github.com/checkstyle/" +
            "checkstyle/releases/download/checkstyle-10.17.0/checkstyle-10.17.0-all.jar";
    private static final String JAR_NAME = "checkstyle-all.jar";
    private static final Path TOOLS_DIR = Path.of(
            System.getProperty("user.home"), ".cache", "oop-checker"
    );

    private static Path prepareCheckstyle() throws IOException {
        Files.createDirectories(TOOLS_DIR);
        Path jarPath = TOOLS_DIR.resolve(JAR_NAME);
        if (!Files.exists(jarPath)) {
            System.out.println("[INFO] Downloading Checkstyle...");
            try (InputStream in = new URI(CHECKSTYLE_URL).toURL().openStream()) {
                Files.copy(in, jarPath, StandardCopyOption.REPLACE_EXISTING);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }
        return jarPath;
    }

    private static Path prepareConfig() throws IOException {
        Path tempConfig = Files.createTempFile("google_checks", ".xml");
        try (InputStream in = RepositoryWorker.class.getClassLoader()
                .getResourceAsStream("google_checks.xml")) {
            if (in == null) {
                throw new IOException("Config google_checks.xml not found in resources!");
            }
            Files.copy(in, tempConfig, StandardCopyOption.REPLACE_EXISTING);
        }
        tempConfig.toFile().deleteOnExit();
        return tempConfig;
    }

    public static boolean cloneRepository(String repoUrl, String branch, Path workDir) {
        try {
            if (Files.exists(workDir)) {
                deleteDirectory(workDir);
            }
            Files.createDirectories(workDir);
            List<String> command = List.of(
                    "git", "clone", "--branch", branch, "--depth", "1", repoUrl, "."
            );
            return execute(command, workDir, "git");
        } catch (IOException e) {
            return false;
        }
    }

    public static boolean compileProject(Path projectDir) {
        return runGradle(projectDir, "testClasses");
    }

    public static boolean generateDocumentation(Path projectDir) {
        try {
            String initContent = "allprojects " +
                    "{ tasks.withType(Javadoc) { " +
                    "options.addBooleanOption('Xwerror', true);" +
                    "} }";
            Path initScript = Files.createTempFile("javadoc-init", ".gradle");
            Files.writeString(initScript, initContent);
            boolean result = runGradle(
                    projectDir,
                    "javadoc",
                    "--init-script",
                    initScript.toString()
            );
            Files.deleteIfExists(initScript);
            return result;
        } catch (IOException e) {
            return false;
        }
    }

    public static boolean checkCodeStyle(Path projectDir) {
        try {
            Path jarPath = prepareCheckstyle();
            Path configPath = prepareConfig();
            Path srcDir = projectDir.resolve("src/main/java");

            if (!Files.exists(srcDir)) return false;

            List<String> command = List.of(
                    "java", "-Duser.language=en", "-Dfile.encoding=UTF-8",
                    "-jar", jarPath.toString(),
                    "-c", configPath.toString(),
                    srcDir.toString()
            );

            // Флаг для отслеживания нарушений
            final boolean[] hasViolations = {false};

            boolean exitOk = execute(command, projectDir, "checkstyle", line -> {
                if (line.contains("[WARN]") || line.contains("[ERROR]")) {
                    hasViolations[0] = true;
                }
            });

            return exitOk && !hasViolations[0];

        } catch (Exception e) {
            return false;
        }
    }

    public static TestResults runTests(Path projectDir) {
        runGradle(projectDir, "test");
        return parseXmlResults(projectDir.resolve("build/test-results/test"));
    }

    private static boolean runGradle(Path dir, String... args) {
        boolean isWin = System.getProperty("os.name").toLowerCase().contains("win");
        Path wrapper = dir.resolve(isWin ? "gradlew.bat" : "gradlew");
        if (!Files.exists(wrapper)) return false;

        if (!isWin) {
            try {
                Files.setPosixFilePermissions(
                        wrapper, PosixFilePermissions.fromString("rwxr-xr-x")
                );
            } catch (Exception ignored) {
            }
        }

        List<String> command = new ArrayList<>();
        command.add(wrapper.toAbsolutePath().toString());

        String currentJavaHome = System.getProperty("java.home");
        command.add("-Dorg.gradle.java.home=" + currentJavaHome);

        command.add("-Porg.gradle.java.installations.auto-detect=true");
        command.add("-Porg.gradle.java.installations.auto-download=true");

        command.addAll(Arrays.asList(args));

        return execute(command, dir, "gradle");
    }

    private static boolean execute(
            List<String> command,
            Path dir,
            String loggerName,
            Consumer<String> outputInspector
    ) {
        Logger logger = new Logger(loggerName);
        try {
            ProcessBuilder pb = new ProcessBuilder(command)
                    .directory(dir.toFile())
                    .redirectErrorStream(true);
            Process process = pb.start();
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8)
            )) {
                reader.lines().forEach(line -> {
                    logger.info(line);
                    if (outputInspector != null) {
                        outputInspector.accept(line);
                    }
                });
            }
            return process.waitFor(DEFAULT_TIMEOUT_MIN, TimeUnit.MINUTES) && process.exitValue() == 0;
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean execute(List<String> command, Path dir, String loggerName) {
        return execute(command, dir, loggerName, null);
    }

    private static void deleteDirectory(Path path) throws IOException {
        if (Files.exists(path)) {
            Files.walk(path)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        }
    }

    private static TestResults parseXmlResults(Path dir) {
        final TestResults[] results = {TestResults.empty()};
        if (Files.exists(dir)) {
            try (var stream = Files.list(dir)) {
                stream.filter(p -> p.toString().endsWith(".xml")).forEach(file -> {
                    try {
                        String content = Files.readString(file);
                        int t = extractInt(content, "tests");
                        int f = extractInt(content, "failures");
                        int s = extractInt(content, "skipped");
                        results[0] = new TestResults(t - f - s, f, s);
                    } catch (IOException ignored) {
                    }
                });
            } catch (IOException ignored) {
            }
        }
        return results[0];
    }

    private static int extractInt(String xml, String attr) {
        Matcher m = Pattern.compile(attr + "=\"(\\d+)\"").matcher(xml);
        return m.find() ? Integer.parseInt(m.group(1)) : 0;
    }
}
