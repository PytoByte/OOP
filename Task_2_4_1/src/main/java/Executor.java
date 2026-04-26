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
        Path studentRepoPath = workDir.resolve(studentNick);
        Path projectPath = studentRepoPath.resolve(taskId);

        String branchName = taskId.toLowerCase().replace('_', '-');

        // 1. Клон
        if (RepositoryWorker.cloneRepository(checkAssignment.student().repoUrl(), branchName, studentRepoPath) == null) {
            return CheckResult.failedDownload(checkAssignment);
        }

        // 2. Компиляция (теперь за раз и main, и tests)
        boolean isCompiled = RepositoryWorker.compileProject(projectPath);
        if (!isCompiled) return CheckResult.failedBuild(checkAssignment);

        // 3. Javadoc
        boolean docsOk = RepositoryWorker.generateDocumentation(projectPath);

        // 4. Checkstyle (теперь CLI и берет конфиг из ресурсов сам)
        boolean styleOk = RepositoryWorker.checkCodeStyle(projectPath);

        // 5. Тесты
        RepositoryWorker.TestResults testResults = RepositoryWorker.runTests(projectPath);

        return new CheckResult(checkAssignment, true, isCompiled, docsOk, styleOk,
                testResults.passed, testResults.failed, testResults.skipped);
    }
}