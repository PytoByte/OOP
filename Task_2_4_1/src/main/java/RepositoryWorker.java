import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RepositoryWorker {

    private static final int DEFAULT_TIMEOUT_MIN = 10;
    private static final String CHECKSTYLE_URL = "https://github.com/checkstyle/checkstyle/releases/download/checkstyle-10.17.0/checkstyle-10.17.0-all.jar";
    private static final String JAR_NAME = "checkstyle-all.jar";
    private static final Path TOOLS_DIR = Path.of(System.getProperty("user.home"), ".cache", "oop-checker");

    // Метод для подготовки Checkstyle JAR
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

    // Метод для извлечения google_checks.xml из ресурсов вашего JAR
    private static Path prepareConfig() throws IOException {
        Path tempConfig = Files.createTempFile("google_checks", ".xml");
        try (InputStream in = RepositoryWorker.class.getClassLoader().getResourceAsStream("google_checks.xml")) {
            if (in == null) throw new IOException("Config google_checks.xml not found in resources!");
            Files.copy(in, tempConfig, StandardCopyOption.REPLACE_EXISTING);
        }
        tempConfig.toFile().deleteOnExit();
        return tempConfig;
    }

    public static Path cloneRepository(String repoUrl, String branch, Path workDir) {
        try {
            if (Files.exists(workDir)) {
                // Если папка уже есть, удаляем (чтобы клон был чистым)
                deleteDirectory(workDir);
            }
            Files.createDirectories(workDir);
            List<String> command = List.of("git", "clone", "--branch", branch, "--depth", "1", repoUrl, ".");
            return execute(command, workDir, "[git]") ? workDir : null;
        } catch (IOException e) { return null; }
    }

    public static boolean compileProject(Path projectDir) {
        // Компилируем всё сразу (main и test) одним вызовом
        return runGradle(projectDir, "testClasses");
    }

    public static boolean generateDocumentation(Path projectDir) {
        try {
            String initContent = "allprojects { tasks.withType(Javadoc) { options.addBooleanOption('Xwerror', true); } }";
            Path initScript = Files.createTempFile("javadoc-init", ".gradle");
            Files.writeString(initScript, initContent);
            boolean result = runGradle(projectDir, "javadoc", "--init-script", initScript.toString());
            Files.deleteIfExists(initScript);
            return result;
        } catch (IOException e) { return false; }
    }

    public static boolean checkCodeStyle(Path projectDir) {
        try {
            Path jarPath = prepareCheckstyle();
            Path configPath = prepareConfig();
            Path srcDir = projectDir.resolve("src/main/java");

            if (!Files.exists(srcDir)) return false;

            List<String> command = List.of(
                    "java",
                    "-Duser.language=en",
                    "-Dfile.encoding=UTF-8",
                    "-jar", jarPath.toString(),
                    "-c", configPath.toString(),
                    srcDir.toString()
            );

            // Мы не можем просто использовать execute(command...),
            // потому что нам нужно прочитать вывод и найти там слова [WARN] или [ERROR]
            ProcessBuilder pb = new ProcessBuilder(command).directory(projectDir.toFile()).redirectErrorStream(true);
            Process process = pb.start();

            boolean hasViolations = false;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println("[checkstyle] " + line);
                    // Если в строке есть маркер нарушения стиля
                    if (line.contains("[WARN]") || line.contains("[ERROR]")) {
                        hasViolations = true;
                    }
                }
            }

            process.waitFor(DEFAULT_TIMEOUT_MIN, TimeUnit.MINUTES);

            // Возвращаем true, только если процесс завершился без системных ошибок
            // И не было найдено ни одного нарушения стиля
            return process.exitValue() == 0 && !hasViolations;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static TestResults runTests(Path projectDir) {
        runGradle(projectDir, "test");
        return parseXmlResults(projectDir.resolve("build/test-results/test"));
    }

    // --- Вспомогательные методы ---

    private static boolean runGradle(Path dir, String... args) {
        boolean isWin = System.getProperty("os.name").toLowerCase().contains("win");
        Path wrapper = dir.resolve(isWin ? "gradlew.bat" : "gradlew");
        if (!Files.exists(wrapper)) return false;

        if (!isWin) {
            try { Files.setPosixFilePermissions(wrapper, PosixFilePermissions.fromString("rwxr-xr-x")); } catch (Exception ignored) {}
        }

        List<String> command = new ArrayList<>();
        command.add(wrapper.toAbsolutePath().toString());

        // 1. Указываем JAVA_HOME текущей запущенной JVM (где точно есть компилятор)
        String currentJavaHome = System.getProperty("java.home");
        command.add("-Dorg.gradle.java.home=" + currentJavaHome);

        // 2. ОТКЛЮЧАЕМ Toolchains студента, чтобы Gradle не пытался искать другую Java на диске
        command.add("-Porg.gradle.java.installations.auto-detect=false");
        command.add("-Porg.gradle.java.installations.auto-download=false");

        command.addAll(Arrays.asList(args));

        return execute(command, dir, "[gradle]");
    }

    private static boolean execute(List<String> command, Path dir, String logPrefix) {
        try {
            ProcessBuilder pb = new ProcessBuilder(command).directory(dir.toFile()).redirectErrorStream(true);
            Process process = pb.start();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))) {
                reader.lines().forEach(line -> System.out.println(logPrefix + " " + line));
            }
            return process.waitFor(DEFAULT_TIMEOUT_MIN, TimeUnit.MINUTES) && process.exitValue() == 0;
        } catch (Exception e) { return false; }
    }

    private static void deleteDirectory(Path path) throws IOException {
        if (Files.exists(path)) {
            Files.walk(path).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
        }
    }

    private static TestResults parseXmlResults(Path dir) {
        TestResults results = new TestResults();
        if (Files.exists(dir)) {
            try (var stream = Files.list(dir)) {
                stream.filter(p -> p.toString().endsWith(".xml")).forEach(file -> {
                    try {
                        String content = Files.readString(file);
                        int t = extractInt(content, "tests");
                        int f = extractInt(content, "failures");
                        int s = extractInt(content, "skipped");
                        results.passed += (t - f - s);
                        results.failed += f;
                        results.skipped += s;
                    } catch (IOException ignored) {}
                });
            } catch (IOException ignored) {}
        }
        return results;
    }

    private static int extractInt(String xml, String attr) {
        Matcher m = Pattern.compile(attr + "=\"(\\d+)\"").matcher(xml);
        return m.find() ? Integer.parseInt(m.group(1)) : 0;
    }

    public static class TestResults {
        public int passed = 0, failed = 0, skipped = 0;
    }
}
