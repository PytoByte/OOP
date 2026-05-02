import Domain.CheckAssignment;
import Domain.CheckResult;
import Domain.Config;
import Domain.TestResults;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Executor {

    public static List<CheckResult> execute(Config cfg, Path workDir) {
        List<CheckResult> results = new ArrayList<>();
        for (CheckAssignment assignment : cfg.getCheckAssignments()) {
            results.add(executeCheckAssignment(assignment, workDir));
        }
        return results;
    }

    public static CheckResult executeCheckAssignment(
            CheckAssignment checkAssignment,
            Path workDir
    ) {
        String studentName = checkAssignment.student().name();
        String studentNick = checkAssignment.student().nick();
        String taskId = checkAssignment.task().getId();
        Path studentRepoPath = workDir.resolve(studentNick);
        Path projectPath = studentRepoPath.resolve(taskId);
        Logger logger = new Logger("executor");

        logger.info("=== STUDENT: %s TASK: %s ===", studentName, taskId);

        logger.info("Cloning");
        if (RepositoryWorker.cloneRepository(
                checkAssignment.student().repoUrl(),
                "main",
                studentRepoPath
        )) {
            logger.info("Cloning success");
        } else {
            logger.info("Cloning failed");
            return CheckResult.failedDownload(checkAssignment);
        }

        logger.info("Compiling");
        boolean isCompiled = RepositoryWorker.compileProject(projectPath);
        if (isCompiled) {
            logger.info("Compiling success");
        } else {
            logger.info("Compiling failed");
        }

        logger.info("Generate docs");
        boolean docsOk = RepositoryWorker.generateDocumentation(projectPath);
        if (docsOk) {
            logger.info("Docs generation success");
        } else {
            logger.info("Docs generation failed");
        }

        logger.info("Style checking");
        boolean styleOk = RepositoryWorker.checkCodeStyle(projectPath);
        if (styleOk) {
            logger.info("Style checking success");
        } else {
            logger.info("Style checking failed");
        }

        logger.info("Test build & run");
        boolean testsCompiled;
        TestResults testResults;
        try {
            testResults = RepositoryWorker.runTests(projectPath);
            testsCompiled = true;
            logger.info("Test build & run success");
        } catch (Exception e) {
            testResults = TestResults.empty();
            testsCompiled = false;
            e.printStackTrace();
            logger.info("Test build & run failed");
        }

        return new CheckResult(checkAssignment, true, isCompiled, docsOk, styleOk,
                testsCompiled,
                testResults.passed(), testResults.failed(), testResults.skipped(), 2);
    }
}