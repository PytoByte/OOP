package Domain;

public record CheckResult(
    CheckAssignment checkAssignment,
    boolean download,
    boolean build,
    boolean doc,
    boolean style,
    int passedTestsCount,
    int failedTestsCount,
    int points
) {
    public static CheckResult failedDownload(CheckAssignment checkAssignment) {
        return new CheckResult(
                checkAssignment,
                false,
                false,
                false,
                false,
                0,
                0,
                0
        );
    }

    public static CheckResult failedBuild(CheckAssignment checkAssignment) {
        return new CheckResult(
                checkAssignment,
                true,
                false,
                false,
                false,
                0,
                0,
                0
        );
    }
}
