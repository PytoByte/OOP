package services;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import model.CheckAssignment;
import model.CheckResult;
import model.Checkpoint;
import model.Group;
import model.Student;
import model.Task;
import model.TestResults;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class ReportGeneratorTest {

    @TempDir
    Path tempDir;

    @Test
    void testWriteHtmlGeneratesValidContent() throws IOException {
        Student student = new Student("tester", "tester-nick");
        Group group = new Group("tester-group", Collections.emptyMap());

        Checkpoint cp = new Checkpoint("Soft Deadline", LocalDate.now().minusDays(1), 2.0f);
        Task task = new Task("lab1", "Threading", 5.0f, List.of(cp));

        CheckAssignment assignment = new CheckAssignment(group, student, task);

        CheckResult result = new CheckResult(
                assignment,
                OffsetDateTime.now(),
                true,
                true,
                false,
                true,
                new TestResults(10, 2, 0),
                5.0f
        );

        File reportFile = tempDir.resolve("report.html").toFile();

        ReportGenerator.writeHtml(List.of(result), reportFile);

        assertTrue(reportFile.exists());

        String content = Files.readString(reportFile.toPath());

        assertTrue(content.contains("tester-group"));
        assertTrue(content.contains("tester"));
        assertTrue(content.contains("Threading"));
        assertTrue(content.contains("OK"));
        assertTrue(content.contains("FAIL"));
        assertTrue(content.contains("passed 10"));
        assertTrue(content.contains("failed 2"));

        assertTrue(content.contains("[FAILED]"));
    }

    @Test
    void testWriteHtmlWithNoCommitData() throws IOException {
        Student student = new Student("tester", "test");
        Group group = new Group("G1", Collections.emptyMap());
        Task task = new Task("t1", "T1", 10f, List.of(new Checkpoint("DL", LocalDate.now(), 1f)));

        CheckAssignment assignment = new CheckAssignment(group, student, task);
        CheckResult result = CheckResult.failedDownload(assignment);

        File reportFile = tempDir.resolve("failed_report.html").toFile();
        ReportGenerator.writeHtml(List.of(result), reportFile);

        String content = Files.readString(reportFile.toPath());

        assertTrue(content.contains("FAIL"), "Должно быть FAIL в колонке Clone");
        assertTrue(content.contains("[NO DATA]"));
        assertTrue(content.contains("0,00/11,00") || content.contains("0.00/11.00"));
    }
}