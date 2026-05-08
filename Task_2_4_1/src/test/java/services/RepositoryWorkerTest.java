package services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.OffsetDateTime;
import java.util.List;
import mock.ExecutorMock;
import model.TestResults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class RepositoryWorkerTest {
    @TempDir
    Path tempDir;
    private ExecutorMock executorMock;
    private RepositoryWorker worker;
    private Path toolsDir;
    private Path repoDir;

    @BeforeEach
    void setUp() {
        executorMock = new ExecutorMock();
        toolsDir = tempDir.resolve("tools").toAbsolutePath();
        repoDir = tempDir.resolve("repo").toAbsolutePath();
        worker = new RepositoryWorker(executorMock, toolsDir, "file:///fake/path/checkstyle.jar");
    }

    @Test
    void testCloneRepository() throws IOException {
        boolean result = worker.cloneRepository("https://github.com/test/repo", "main", repoDir);

        assertTrue(result);
        assertFalse(executorMock.calls.isEmpty());

        ExecutorMock.ExecutionCall call = executorMock.calls.get(0);
        assertTrue(call.cmd().contains("git"));
        assertTrue(call.cmd().contains("clone"));
        assertEquals(repoDir.toAbsolutePath(), call.dir().toAbsolutePath());
    }

    @Test
    void testSetTaskAndGetDate() throws IOException {
        worker.cloneRepository("url", "branch", repoDir);

        Path taskDir = repoDir.resolve("Task_1_1");
        Files.createDirectories(taskDir);

        executorMock.outputToInject = "2026-02-13T10:44:59+07:00";

        OffsetDateTime date = worker.setTask("Task_1_1");

        assertNotNull(date, "Дата не должна быть null, если setTask отработал верно");
        assertEquals(2026, date.getYear());
        assertEquals(13, date.getDayOfMonth());
    }

    @Test
    void testCompileProjectBuildsCorrectCommand() throws IOException {
        Path taskPath = setupTaskContext("Task_1_1");
        assertTrue(worker.compileProject());
        ExecutorMock.ExecutionCall call = executorMock.calls.get(executorMock.calls.size() - 1);
        assertEquals(taskPath.toAbsolutePath(), call.dir().toAbsolutePath());
        List<String> cmd = call.cmd();;
        assertTrue(cmd.get(0).contains("gradlew"));
        assertTrue(cmd.contains("testClasses"));;
    }

    @Test
    void testCheckCodeStyleFailsOnWarning() throws IOException {
        setupTaskContext("Task_1_1");

        Files.createDirectories(toolsDir);
        Files.createFile(toolsDir.resolve("checkstyle-all.jar"));
        Files.createFile(toolsDir.resolve("checkstyle.xml"));

        executorMock.outputToInject = "[WARN] SomeClass.java:10: Неправильный отступ";

        boolean result = worker.checkCodeStyle();

        assertFalse(result);
    }

    @Test
    void testRunTestsAndParseXml() throws IOException {
        Path taskPath = setupTaskContext("Task_1_1");

        Path xmlDir = taskPath.resolve("build/test-results/test");
        Files.createDirectories(xmlDir);

        String xmlContent = """
            <?xml version="1.0" encoding="UTF-8"?>
            <testsuite tests="10" failures="2" errors="1" skipped="1">
            </testsuite>
            """;
        Files.writeString(xmlDir.resolve("TEST-results.xml"), xmlContent);

        executorMock.nextResult = true;
        TestResults results = worker.runTests();

        assertNotNull(results);
        assertFalse(results.isError());
        assertEquals(6, results.passed());
        assertEquals(3, results.failed());
        assertEquals(1, results.skipped());
    }

    private Path setupTaskContext(String taskId) throws IOException {
        worker.cloneRepository("url", "branch", repoDir);

        Path taskPath = repoDir.resolve(taskId);
        Files.createDirectories(taskPath);
        Files.createFile(taskPath.resolve("gradlew.bat"));

        worker.setTask(taskId);

        return taskPath;
    }
}