import Domain.CheckAssignment;
import Domain.CheckResult;
import Domain.Config;
import Domain.TestResults;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.OffsetDateTime;
import java.util.LinkedList;
import java.util.List;

public class Executor {

    public static List<CheckResult> execute(Config cfg) {
        List<CheckResult> results = new LinkedList<>();
        Path workDir = null;

        try {
            workDir = Files.createTempDirectory(Paths.get("."), "oop-checker-work");

            for (CheckAssignment assignment : cfg.getCheckAssignments()) {
                results.add(executeCheckAssignment(assignment, workDir));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (workDir != null) {
                cleanup(workDir);
            }
        }
        return results;
    }

    private static void cleanup(Path workDir) {
        try {
            if (Files.exists(workDir)) {
                Files.walk(workDir)
                        .sorted(java.util.Comparator.reverseOrder())
                        .map(Path::toFile)
                        .forEach(java.io.File::delete);
            }
        } catch (IOException e) {
            System.err.println("Cleanup failed: " + e.getMessage());
        }
    }

    public static CheckResult executeCheckAssignment(
            CheckAssignment checkAssignment,
            Path workDir
    ) {
        String repoUrl = String.format(
                "https://github.com/%s/OOP",
                checkAssignment.student().nick()
        );
        Path studentRepoPath = workDir.resolve(checkAssignment.student().nick());
        String taskId = checkAssignment.task().getId();

        Logger logger = new Logger("executor");
        logger.info("=== STUDENT: %s TASK: %s ===", checkAssignment.student().name(), taskId);

        RepositoryWorker worker = new RepositoryWorker();

        logger.info("Cloning repo to: %s", studentRepoPath);
        if (!worker.cloneRepository(repoUrl, "main", studentRepoPath)) {
            logger.info("Cloning failed");
            return CheckResult.failedDownload(checkAssignment);
        }
        logger.info("Cloning success");

        logger.info("Setting task context: %s", taskId);
        OffsetDateTime commitDate = worker.setTask(taskId);

        if (commitDate == null) {
            logger.info("Task folder %s not found in repository!", taskId);
            return CheckResult.failedDownload(checkAssignment);
        }
        logger.info("Task found. Latest commit date: %s", commitDate);

        logger.info("Compiling");
        boolean isCompiled = worker.compileProject();
        logger.info(isCompiled ? "Compiling success" : "Compiling failed");

        logger.info("Generate docs");
        boolean docsOk = worker.generateDocumentation();
        logger.info(docsOk ? "Docs generation success" : "Docs generation failed");

        logger.info("Style checking");
        boolean styleOk = worker.checkCodeStyle();
        logger.info(styleOk ? "Style checking success" : "Style checking failed");

        logger.info("Test build & run");
        TestResults testResults = worker.runTests();
        boolean testsCompiled = !testResults.equals(TestResults.error());
        logger.info(testsCompiled ? "Test build & run success" : "Test build & run failed");

        return new CheckResult(
                checkAssignment,
                commitDate,
                true,
                isCompiled,
                docsOk,
                styleOk,
                testsCompiled,
                testResults.passed(),
                testResults.failed(),
                testResults.skipped(),
                2
        );
    }
}