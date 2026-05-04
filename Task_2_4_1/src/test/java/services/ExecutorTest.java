package services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import mock.ExecutorMock;
import model.CheckAssignment;
import model.CheckResult;
import model.Checkpoint;
import model.Group;
import model.Student;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class ExecutorTest {

    @TempDir
    Path tempDir;

    private ExecutorMock commandMock;
    private Executor executor;
    private Path tempWorkDir;

    private Group testGroup;
    private Student testStudent;

    @BeforeEach
    void setUp() throws IOException {
        tempWorkDir = tempDir.resolve("work").toAbsolutePath();
        Path toolsDir = tempDir.resolve("tools").toAbsolutePath();

        Files.createDirectories(tempWorkDir);
        Files.createDirectories(toolsDir);

        commandMock = new ExecutorMock() {
            @Override
            public boolean execute(
                    Path dir,
                    List<String> cmd,
                    String loggerName,
                    Consumer<String> inspector
            ) {
                if (cmd.contains("clone")) {
                    try {
                        Files.createDirectories(tempWorkDir
                                .resolve("tester")
                                .resolve("task_1"));
                        Files.createDirectories(tempWorkDir
                                .resolve("tester")
                                .resolve("task_3"));
                    } catch (IOException ignored) {

                    }
                }
                return super.execute(dir, cmd, loggerName, inspector);
            }
        };

        executor = new Executor(
                commandMock,
                tempWorkDir,
                toolsDir,
                "file:///fake/checkstyle.jar"
        );

        testStudent = new Student("tester", "tester");
        testGroup = new Group("M30-212", Map.of("tester", testStudent));

        commandMock.nextResult = true;
    }

    @Test
    void testSuccessfulExecutionFlow() {
        Checkpoint cp = new Checkpoint("Soft Deadline", LocalDate.now().plusDays(1), 2.0f);
        Task task = new Task("task_1", "Lab 1", 5.0f, List.of(cp));
        CheckAssignment assignment = new CheckAssignment(testGroup, testStudent, task);

        commandMock.nextResult = true;
        commandMock.outputToInject = OffsetDateTime.now().toString();

        List<CheckResult> results = executor.execute(List.of(assignment));

        assertFalse(results.isEmpty());
        CheckResult res = results.get(0);

        assertTrue(res.download(), "Download failed: RepositoryWorker didn't find the task directory");
        assertTrue(res.build(), "Build failed");
        assertEquals(7.0f, res.points());
    }

    @Test
    void testMissedDeadlinePoints() throws IOException {
        commandMock.nextResult = true;
        commandMock.outputToInject = OffsetDateTime.now().toString();

        Checkpoint deadline = new Checkpoint("Hard Deadline", LocalDate.now().minusDays(1), 5.0f);
        Task task = new Task("task_3", "Lab 3", 10.0f, List.of(deadline));
        CheckAssignment assignment = new CheckAssignment(testGroup, testStudent, task);

        Files.createDirectories(tempWorkDir.resolve("tester").resolve("task_3"));

        CheckResult res = executor.executeCheckAssignment(assignment, tempWorkDir);

        assertTrue(res.download());
        assertEquals(10.0f, res.points());
    }

    @Test
    void testFailedDownloadReturnsCorrectRecord() {
        commandMock.nextResult = false;

        Task task = new Task("task_2", "Lab 2", 10.0f, List.of());
        CheckAssignment assignment = new CheckAssignment(testGroup, testStudent, task);

        CheckResult res = executor.executeCheckAssignment(assignment, tempWorkDir);

        assertFalse(res.download());
        assertEquals(0.0f, res.points());
    }
}
