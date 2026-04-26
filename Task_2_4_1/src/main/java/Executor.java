import Domain.CheckAssignment;
import Domain.CheckResult;
import Domain.Config;
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

    public static CheckResult executeCheckAssignment(CheckAssignment checkAssignment, Path workDir) {
        String studentNick = checkAssignment.student().nick();
        String taskId = checkAssignment.task().getId();

        // Root folder for the student's repo
        Path repoPath = workDir.resolve(studentNick);

        // 1. Clone
        Path clonedPath = RepositoryWorker.cloneRepository(
                checkAssignment.student().repoUrl(),
                taskId.replace('T', 't').replace('_', '-'),
                repoPath
        );

        if (clonedPath == null) {
            System.err.println("[EXEC] Download failed for student: " + studentNick);
            return CheckResult.failedDownload(checkAssignment);
        }

        // 2. Identify the Project Directory (The subfolder where gradlew lives)
        Path projectPath = clonedPath.resolve(taskId);
        System.out.println("[EXEC] Processing task folder: " + projectPath.toAbsolutePath());

        // 3. Compile
        boolean isCompiled = RepositoryWorker.compileProject(projectPath);
        if (!isCompiled) {
            System.err.println("[EXEC] Compilation failed for task: " + taskId);
            return CheckResult.failedBuild(checkAssignment);
        }

        // 4. Documentation
        boolean hasDocs = RepositoryWorker.generateDocumentation(projectPath);

        // 5. Code Style
        boolean styleOk = RepositoryWorker.checkCodeStyle(projectPath);

        // 6. Tests
        RepositoryWorker.TestResults testResults = RepositoryWorker.runTests(projectPath);
        System.out.println("[EXEC] Test results: " + testResults);

        return new CheckResult(
                checkAssignment,
                true,          // downloaded
                isCompiled,    // compiled
                hasDocs,       // docs
                styleOk,       // style
                testResults.passed,
                testResults.failed,
                testResults.skipped
        );
    }
}