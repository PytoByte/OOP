package services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import model.CheckAssignment;
import model.CheckResult;
import model.Checkpoint;
import model.CommandExecutor;
import model.TestResults;

/**
 * Executor of check assignments.
 */
public class Executor {
    private final CommandExecutor commandExecutor;
    private final Path tempDir;
    private final Path toolsDir;
    private final String checkstyleUrl;

    public static final Path DEFAULT_TEMP_DIR = Path.of("oop-checker").toAbsolutePath();
    public static final Path DEFAULT_TOOLS_DIR = Path.of("tools").toAbsolutePath();
    public static final String DEFAULT_CHECKSTYLE_DOWNLOAD_URL = "https://github.com/checkstyle/"
            + "checkstyle/releases/download/checkstyle-10.17.0/checkstyle-10.17.0-all.jar";

    /**
     * Constructor with default paths and checkstyle repository url.
     *
     * @param commandExecutor any command executor
     */
    public Executor(CommandExecutor commandExecutor) {
        this(
                commandExecutor,
                DEFAULT_TEMP_DIR,
                DEFAULT_TOOLS_DIR,
                DEFAULT_CHECKSTYLE_DOWNLOAD_URL
        );
    }

    /**
     * Extended constructor with paths and checkstyle url selection.
     *
     * @param commandExecutor any command executor
     * @param tempDir path for dir with temp files. After checkstyle dir will be deleted
     * @param toolsDir dir with tools (checkstyle jar and checkstyle xml)
     * @param checkstyleUrl url with checkstyle jar
     */
    public Executor(
            CommandExecutor commandExecutor,
            Path tempDir,
            Path toolsDir,
            String checkstyleUrl
    ) {
        this.commandExecutor = commandExecutor;
        this.toolsDir = toolsDir.toAbsolutePath();
        this.checkstyleUrl = checkstyleUrl;
        this.tempDir = tempDir;
    }

    /**
     * Main method for execution many check assignments.
     *
     * @param checkAssignments list of check assignments
     */
    public List<CheckResult> execute(List<CheckAssignment> checkAssignments) {
        List<CheckResult> results = new LinkedList<>();
        Path absoluteWorkDir = tempDir.toAbsolutePath().normalize();

        try {
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

    /**
     * Executor of single check assignment.
     *
     * @param checkAssignment task check assignment
     * @param tempDir dir with temp files
     * @return result of check assignment
     */
    public CheckResult executeCheckAssignment(CheckAssignment checkAssignment, Path tempDir) {
        String repoUrl = String.format(
                "https://github.com/%s/OOP",
                checkAssignment.student().nick()
        );
        Path studentRepoPath = tempDir.resolve(checkAssignment.student().nick());

        Logger logger = new Logger("executor");
        logger.info("\n===\nCheck assignment\nGroup: %s\nStudent: %s\nTask: %s\n===",
                checkAssignment.group().name(),
                checkAssignment.student().name(),
                checkAssignment.task().id()
        );

        RepositoryWorker worker = new RepositoryWorker(commandExecutor, toolsDir, checkstyleUrl);

        try {
            if (!worker.cloneRepository(repoUrl, "main", studentRepoPath)) {
                logger.info("Check assignment ended with failed download");
                return CheckResult.failedDownload(checkAssignment);
            }
        } catch (Exception e) {
            logger.info("Check assignment ended with exception: %s", e.getMessage());
            return CheckResult.failedDownload(checkAssignment);
        }

        OffsetDateTime commitDateTime = worker.setTask(checkAssignment.task().id());
        if (commitDateTime == null) {
            logger.info("Check assignment ended with task not found");
            return CheckResult.taskNotFound(checkAssignment);
        }

        boolean isCompiled = worker.compileProject();
        boolean docsOk = worker.generateDocumentation();

        boolean styleOk = false;
        try {
            styleOk = worker.checkCodeStyle();
        } catch (Exception e) {
            logger.info("Check style failed with exception %s", e.getMessage());
        }

        TestResults testResults = null;
        try {
            testResults = worker.runTests();
        } catch (Exception e) {
            logger.info("Run tests failed with exception %s", e.getMessage());
        }

        float points = checkAssignment.task().basePoints();
        for (Checkpoint checkpoint : checkAssignment.task().checkpoints()) {
            if (!commitDateTime.toLocalDate().isAfter(checkpoint.date())) {
                points += checkpoint.rewardPoints();
            }
        }

        logger.info("Check assignment ended successfully");
        return new CheckResult(
                checkAssignment, commitDateTime, true, true, isCompiled,
                docsOk, styleOk, testResults, points
        );
    }

    /**
     * Delete temp dir.
     *
     * @param tempDir temp dir
     */
    private void cleanup(Path tempDir) {
        if (!Files.exists(tempDir)) {
            return;
        }
        try (var stream = Files.walk(tempDir)) {
            stream.sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(java.io.File::delete);
        } catch (IOException e) {
            System.err.println("Cleanup failed: " + e.getMessage());
        }
    }
}