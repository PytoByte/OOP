package Services;

import Domain.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.OffsetDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Comparator;

public class Executor {
    private final CommandExecutor commandExecutor;
    private final Path workDir;
    private final Path toolsDir;
    private final String checkstyleUrl;

    private final static Path DEFAULT_WORKDIR = Path.of("oop-checker").toAbsolutePath();
    private final static Path DEFAULT_TOOLS_DIR = Path.of("tools").toAbsolutePath();

    public Executor(CommandExecutor commandExecutor) {
        this(
                commandExecutor,
                DEFAULT_WORKDIR,
                DEFAULT_TOOLS_DIR,
                "https://github.com/checkstyle/checkstyle/releases/download/" +
                        "checkstyle-10.17.0/checkstyle-10.17.0-all.jar"
        );
    }

    public Executor(
            CommandExecutor commandExecutor,
            Path workDir,
            Path toolsDir,
            String checkstyleUrl
    ) {
        this.commandExecutor = commandExecutor;
        this.toolsDir = toolsDir.toAbsolutePath();
        this.checkstyleUrl = checkstyleUrl;
        this.workDir = workDir;
    }

    /**
     * Основной метод запуска проверки.
     * @param checkAssignments список заданий
     */
    public List<CheckResult> execute(List<CheckAssignment> checkAssignments) {
        List<CheckResult> results = new LinkedList<>();
        Path absoluteWorkDir = workDir.toAbsolutePath().normalize();

        try {
            // Очищаем и пересоздаем рабочую папку
            cleanup(absoluteWorkDir);
            Files.createDirectories(absoluteWorkDir);

            for (CheckAssignment assignment : checkAssignments) {
                results.add(executeCheckAssignment(assignment, absoluteWorkDir));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            cleanup(absoluteWorkDir);
        }
        return results;
    }

    // Внутренняя логика осталась прежней, но теперь она использует переданный путь
    public CheckResult executeCheckAssignment(CheckAssignment checkAssignment, Path workDir) {
        String repoUrl = String.format("https://github.com/%s/OOP", checkAssignment.student().nick());
        Path studentRepoPath = workDir.resolve(checkAssignment.student().nick());
        String taskId = checkAssignment.task().id();

        Logger logger = new Logger("executor");
        logger.info("=== STUDENT: %s TASK: %s ===", checkAssignment.student().name(), taskId);

        RepositoryWorker worker = new RepositoryWorker(commandExecutor, toolsDir, checkstyleUrl);

        if (!worker.cloneRepository(repoUrl, "main", studentRepoPath)) {
            return CheckResult.failedDownload(checkAssignment);
        }

        OffsetDateTime commitDateTime = worker.setTask(taskId);
        if (commitDateTime == null) {
            return CheckResult.failedDownload(checkAssignment);
        }

        boolean isCompiled = worker.compileProject();
        boolean docsOk = worker.generateDocumentation();
        boolean styleOk = worker.checkCodeStyle();

        TestResults testResults = worker.runTests();
        boolean testsCompiled = !testResults.isError();

        // Расчет баллов
        float points = checkAssignment.task().basePoints();
        for (Checkpoint checkpoint : checkAssignment.task().checkpoints()) {
            if (!commitDateTime.toLocalDate().isAfter(checkpoint.date())) {
                points += checkpoint.rewardPoints();
            }
        }

        return new CheckResult(
                checkAssignment, commitDateTime, true, isCompiled,
                docsOk, styleOk, testsCompiled,
                testResults.passed(), testResults.failed(), testResults.skipped(),
                points
        );
    }

    private void cleanup(Path workDir) {
        if (!Files.exists(workDir)) return;
        try (var stream = Files.walk(workDir)) {
            stream.sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(java.io.File::delete);
        } catch (IOException e) {
            System.err.println("Cleanup failed: " + e.getMessage());
        }
    }
}