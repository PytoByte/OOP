package Domain;

public record CheckResult(
    CheckAssignment checkAssignment,
    boolean download,
    boolean build,
    boolean doc,
    boolean style,
    boolean tests,
    int passedTestsCount,
    int failedTestsCount,
    int skippedTestsCount,
    int points
) {
    public static CheckResult failedDownload(CheckAssignment checkAssignment) {
        return new CheckResult(
                checkAssignment,
                false,
                false,
                false,
                false,
                false,
                0,
                0,
                0,
                0
        );
    }
}
