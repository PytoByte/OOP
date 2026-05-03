package Domain;

import java.time.OffsetDateTime;

public record CheckResult(
    CheckAssignment checkAssignment,
    OffsetDateTime completeDateTime,
    boolean download,
    boolean build,
    boolean doc,
    boolean style,
    boolean tests,
    int passedTestsCount,
    int failedTestsCount,
    int skippedTestsCount,
    float points
) {
    public static CheckResult failedDownload(CheckAssignment checkAssignment) {
        return new CheckResult(
                checkAssignment,
                null,
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
